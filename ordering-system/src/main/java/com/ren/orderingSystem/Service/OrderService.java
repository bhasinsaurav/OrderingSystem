package com.ren.orderingSystem.Service;

import com.ren.orderingSystem.ApiContracts.RequestDto.*;
import com.ren.orderingSystem.ApiContracts.ResponseDto.TotalOrdersResponse;
import com.ren.orderingSystem.ApiContracts.WebSocketDto.IncludeCustomerAddressInfo;
import com.ren.orderingSystem.ApiContracts.WebSocketDto.IncludeCustomerInfo;
import com.ren.orderingSystem.ApiContracts.WebSocketDto.OrderResponse;
import com.ren.orderingSystem.Entity.*;
import com.ren.orderingSystem.Exceptions.RestaurantNotFoundException;
import com.ren.orderingSystem.Feign.OrderFeignClient;
import com.ren.orderingSystem.Mappers.*;

import com.ren.orderingSystem.repository.CustomerRepository;
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
    private final RestaurantRepository restaurantRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final OrderFeignClient orderFeignClient;

    @Autowired
    public OrderService(UserRepository userRepository, UserMapper userMapper,
                        CustomerRepository customerRepository, CustomerAddressMapper customerAddressMapper,
                        RestaurantRepository restaurantRepository, SimpMessagingTemplate messagingTemplate, OrderFeignClient orderFeignClient){
        this.userRepository= userRepository;
        this.userMapper = userMapper;
        this.customerAddressMapper = customerAddressMapper;
        this.restaurantRepository= restaurantRepository;
        this.messagingTemplate= messagingTemplate;
        this.orderFeignClient = orderFeignClient;
    }

    @Transactional
    public OrderResponse placeOrder(PlaceOrderRequest placeOrderRequest, UUID userId){

        Optional<User> userById = userRepository.findById(userId);
        User restaurantUser = userById.orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found for user ID: " + userId));
        Restaurant restaurant = restaurantUser.getRestaurant();
        Long restaurantId = restaurant.getRestaurantId();
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
            orderRequestToOrderService.setRestaurantId(restaurantId);
            orderRequestToOrderService.setOrderedItemsRequests(placeOrderRequest.getOrderedItemsRequestsList());
        OrderResponse orderResponse = orderFeignClient.placeOrder(orderRequestToOrderService);

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

    public void updateStatus(UpdateStatusDto updateStatusDto, UUID customerUserId) {

        orderFeignClient.updateStatus(updateStatusDto);


        messagingTemplate.convertAndSend(
                "/topic/order-status/" + customerUserId + "/" + updateStatusDto.getOrderId(),
                updateStatusDto.getOrderStatus()
        );
    }

    public TotalOrdersResponse getOrdersByRestaurantId(UUID userId) {

        Optional<User> byId = userRepository.findById(userId);
        User user = byId.orElseThrow(() -> new RestaurantNotFoundException("no restaurant found for given user Id"));
        long restaurantId = user.getRestaurant().getRestaurantId();
        return orderFeignClient.getOrdersByRestaurant(restaurantId);
    }
}
