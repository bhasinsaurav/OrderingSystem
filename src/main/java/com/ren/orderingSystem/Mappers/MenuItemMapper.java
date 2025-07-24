package com.ren.orderingSystem.Mappers;

import com.ren.orderingSystem.ApiContracts.RequestDto.AddMenuItemRequest;
import com.ren.orderingSystem.ApiContracts.RequestDto.UpdateMenuItemRequest;
import com.ren.orderingSystem.ApiContracts.ResponseDto.AddMenuItemResponse;
import com.ren.orderingSystem.ApiContracts.ResponseDto.GetMenuItemResponse;
import com.ren.orderingSystem.Entity.MenuItem;
import com.ren.orderingSystem.repository.MenuItemRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuItemMapper {

    private final MenuItemRepository menuItemRepository;

    public MenuItemMapper(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public GetMenuItemResponse toGetMenuResponseDto(MenuItem menuItem) {
            GetMenuItemResponse dto = new GetMenuItemResponse();
            dto.setItemId(menuItem.getItemId());
            dto.setDescription(menuItem.getDescription());
            dto.setPrice(menuItem.getPrice());
            dto.setCategory(menuItem.getCategory());
            dto.setItemName(menuItem.getItemName());
            dto.setIsAvailable(menuItem.getIsAvailable());
            return dto;
    }

    public MenuItem toMenuItemEntity(AddMenuItemRequest menuItemRequest, MenuItem menuItem){
        menuItem.setDescription(menuItemRequest.getDescription());
        menuItem.setPrice(menuItemRequest.getPrice());
        menuItem.setCategory(menuItemRequest.getCategory());
        menuItem.setItemName(menuItemRequest.getItemName());
        menuItem.setIsAvailable(menuItemRequest.getIsAvailable());
        return menuItem;
    }

    public AddMenuItemResponse toMenuItemResponse(MenuItem menuItem){
        AddMenuItemResponse dto = new AddMenuItemResponse();
        dto.setMenuItemId(menuItem.getItemId());
        dto.setDescription(menuItem.getDescription());
        dto.setItemName(menuItem.getItemName());
        dto.setIsAvailable(menuItem.getIsAvailable());
        dto.setPrice(menuItem.getPrice());
        dto.setCategory(menuItem.getCategory());
        dto.setItemName(menuItem.getItemName());
        return dto;

    }

    public MenuItem updateToMenuItemEntity(UpdateMenuItemRequest updateMenuItemRequest){

        MenuItem menuItem = menuItemRepository.getMenuItemByItemId(updateMenuItemRequest.getMenuItemId());
        if (updateMenuItemRequest.getIsAvailable()!= null) {
            menuItem.setIsAvailable(updateMenuItemRequest.getIsAvailable());
        }
        if (updateMenuItemRequest.getDescription() != null) {
            menuItem.setDescription(updateMenuItemRequest.getDescription());
        }
        if (updateMenuItemRequest.getPrice() != null) {
            menuItem.setPrice(updateMenuItemRequest.getPrice());
        }
        if (updateMenuItemRequest.getCategory() != null) {
            menuItem.setCategory(updateMenuItemRequest.getCategory());
        }
        if (updateMenuItemRequest.getItemName() != null) {
            menuItem.setItemName(updateMenuItemRequest.getItemName());
        }
        return menuItem;

    }

}
