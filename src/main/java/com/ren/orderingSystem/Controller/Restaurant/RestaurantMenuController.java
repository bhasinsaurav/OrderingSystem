package com.ren.orderingSystem.Controller.Restaurant;

import com.ren.orderingSystem.ApiContracts.RequestDto.AddMenuItemRequest;
import com.ren.orderingSystem.ApiContracts.ResponseDto.AddMenuItemResponse;
import com.ren.orderingSystem.ApiContracts.ResponseDto.GetMenuItemResponse;
import com.ren.orderingSystem.Service.MenuService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/restaurant")

public class RestaurantMenuController {

    private final MenuService menuService;

    public RestaurantMenuController(MenuService menuService) {

        this.menuService= menuService;
    }

    @PostMapping("/addMenu/{userId}")
    public ResponseEntity<AddMenuItemResponse> addMenu(@RequestBody AddMenuItemRequest addMenuItemDto, @PathVariable UUID userId){
        AddMenuItemResponse addMenuItemResponse = menuService.addMenuItem(addMenuItemDto, userId);
        return new ResponseEntity<>(addMenuItemResponse, HttpStatus.CREATED);

    }

    @GetMapping("/getMenu/{userId}")
    public ResponseEntity<?> getMenu(@PathVariable UUID userId){

        List<GetMenuItemResponse> menuItemsDtos = menuService.showMenuItemsToUser(userId);
        return new ResponseEntity<>(menuItemsDtos,HttpStatus.OK);
    }

//    @PutMapping("/update-menuitem/{userId}")
//    public ResponseEntity<?>  updateMenu(@PathVariable UUID user, @RequestBody ){
//
//    }
}
