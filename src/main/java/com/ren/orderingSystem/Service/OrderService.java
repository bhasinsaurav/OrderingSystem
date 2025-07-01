package com.ren.orderingSystem.Service;

import com.ren.orderingSystem.ApiContracts.RequestDto.AddCustomerAddressRequest;
import com.ren.orderingSystem.ApiContracts.RequestDto.OrderedItemsRequest;
import com.ren.orderingSystem.ApiContracts.RequestDto.PlaceOrderRequest;
import com.ren.orderingSystem.ApiContracts.WebSocketDto.SendOrderToRestaurant;
import com.ren.orderingSystem.Entity.*;
import com.ren.orderingSystem.Exceptions.RestaurantNotFoundException;
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
    private final OrderMapper orderMapper;
    private final OrderItemsMapper orderItemsMapper;
    private final RestaurantRepository restaurantRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final WebSocketMapper webSocketMapper;
    @Autowired
    public OrderService(UserRepository userRepository, UserMapper userMapper,
                        CustomerRepository customerRepository, CustomerAddressMapper customerAddressMapper,
                        OrderMapper orderMapper, OrderItemsMapper orderItemsMapper,
                        RestaurantRepository restaurantRepository, SimpMessagingTemplate messagingTemplate,
                        WebSocketMapper webSocketMapper){
        this.userRepository= userRepository;
        this.userMapper = userMapper;
        this.customerAddressMapper = customerAddressMapper;
        this.orderMapper = orderMapper;
        this.orderItemsMapper = orderItemsMapper;
        this.restaurantRepository= restaurantRepository;
        this.messagingTemplate= messagingTemplate;
        this.webSocketMapper= webSocketMapper;
    }

    @Transactional
    public void placeOrder(PlaceOrderRequest placeOrderRequest, UUID restaurantUserId){
        Optional<Restaurant> byUserUserId = restaurantRepository.findByUser_UserId(restaurantUserId);
        Restaurant restaurant = byUserUserId
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found for user ID: " + restaurantUserId));

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

        User savedUser = userRepository.save(userEntity);
        SendOrderToRestaurant sendOrderToRestaurant = webSocketMapper.mapOrderToSendToRestaurant(savedUser);


        log.info("Username in service class is :" + restaurant.getUser().getUserName());
        messagingTemplate.convertAndSendToUser(
                restaurant.getUser().getUserName(),
                "/queue/new-order",
                sendOrderToRestaurant
        );

    }
}
