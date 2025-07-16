package com.ren.orderingSystem.Controller.Customer;

import com.ren.orderingSystem.ApiContracts.ResponseDto.GetMenuItemResponse;
import com.ren.orderingSystem.Service.MenuService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/customer")

public class CustomerMenuController {

    private final MenuService menuService;

    public CustomerMenuController(MenuService menuService) {

        this.menuService= menuService;
    }

    @GetMapping("/getMenu/{userId}")
    public ResponseEntity<?> getMenu(@PathVariable UUID userId){

        List<GetMenuItemResponse> menuItemsDtos = menuService.showMenuItemsToUser(userId);
        return new ResponseEntity<>(menuItemsDtos,HttpStatus.OK);
    }
}
