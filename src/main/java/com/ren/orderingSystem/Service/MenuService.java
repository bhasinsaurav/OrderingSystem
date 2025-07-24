package com.ren.orderingSystem.Service;


import com.ren.orderingSystem.ApiContracts.RequestDto.AddMenuItemRequest;
import com.ren.orderingSystem.ApiContracts.RequestDto.UpdateMenuItemRequest;
import com.ren.orderingSystem.ApiContracts.ResponseDto.AddMenuItemResponse;
import com.ren.orderingSystem.ApiContracts.ResponseDto.GetMenuItemResponse;
import com.ren.orderingSystem.Entity.MenuItem;
import com.ren.orderingSystem.Entity.Restaurant;
import com.ren.orderingSystem.Exceptions.RestaurantNotFoundException;
import com.ren.orderingSystem.Mappers.MenuItemMapper;
import com.ren.orderingSystem.repository.MenuItemRepository;
import com.ren.orderingSystem.repository.RestaurantRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
    private final MenuItemRepository menuItemRepository;
    private final SimpMessagingTemplate messagingTemplate;

    // Constructor for dependency injection
    public MenuService(RestaurantRepository restaurantRepository,
                       MenuItemMapper menuItemMapper, MenuItemRepository menuItemRepository,
                        SimpMessagingTemplate messagingTemplate) {
        this.restaurantRepository = restaurantRepository;
        this.menuItemMapper = menuItemMapper;
        this.menuItemRepository = menuItemRepository;
        this.messagingTemplate = messagingTemplate;
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
        MenuItem byItemName = menuItemRepository.findByItemName(addedItemEntity.getItemName());
        AddMenuItemResponse menuItemResponse = menuItemMapper.toMenuItemResponse(byItemName);
        return menuItemResponse;
    }

    public GetMenuItemResponse updateMenuItem(UpdateMenuItemRequest updateMenuItemRequest, UUID restaurantUserId){
        MenuItem updatedMenuItem = menuItemMapper.updateToMenuItemEntity(updateMenuItemRequest);

        menuItemRepository.save(updatedMenuItem);
        GetMenuItemResponse menuItemToCustomer = menuItemMapper.toGetMenuResponseDto(updatedMenuItem);

        messagingTemplate.convertAndSend(
                "/topic/menuUpdate/" + restaurantUserId,
                menuItemToCustomer
        );

        return menuItemToCustomer;
    }
}
