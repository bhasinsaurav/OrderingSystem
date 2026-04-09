package com.ren.orderingSystem.Service;

import com.ren.orderingSystem.ApiContracts.RequestDto.*;
import com.ren.orderingSystem.ApiContracts.WebSocketDto.OrderResponse;
import com.ren.orderingSystem.Entity.*;
import com.ren.orderingSystem.Mappers.*;
import com.ren.orderingSystem.Feign.OrderFeignClient;
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
    @Mock private UserMapper userMapper;
    @Mock private CustomerAddressMapper customerAddressMapper;
    @Mock private SimpMessagingTemplate messagingTemplate;
    @Mock private OrderFeignClient orderFeignClient;

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
        UUID restaurantUserId = UUID.randomUUID();
        User restaurantUserEntity = new User();
        restaurantUserEntity.setUserId(restaurantUserId);
        restaurantUserEntity.setUserName("owner_user");
        
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId(10L);
        restaurantUserEntity.setRestaurant(restaurant);
        restaurant.setUser(restaurantUserEntity);

        User existingCustomerUser = new User();
        Customer existingCustomer = new Customer();
        existingCustomer.setCustomerId(500L);
        
        CustomerAddress address = new CustomerAddress();
        address.setCity("Toronto");
        existingCustomer.setCustomerAddresses(Set.of(address));
        
        existingCustomerUser.setCustomer(existingCustomer);
        existingCustomerUser.setUserId(UUID.randomUUID());
        existingCustomerUser.setEmail("customer@test.com");

        when(userRepository.findById(restaurantUserId)).thenReturn(Optional.of(restaurantUserEntity));
        when(userRepository.findByUserName("customer@test.com")).thenReturn(existingCustomerUser);
        when(userRepository.save(any(User.class))).thenReturn(existingCustomerUser);
        
        // Mock the Feign Client call
        OrderResponse feignResponse = new OrderResponse();
        feignResponse.setOrderId(999L);
        when(orderFeignClient.placeOrder(any(OrderRequestToOrderService.class))).thenReturn(feignResponse);

        // Act
        OrderResponse response = orderService.placeOrder(placeOrderRequest, restaurantUserId);

        // Assert
        assertNotNull(response);
        assertEquals(999L, response.getOrderId());
        
        // VERIFY ORCHESTRATION:
        // 1. Verify Feign Client was called with correctly enriched data
        verify(orderFeignClient).placeOrder(argThat(command -> 
            command.getCustomerId() == 500L && 
            command.getRestaurantId() == 10L
        ));

        // 2. Verify WebSocket notification was attempted for the owner
        verify(messagingTemplate).convertAndSendToUser(
                eq("owner_user"), 
                eq("/queue/new-order"), 
                any(OrderResponse.class)
        );
    }
}
