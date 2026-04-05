package com.ren.orderingSystem.Service;

import com.ren.orderingSystem.ApiContracts.RequestDto.PlaceOrderRequest;
import com.ren.orderingSystem.ApiContracts.RequestDto.AddUserDetailForCustomerRequest;
import com.ren.orderingSystem.ApiContracts.RequestDto.OrderedItemsRequest;
import com.ren.orderingSystem.ApiContracts.WebSocketDto.OrderResponse;
import com.ren.orderingSystem.Entity.Order;
import com.ren.orderingSystem.Entity.Restaurant;
import com.ren.orderingSystem.Entity.User;
import com.ren.orderingSystem.Entity.Customer;
import com.ren.orderingSystem.repository.OrderRepository;
import com.ren.orderingSystem.repository.RestaurantRepository;
import com.ren.orderingSystem.repository.UserRepository;
import com.ren.orderingSystem.Mappers.OrderMapper;
import com.ren.orderingSystem.Mappers.OrderItemsMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private RestaurantRepository restaurantRepository;
    @Mock private OrderRepository orderRepository;
    @Mock private OrderMapper orderMapper;
    @Mock private OrderItemsMapper orderItemsMapper;
    @Mock private WebSocketMapper webSocketMapper;
    @Mock private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private OrderService orderService;

    private UUID restaurantUserId;
    private PlaceOrderRequest request;

    @BeforeEach
    void setUp() {
        restaurantUserId = UUID.randomUUID();
        request = new PlaceOrderRequest();
        
        AddUserDetailForCustomerRequest userRequest = new AddUserDetailForCustomerRequest();
        userRequest.setEmail("test@customer.com");
        request.setAddUserDetailForCustomerRequest(userRequest);
        
        OrderedItemsRequest itemRequest = new OrderedItemsRequest();
        itemRequest.setMenuItemId(101L);
        request.setOrderedItemsRequestsList(List.of(itemRequest));
    }

    @Test
    void testPlaceOrder_Success_Decoupled() {
        // Arrange
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId(1L);
        User restaurantUser = new User();
        restaurantUser.setUserName("restaurant_owner");
        restaurant.setUser(restaurantUser);
        
        User customerUser = new User();
        Customer customer = new Customer();
        customer.setCustomerId(99L);
        customerUser.setCustomer(customer);

        when(restaurantRepository.findByUser_UserId(restaurantUserId)).thenReturn(Optional.of(restaurant));
        when(userRepository.findByUserName("test@customer.com")).thenReturn(customerUser);
        
        // Mocking the behavior where the service saves the ORDER, not the USER
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);
        when(webSocketMapper.mapOrderToSendToResponse(any(), any())).thenReturn(new OrderResponse());

        // Act
        OrderResponse response = orderService.placeOrder(request, restaurantUserId);

        // Assert
        assertNotNull(response);
        
        // CRITICAL MIGRATION CHECKS:
        // 1. Verify we used the ID-only link
        verify(orderRepository).save(argThat(order -> 
            order.getCustomer() == 99L && 
            order.getRestaurant() == 1L
        ));
        
        // 2. Verify we didn't try to save the whole user aggregate (The "Old Way")
        verify(userRepository, never()).saveAndFlush(any());
        
        // 3. Verify notification was sent
        verify(messagingTemplate).convertAndSendToUser(eq("restaurant_owner"), anyString(), any(OrderResponse.class));
    }
}
