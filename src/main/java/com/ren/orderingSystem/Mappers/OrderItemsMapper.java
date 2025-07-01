package com.ren.orderingSystem.Mappers;

import com.ren.orderingSystem.ApiContracts.RequestDto.OrderedItemsRequest;
import com.ren.orderingSystem.Entity.MenuItem;
import com.ren.orderingSystem.Entity.Order;
import com.ren.orderingSystem.Entity.OrderItems;
import com.ren.orderingSystem.repository.MenuItemRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderItemsMapper {

    private final MenuItemRepository menuItemRepository;

    public OrderItemsMapper(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public OrderItems toOrderItemsEntity(OrderedItemsRequest dto) {

        OrderItems orderItems = new OrderItems();
        MenuItem menuItem = menuItemRepository.findById(dto.getMenuItemId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid menu item ID: " + dto.getMenuItemId()));
        List<OrderItems> orderItemsList = menuItem.getOrderItems();

        orderItems.setMenuItem(menuItem);
        orderItems.setQuantity(dto.getQuantity());
        orderItemsList.add(orderItems);
        menuItem.setOrderItems(orderItemsList);

        return orderItems;
    }
}
