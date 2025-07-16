package com.ren.orderingSystem.Service;


import com.ren.orderingSystem.ApiContracts.RequestDto.AddMenuItemRequest;
import com.ren.orderingSystem.ApiContracts.ResponseDto.AddMenuItemResponse;
import com.ren.orderingSystem.ApiContracts.ResponseDto.GetMenuItemResponse;
import com.ren.orderingSystem.Entity.MenuItem;
import com.ren.orderingSystem.Entity.Restaurant;
import com.ren.orderingSystem.Exceptions.RestaurantNotFoundException;
import com.ren.orderingSystem.Mappers.MenuItemMapper;
import com.ren.orderingSystem.repository.RestaurantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class MenuService {

    private final RestaurantRepository restaurantRepository;
    private final MenuItemMapper menuItemMapper;

    // Constructor for dependency injection
    public MenuService(RestaurantRepository restaurantRepository,
                       MenuItemMapper menuItemMapper) {
        this.restaurantRepository = restaurantRepository;
        this.menuItemMapper = menuItemMapper;
    }

    public List<GetMenuItemResponse> showMenuItemsToUser(UUID userId){
        Restaurant restaurant = restaurantRepository.findByUser_UserId(userId).orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found for given id"));
        Set<MenuItem> menuItemSet = restaurant.getMenuItems();
        return menuItemSet.stream().map(menuItemMapper::toGetMenuResponseDto).toList();


    }

    @Transactional
    public AddMenuItemResponse addMenuItem(AddMenuItemRequest addedMenuItem, UUID userId){
        Restaurant restaurant = restaurantRepository.findByUser_UserId((userId)).orElseThrow(() -> new RestaurantNotFoundException("No restauarnt associated with given userId"));
        MenuItem menuItem = new MenuItem();
        menuItem.setCreatedAt(LocalDateTime.now());
        menuItem.setUpdatedAt(LocalDateTime.now());
        MenuItem addedItemEntity = menuItemMapper.toMenuItemEntity(addedMenuItem, menuItem);
        addedItemEntity.setRestaurant(restaurant);
        restaurant.getMenuItems().add(addedItemEntity);
        restaurantRepository.save(restaurant);
        AddMenuItemResponse menuItemResponse = menuItemMapper.toMenuItemResponse(addedItemEntity);
        return menuItemResponse;
    }
}
