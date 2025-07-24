package com.ren.orderingSystem.Controller.Customer;

import com.ren.orderingSystem.ApiContracts.ResponseDto.GetMenuItemResponse;
import com.ren.orderingSystem.ApiContracts.ResponseDto.S3UrlResponse;
import com.ren.orderingSystem.Service.MenuService;
import com.ren.orderingSystem.Service.S3Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/customer")

public class CustomerMenuController {

    private final MenuService menuService;
    private final S3Service s3Service;

    public CustomerMenuController(MenuService menuService, S3Service s3Service) {

        this.menuService= menuService;
        this.s3Service= s3Service;
    }

    @GetMapping("/getMenu/{userId}")
    public ResponseEntity<?> getMenu(@PathVariable UUID userId){

        List<GetMenuItemResponse> menuItemsDtos = menuService.showMenuItemsToUser(userId);
        return new ResponseEntity<>(menuItemsDtos,HttpStatus.OK);
    }

    @GetMapping("/getMenuImage/{userId}")
    public ResponseEntity<?> getMenuImage(@PathVariable UUID userId, @RequestParam Long menuItemId){

        S3UrlResponse presignedGetUrl = s3Service.generateGetPresignedUrl(menuItemId.toString(), userId.toString());
        return new ResponseEntity<>(presignedGetUrl, HttpStatus.OK);
    }
}
