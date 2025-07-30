package com.ren.orderingSystem.Service;

import com.ren.orderingSystem.ApiContracts.RequestDto.AddCustomerAddressRequest;
import com.ren.orderingSystem.ApiContracts.RequestDto.OrderedItemsRequest;
import com.ren.orderingSystem.ApiContracts.RequestDto.PlaceOrderRequest;
import com.ren.orderingSystem.ApiContracts.RequestDto.UpdateStatusDto;
import com.ren.orderingSystem.ApiContracts.ResponseDto.TotalOrdersResponse;
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
    private final OrderItemsMapper orderItemsMapper;
    private final RestaurantRepository restaurantRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final WebSocketMapper webSocketMapper;
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(UserRepository userRepository, UserMapper userMapper,
                        CustomerRepository customerRepository, CustomerAddressMapper customerAddressMapper,
                        OrderMapper orderMapper, OrderItemsMapper orderItemsMapper,
                        RestaurantRepository restaurantRepository, SimpMessagingTemplate messagingTemplate,
                        WebSocketMapper webSocketMapper, OrderRepository orderRepository){
        this.userRepository= userRepository;
        this.userMapper = userMapper;
        this.customerAddressMapper = customerAddressMapper;
        this.orderMapper = orderMapper;
        this.orderItemsMapper = orderItemsMapper;
        this.restaurantRepository= restaurantRepository;
        this.messagingTemplate= messagingTemplate;
        this.webSocketMapper= webSocketMapper;
        this.orderRepository= orderRepository;
    }

    @Transactional
    public OrderResponse placeOrder(PlaceOrderRequest placeOrderRequest, UUID restaurantUserId){

        Optional<Restaurant> byUserUserId = restaurantRepository.findByUser_UserId(restaurantUserId);
        Restaurant restaurant = byUserUserId
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

            //Now map Order
            // set order items in entity
            List<OrderedItemsRequest> orderedItemsRequestsList = placeOrderRequest.getOrderedItemsRequestsList();

            List<OrderItems> orderItemsList = orderedItemsRequestsList.stream().map(dto -> orderItemsMapper.toOrderItemsEntity(dto)).toList();

        // populate order entity
        Order order = new Order();
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        Order orderEntity = orderMapper.toOrderEntity(orderItemsList, order);
        orderItemsList.forEach(item -> item.setOrder(orderEntity));

        orderEntity.setOrderItems(orderItemsList);

        //Connect each entity
        Set<Order> orderSet = customer.getOrders() != null ? customer.getOrders() : new HashSet<>();
        orderSet.add(orderEntity);
        customer.setOrders(orderSet);
        orderEntity.setCustomer(customer);

        Set<Order> restaurantOrders = restaurant.getOrders() != null ? restaurant.getOrders() : new HashSet<>();
        restaurantOrders.add(orderEntity);
        restaurant.setOrders(restaurantOrders);
        orderEntity.setRestaurant(restaurant);

        User savedUser = userRepository.saveAndFlush(user);
        savedUser = userRepository.findByIdWithCustomer(savedUser.getUserId()).orElseThrow(() -> new RuntimeException("User not found after save"));
        OrderResponse sendOrderInfo = webSocketMapper.mapOrderToSendToResponse(savedUser, null);


        log.info("Username in service class is: " + restaurant.getUser().getUserName());
        messagingTemplate.convertAndSendToUser(
                restaurant.getUser().getUserName(),
                "/queue/new-order",
                sendOrderInfo
        );

        return sendOrderInfo;

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

    public TotalOrdersResponse getTotalOrdersByUserId(UUID userId ) {
        TotalOrdersResponse totalOrdersResponse = new TotalOrdersResponse();
        User user = userRepository.findById(userId).orElseThrow(() -> new RestaurantNotFoundException("No user found with ID: " + userId));
        Set<Order> orders = user.getRestaurant().getOrders();
        List<OrderResponse> allOrderList = orders.stream().map(order -> webSocketMapper.mapOrderToSendToResponse(null, order))
                .toList();
        totalOrdersResponse.setAllOrders(allOrderList);
        return totalOrdersResponse;
    }

}
