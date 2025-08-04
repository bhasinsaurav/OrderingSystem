package com.ren.orderingSystem.Mappers;

import com.ren.orderingSystem.ApiContracts.WebSocketDto.IncludeCustomerAddressInfo;
import com.ren.orderingSystem.ApiContracts.WebSocketDto.IncludeCustomerInfo;
import com.ren.orderingSystem.ApiContracts.WebSocketDto.IncludeOrderItemsInfo;
import com.ren.orderingSystem.ApiContracts.WebSocketDto.OrderResponse;
import com.ren.orderingSystem.Entity.CustomerAddress;
import com.ren.orderingSystem.Entity.Order;
import com.ren.orderingSystem.Entity.OrderItems;
import com.ren.orderingSystem.Entity.User;
import com.ren.orderingSystem.repository.OrderRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WebSocketMapper {

    private final OrderRepository orderRepository;

    public WebSocketMapper(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    public OrderResponse mapOrderToSendToResponse(User user, Order order){

        OrderResponse sendOrderResponse = new OrderResponse();
        if(order==null && user!=null) {
            order = orderRepository.findTopByCustomer_CustomerIdOrderByCreatedAtDesc(user.getCustomer().getCustomerId());
        }
        //Setting yp customer address info
        IncludeCustomerAddressInfo customerAddressInfo = new IncludeCustomerAddressInfo();
        CustomerAddress customerAddress;

        if(user == null && order != null) {
            user = order.getCustomer().getUser();
        }
        customerAddress = user.getCustomer().getCustomerAddresses().iterator().next();
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

        //Setting up Order Items info to send to frontend

        List<IncludeOrderItemsInfo> orderItems = order
                .getOrderItems().stream()
                .map(this::includeOrderItemsInfo)
                .toList();

        // Seeting up objects to the reference in web socket dto
        sendOrderResponse.setOrderItemsInfo(orderItems);
        sendOrderResponse.setIncludeCustomerInfo(customerInfo);
        sendOrderResponse.setOrderTotal(order.getTotalAmount());
        sendOrderResponse.setOrderId(order.getOrderId());
        sendOrderResponse.setOrderStatus(order.getOrderStatus().toString());
        return sendOrderResponse;
    }

    public IncludeOrderItemsInfo includeOrderItemsInfo(OrderItems orderItems){
        IncludeOrderItemsInfo includeOrderItemsInfo = new IncludeOrderItemsInfo();
        includeOrderItemsInfo.setItemName(orderItems.getMenuItem().getItemName());
        includeOrderItemsInfo.setQuantity(orderItems.getQuantity());
        includeOrderItemsInfo.setItemPrice(orderItems.getItemPrice());
        includeOrderItemsInfo.setItemTotalPrice(orderItems.getItemTotalPrice());
        return includeOrderItemsInfo;
    }

}
