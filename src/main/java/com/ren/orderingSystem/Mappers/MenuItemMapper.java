package com.ren.orderingSystem.Mappers;

import com.ren.orderingSystem.ApiContracts.RequestDto.AddMenuItemRequest;
import com.ren.orderingSystem.ApiContracts.ResponseDto.AddMenuItemResponse;
import com.ren.orderingSystem.ApiContracts.ResponseDto.GetMenuItemResponse;
import com.ren.orderingSystem.Entity.MenuItem;
import org.springframework.stereotype.Component;

@Component
public class MenuItemMapper {

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
        dto.setDescription(menuItem.getDescription());
        dto.setItemName(menuItem.getItemName());
        dto.setPrice(menuItem.getPrice());
        dto.setCategory(menuItem.getCategory());
        dto.setItemName(menuItem.getItemName());
        return dto;

    }
}
