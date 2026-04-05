package com.ren.orderingSystem.Service;

import com.ren.orderingSystem.ApiContracts.RequestDto.*;
import com.ren.orderingSystem.ApiContracts.WebSocketDto.OrderResponse;
import com.ren.orderingSystem.Entity.*;
import com.ren.orderingSystem.Mappers.*;
import com.ren.orderingSystem.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private RestaurantRepository restaurantRepository;
    @Mock private OrderRepository orderRepository;
    @Mock private UserMapper userMapper;
    @Mock private CustomerAddressMapper customerAddressMapper;
    @Mock private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private OrderService orderService;

    private PlaceOrderRequest placeOrderRequest;
    private long restaurantId = 1L;

    @BeforeEach
    void setUp() {
        placeOrderRequest = new PlaceOrderRequest();
        
        // Mock User Request
        AddUserDetailForCustomerRequest userDetailRequest = new AddUserDetailForCustomerRequest();
        userDetailRequest.setEmail("customer@test.com");
        
        AddCustomerDetailRequest customerDetailRequest = new AddCustomerDetailRequest();
        AddCustomerAddressRequest addressRequest = new AddCustomerAddressRequest();
        addressRequest.setCity("Toronto");
        addressRequest.setCountry("Canada");
        customerDetailRequest.setCustomerAddressRequestList(Set.of(addressRequest));
        userDetailRequest.setAddCustomerDetailRequest(customerDetailRequest);
        
        placeOrderRequest.setAddUserDetailForCustomerRequest(userDetailRequest);

        // Mock Ordered Items
        OrderedItemsRequest item = new OrderedItemsRequest();
        item.setMenuItemId(101L);
        item.setMenuItemName("Burger");
        item.setMenuItemPrice(new BigDecimal("10.00"));
        item.setQuantity(2L);
        placeOrderRequest.setOrderedItemsRequestsList(List.of(item));
    }

    @Test
    void testPlaceOrder_Success_Orchestration() {
        // Arrange
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId(restaurantId);
        User restaurantUser = new User();
        restaurantUser.setUserName("owner_user");
        restaurant.setUser(restaurantUser);

        User existingUser = new User();
        Customer existingCustomer = new Customer();
        existingCustomer.setCustomerId(500L);
        
        CustomerAddress address = new CustomerAddress();
        address.setCity("Toronto");
        existingCustomer.setCustomerAddresses(Set.of(address));
        
        existingUser.setCustomer(existingCustomer);
        existingUser.setUserId(UUID.randomUUID());
        existingUser.setEmail("customer@test.com");

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(userRepository.findByUserName("customer@test.com")).thenReturn(existingUser);
        when(userRepository.save(any(User.class))).thenReturn(existingUser);
        
        // Mock the internal save to verify it works independently
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order o = invocation.getArgument(0);
            o.setOrderId(999L);
            return o;
        });

        // Act
        OrderResponse response = orderService.placeOrder(placeOrderRequest, restaurantId);

        // Assert
        assertNotNull(response);
        assertEquals(999L, response.getOrderId());
        
        // VERIFY DECOUPLING:
        // 1. Verify we extracted the Customer ID correctly
        verify(orderRepository).save(argThat(order -> 
            order.getCustomerId() == 500L && 
            order.getRestaurantId() == restaurantId &&
            order.getTotalAmount().compareTo(new BigDecimal("20.00")) == 0
        ));

        // 2. Verify WebSocket notification was attempted for the correct restaurant user
        verify(messagingTemplate).convertAndSendToUser(
                eq("owner_user"), 
                eq("/queue/new-order"), 
                any(OrderResponse.class)
        );
    }
}
