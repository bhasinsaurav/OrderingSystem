package com.ren.orderingSystem.Service;

import com.ren.orderingSystem.ApiContracts.RequestDto.AddCustomerAddressRequest;
import com.ren.orderingSystem.ApiContracts.RequestDto.OrderedItemsRequest;
import com.ren.orderingSystem.ApiContracts.RequestDto.PlaceOrderRequest;
import com.ren.orderingSystem.Entity.*;
import com.ren.orderingSystem.Mappers.CustomerAddressMapper;
import com.ren.orderingSystem.Mappers.OrderItemsMapper;
import com.ren.orderingSystem.Mappers.OrderMapper;
import com.ren.orderingSystem.Mappers.UserMapper;
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
    private final OrderMapper orderMapper;
    private final OrderItemsMapper orderItemsMapper;
    private final RestaurantRepository restaurantRepository;
    private final SimpMessagingTemplate messagingTemplate;
    @Autowired
    public OrderService(UserRepository userRepository, UserMapper userMapper,
                        CustomerRepository customerRepository, CustomerAddressMapper customerAddressMapper,
                        OrderMapper orderMapper, OrderItemsMapper orderItemsMapper,
                        RestaurantRepository restaurantRepository, SimpMessagingTemplate messagingTemplate){
        this.userRepository= userRepository;
        this.userMapper = userMapper;
        this.customerAddressMapper = customerAddressMapper;
        this.orderMapper = orderMapper;
        this.orderItemsMapper = orderItemsMapper;
        this.restaurantRepository= restaurantRepository;
        this.messagingTemplate= messagingTemplate;
    }

    @Transactional
    public void placeOrder(PlaceOrderRequest placeOrderRequest, UUID restaurantUserId){
        Optional<Restaurant> byUserUserId = restaurantRepository.findByUser_UserId(restaurantUserId);
        Restaurant restaurant = byUserUserId
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found for user ID: " + restaurantUserId));

        User user = new User();
        user.setUserTimestamp(LocalDateTime.now());
        User userEntity = userMapper.toUserEntity(user, placeOrderRequest.getAddUserDetailForCustomerRequest());

        Customer customer = new Customer();
        customer.setUser(userEntity); // Link user to customer
        userEntity.setCustomer(customer);

// Now map addresses

        Set<AddCustomerAddressRequest> addCustomerAddressRequest = placeOrderRequest.getAddUserDetailForCustomerRequest().getAddCustomerDetailRequest().getCustomerAddressRequestList();
        Set<CustomerAddress> addressEntities = addCustomerAddressRequest.stream()
                .map(dto -> customerAddressMapper.toCustomerAddressEntity(dto,  customer))
                .collect(Collectors.toSet());
        customer.setCustomerAddresses(addressEntities);


        //Now map Order
        Order order = new Order();
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        Order orderEntity = orderMapper.toOrderEntity(placeOrderRequest, order);

        List<OrderedItemsRequest> orderedItemsRequestsList = placeOrderRequest.getOrderedItemsRequestsList();
        List<OrderItems> orderItemsList = orderedItemsRequestsList.stream().map(dto -> orderItemsMapper.toOrderItemsEntity(dto)).toList();
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

        userRepository.save(userEntity);

        log.info("Username in service class is :" + restaurant.getUser().getUserName());
        messagingTemplate.convertAndSendToUser(
                restaurant.getUser().getUserName(),
                "/queue/new-order",
                placeOrderRequest //
        );

    }
}
