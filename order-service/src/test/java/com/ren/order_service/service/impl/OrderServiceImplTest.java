package com.ren.order_service.service.impl;

import com.ren.order_service.Enum.OrderStatus;

import com.ren.order_service.dto.*;
import com.ren.order_service.entity.Order;
import com.ren.order_service.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private OrderRequestToOrderService orderRequest;

    @BeforeEach
    void setUp() {
        orderRequest = new OrderRequestToOrderService();
        orderRequest.setCustomerId(1L);
        orderRequest.setRestaurantId(10L);

        OrderedItemsRequest item = new OrderedItemsRequest();
        item.setMenuItemId(101L);
        item.setMenuItemName("Pizza");
        item.setMenuItemPrice(new BigDecimal("15.00"));
        item.setQuantity(2L);

        orderRequest.setOrderedItemsRequests(List.of(item));
    }

    @Test
    void testPlaceOrder_Success() {
        // Arrange
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order o = invocation.getArgument(0);
            o.setOrderId(999L);
            return o;
        });

        // Act
        OrderResponse response = orderService.placeOrder(orderRequest);

        // Assert
        assertNotNull(response);
        assertEquals(999L, response.getOrderId());
        assertEquals(new BigDecimal("30.00"), response.getOrderTotal());
        assertEquals(OrderStatus.PENDING, response.getOrderStatus());
        assertEquals(1, response.getOrderItemsInfo().size());
        assertEquals("Pizza", response.getOrderItemsInfo().get(0).getItemName());

        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testUpdateStatus_Success() {
        // Arrange
        Order existingOrder = new Order();
        existingOrder.setOrderId(999L);
        existingOrder.setOrderStatus(OrderStatus.PENDING);

        UpdateStatusDto updateDto = new UpdateStatusDto();
        updateDto.setOrderId(999L);
        updateDto.setOrderStatus("Accepted");

        when(orderRepository.findById(999L)).thenReturn(Optional.of(existingOrder));

        // Act
        orderService.updateStatus(updateDto);

        // Assert
        assertEquals(OrderStatus.ACCEPTED, existingOrder.getOrderStatus());
        verify(orderRepository, times(1)).save(existingOrder);
    }

    @Test
    void testGetOrdersByRestaurantId_Success() {
        // Arrange
        Order order = new Order();
        order.setOrderId(1L);
        order.setRestaurantId(10L);
        order.setTotalAmount(new BigDecimal("30.00"));

        when(orderRepository.findByRestaurantId(10L)).thenReturn(List.of(order));

        // Act
        TotalOrdersResponse response = orderService.getOrdersByRestaurantId(10L);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getAllOrders().size());
        assertEquals(1L, response.getAllOrders().get(0).getOrderId());
        verify(orderRepository, times(1)).findByRestaurantId(10L);
    }
}
