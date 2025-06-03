package com.ren.orderingSystem.Service;


import com.ren.orderingSystem.ApiContracts.RequestDto.AddMenuItemRequest;
import com.ren.orderingSystem.ApiContracts.ResponseDto.AddMenuItemResponse;
import com.ren.orderingSystem.ApiContracts.ResponseDto.GetCustomerMenuItemResponse;
import com.ren.orderingSystem.Entity.MenuItem;
import com.ren.orderingSystem.Entity.Restaurant;
import com.ren.orderingSystem.Mappers.MenuItemMapper;
import com.ren.orderingSystem.repository.RestaurantRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class MenuService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuItemMapper menuItemMapper;

    public List<GetCustomerMenuItemResponse> showAllMenuItemsToCustomer(UUID userId){
        Restaurant restaurant = restaurantRepository.findByUser_UserId(userId).orElseThrow(() -> new EntityNotFoundException("Restaurant not found for given id"));
        return restaurant.getMenuItems().stream().map(menuItemMapper::toCustomerResponseDto).toList();


    }

    @Transactional
    public AddMenuItemResponse addMenuItem(AddMenuItemRequest addedMenuItem, UUID userId){
        Restaurant restaurant = restaurantRepository.findByUser_UserId((userId)).orElseThrow(() -> new EntityNotFoundException("No restauarnt associated with given userId"));
        MenuItem menuItem = new MenuItem();
        menuItem.setCreatedAt(LocalDateTime.now());
        menuItem.setUpdatedAt(LocalDateTime.now());
        MenuItem addedItemEntity = menuItemMapper.toMenuItemEntity(addedMenuItem, menuItem);
        restaurant.getMenuItems().add(addedItemEntity);
        restaurantRepository.save(restaurant);
        AddMenuItemResponse menuItemResponse = menuItemMapper.toMenuItemResponse(addedItemEntity);
        return menuItemResponse;
    }
}
