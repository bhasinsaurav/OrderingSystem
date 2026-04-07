package com.ren.orderingSystem.Service;

import com.ren.orderingSystem.ApiContracts.RequestDto.*;
import com.ren.orderingSystem.ApiContracts.ResponseDto.TotalOrdersResponse;
import com.ren.orderingSystem.ApiContracts.WebSocketDto.IncludeCustomerAddressInfo;
import com.ren.orderingSystem.ApiContracts.WebSocketDto.IncludeCustomerInfo;
import com.ren.orderingSystem.ApiContracts.WebSocketDto.IncludeOrderItemsInfo;
import com.ren.orderingSystem.ApiContracts.WebSocketDto.OrderResponse;
import com.ren.orderingSystem.Entity.*;
import com.ren.orderingSystem.Enum.OrderStatus;
import com.ren.orderingSystem.Exceptions.RestaurantNotFoundException;
import com.ren.orderingSystem.Mappers.*;
import com.ren.orderingSystem.repository.CustomerRepository;
import com.ren.orderingSystem.repository.OrderRepository;
import com.ren.orderingSystem.repository.RestaurantRepository;
import com.ren.orderingSystem.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CustomerAddressMapper customerAddressMapper;
    private final OrderMapper orderMapper;
    private final RestaurantRepository restaurantRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public OrderService(UserRepository userRepository, UserMapper userMapper,
                        CustomerRepository customerRepository, CustomerAddressMapper customerAddressMapper,
                        OrderMapper orderMapper,
                        RestaurantRepository restaurantRepository, SimpMessagingTemplate messagingTemplate, OrderRepository orderRepository){
        this.userRepository= userRepository;
        this.userMapper = userMapper;
        this.customerAddressMapper = customerAddressMapper;
        this.orderMapper = orderMapper;
        this.restaurantRepository= restaurantRepository;
        this.messagingTemplate= messagingTemplate;
        this.orderRepository= orderRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public OrderResponse placeOrder(PlaceOrderRequest placeOrderRequest, long restaurantUserId){


        Optional<Restaurant> byId = restaurantRepository.findById(restaurantUserId);
        Restaurant restaurant = byId
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found for user ID: " + restaurantUserId));

        User user = new User();
        Customer customer = new Customer();

            User byUserName = userRepository.findByUserName(placeOrderRequest.getAddUserDetailForCustomerRequest().getEmail());
            if(byUserName != null) {
                user = byUserName;
                customer= user.getCustomer();
            }
            else{

            log.info("Creating new customer with email: {}", placeOrderRequest.getAddUserDetailForCustomerRequest().getEmail());
            user.setUserTimestamp(LocalDateTime.now());
            user = userMapper.toUserEntity(user, placeOrderRequest.getAddUserDetailForCustomerRequest());

            customer.setUser(user); // Link user to customer
            user.setCustomer(customer);


// Now map addresses

            Set<AddCustomerAddressRequest> addCustomerAddressRequest = placeOrderRequest.getAddUserDetailForCustomerRequest().getAddCustomerDetailRequest().getCustomerAddressRequestList();
                Customer finalCustomer = customer;
                Set<CustomerAddress> addressEntities = addCustomerAddressRequest.stream()
                    .map(dto -> customerAddressMapper.toCustomerAddressEntity(dto, finalCustomer))
                    .collect(Collectors.toSet());
            customer.setCustomerAddresses(addressEntities);
        }
        User savedUser = userRepository.save(user);
        //create order request object for order service
            OrderRequestToOrderService orderRequestToOrderService = new OrderRequestToOrderService();
            orderRequestToOrderService.setCustomerId(savedUser.getCustomer().getCustomerId());
            orderRequestToOrderService.setRestaurantId(restaurantUserId);
            orderRequestToOrderService.setOrderedItemsRequests(placeOrderRequest.getOrderedItemsRequestsList());
        OrderResponse orderResponse = placeOrderInOrderService(orderRequestToOrderService);

        IncludeCustomerAddressInfo customerAddressInfo = new IncludeCustomerAddressInfo();
        CustomerAddress customerAddress;

        customerAddress = savedUser.getCustomer().getCustomerAddresses().iterator().next();
        customerAddressInfo.setCountry(customerAddress.getCountry());
        customerAddressInfo.setCity(customerAddress.getCity());
        customerAddressInfo.setStreetAddress1(customerAddress.getStreetAddress1());
        customerAddressInfo.setStreetAddress2(customerAddress.getStreetAddress2());
        customerAddressInfo.setPinCode(customerAddress.getPinCode());
        customerAddressInfo.setProvince(customerAddress.getProvince());

        // Setting up customer info to send in webSocket
        IncludeCustomerInfo customerInfo = new IncludeCustomerInfo();
        customerInfo.setCustomerUserId(user.getUserId());
        customerInfo.setEmail(user.getEmail());
        customerInfo.setFirstName(user.getFirstName());
        customerInfo.setLastName(user.getLastName());
        customerInfo.setPhoneNumber(user.getPhoneNumber());
        customerInfo.setIncludeCustomerAddressInfo(customerAddressInfo);


        orderResponse.setIncludeCustomerInfo(customerInfo);


        log.info("Username in service class is: " + restaurant.getUser().getUserName());
        messagingTemplate.convertAndSendToUser(
                restaurant.getUser().getUserName(),
                "/queue/new-order",
                orderResponse
        );

        return orderResponse;

    }

    public OrderResponse placeOrderInOrderService(OrderRequestToOrderService orderRequestToOrderService){
        List<OrderItems> orderItems = new ArrayList<>();
        BigDecimal orderTotal = BigDecimal.ZERO;

        //Create list of Order Items


        //Create and save Order
        Order order = new Order();


        for(OrderedItemsRequest orderedItemsRequest : orderRequestToOrderService.getOrderedItemsRequests()){
            OrderItems orderItem =  new OrderItems();
            orderItem.setMenuItemName(orderedItemsRequest.getMenuItemName());
            orderItem.setItemPrice(orderedItemsRequest.getMenuItemPrice());
            orderItem.setMenuItemId(orderedItemsRequest.getMenuItemId());
            orderItem.setQuantity(orderedItemsRequest.getQuantity());
            BigDecimal itemTotal = orderedItemsRequest.getMenuItemPrice().multiply(BigDecimal.valueOf(orderedItemsRequest.getQuantity()));
            orderItem.setItemTotalPrice(itemTotal);
            orderTotal = orderTotal.add(itemTotal);
            orderItem.setOrder(order);
            orderItems.add(orderItem);
        }

        order.setCustomerId(orderRequestToOrderService.getCustomerId());
        order.setRestaurantId(orderRequestToOrderService.getRestaurantId());
        order.setOrderItems(orderItems);
        order.setTotalAmount(orderTotal);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.PENDING);

        Order savedOrder = orderRepository.save(order);

        return getOrderResponse(savedOrder);

    }

    private static OrderResponse getOrderResponse(Order savedOrder) {
        List<IncludeOrderItemsInfo> includeOrderItemsInfos = new ArrayList<>();
        for(OrderItems orderItem : savedOrder.getOrderItems()){
            IncludeOrderItemsInfo includeOrderItemsInfo = new IncludeOrderItemsInfo();
            includeOrderItemsInfo.setItemName(orderItem.getMenuItemName());
            includeOrderItemsInfo.setItemPrice(orderItem.getItemPrice());
            includeOrderItemsInfo.setQuantity(orderItem.getQuantity());
            includeOrderItemsInfo.setItemTotalPrice(orderItem.getItemTotalPrice());
            includeOrderItemsInfos.add(includeOrderItemsInfo);
        }
        OrderResponse response =  new OrderResponse();
        response.setOrderId(savedOrder.getOrderId());
        response.setOrderItemsInfo(includeOrderItemsInfos);
        response.setOrderTotal(savedOrder.getTotalAmount());
        response.setOrderStatus(savedOrder.getOrderStatus());
        return response;
    }

    public void updateStatus(UpdateStatusDto updateStatusDto, UUID customerUserId) {
        String orderStatus = updateStatusDto.getOrderStatus();
        Order orderByOrderId = orderRepository.getOrderByOrderId(updateStatusDto.getOrderId());
        if (orderStatus.equals("Accepted")) {
            orderByOrderId.setOrderStatus(OrderStatus.ACCEPTED);
        } else if (orderStatus.equals("Rejected")) {
            orderByOrderId.setOrderStatus(OrderStatus.REJECTED);
        } else if (orderStatus.equals("Prepared")) {
            orderByOrderId.setOrderStatus(OrderStatus.PREPARED);
        } else if (orderStatus.equals("Delivered")) {
            orderByOrderId.setOrderStatus(OrderStatus.DELIVERED);
        } else if (orderStatus.equals("Cancelled")) {
            orderByOrderId.setOrderStatus(OrderStatus.CANCELED);
        }

        orderRepository.saveAndFlush(orderByOrderId);

        messagingTemplate.convertAndSend(
                "/topic/order-status/" + customerUserId+"/"+ orderByOrderId.getOrderId(),
                orderByOrderId.getOrderStatus().toString()
        );

    }

    public TotalOrdersResponse getOrdersByRestaurantId(long restaurantId){

        List<Order> byRestaurantId = orderRepository.findByRestaurantId(restaurantId);
        TotalOrdersResponse totalOrdersResponse = new TotalOrdersResponse();
        totalOrdersResponse.setAllOrders(byRestaurantId);
        return totalOrdersResponse;
    }
}
