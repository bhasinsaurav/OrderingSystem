package com.ren.orderingSystem.Mappers;

import com.ren.orderingSystem.ApiContracts.WebSocketDto.IncludeCustomerAddressInfo;
import com.ren.orderingSystem.ApiContracts.WebSocketDto.IncludeCustomerInfo;
import com.ren.orderingSystem.ApiContracts.WebSocketDto.IncludeOrderItemsInfo;
import com.ren.orderingSystem.ApiContracts.WebSocketDto.SendOrderToRestaurant;
import com.ren.orderingSystem.Entity.CustomerAddress;
import com.ren.orderingSystem.Entity.Order;
import com.ren.orderingSystem.Entity.OrderItems;
import com.ren.orderingSystem.Entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class WebSocketMapper {
    public SendOrderToRestaurant mapOrderToSendToRestaurant(User user){

        SendOrderToRestaurant sendOrderToRestaurant = new SendOrderToRestaurant();
        Order order = user.getCustomer().getOrders().iterator().next();

        //Setting yp customer address info
        IncludeCustomerAddressInfo customerAddressInfo = new IncludeCustomerAddressInfo();
        CustomerAddress customerAddress = user.getCustomer().getCustomerAddresses().iterator().next();
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
        sendOrderToRestaurant.setOrderItemsInfo(orderItems);
        sendOrderToRestaurant.setIncludeCustomerInfo(customerInfo);
        sendOrderToRestaurant.setOrderTotal(order.getTotalAmount());
        return sendOrderToRestaurant;
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
