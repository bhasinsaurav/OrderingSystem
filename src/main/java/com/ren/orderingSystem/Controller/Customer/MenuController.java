package com.ren.orderingSystem.Controller.Customer;

import com.ren.orderingSystem.ApiContracts.ResponseDto.GetCustomerMenuItemResponse;
import com.ren.orderingSystem.Service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/customer")
public class MenuController {

    @Autowired
    private MenuService MenuService;

    @GetMapping("/getMenu/{userId}")
    private ResponseEntity<?> getMenu(@PathVariable UUID userId){

        List<GetCustomerMenuItemResponse> menuItemsDtos = MenuService.showAllMenuItemsToCustomer(userId);
        return new ResponseEntity<>(menuItemsDtos,HttpStatus.OK);
    }
}
