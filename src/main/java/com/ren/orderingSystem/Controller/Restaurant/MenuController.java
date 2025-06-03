package com.ren.orderingSystem.Controller.Restaurant;

import com.ren.orderingSystem.ApiContracts.RequestDto.AddMenuItemRequest;
import com.ren.orderingSystem.ApiContracts.ResponseDto.AddMenuItemResponse;
import com.ren.orderingSystem.Service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/restaurant")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @PostMapping("/addMenu/{userId}")
    private ResponseEntity<?> addMenu(@RequestBody AddMenuItemRequest addMenuItemDto, @PathVariable UUID userId){
        AddMenuItemResponse addMenuItemResponse = menuService.addMenuItem(addMenuItemDto, userId);
        return new ResponseEntity<>(addMenuItemResponse, HttpStatus.CREATED);

    }
}
