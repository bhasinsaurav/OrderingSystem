package com.ren.orderingSystem.Controller.Restaurant;

import com.ren.orderingSystem.ApiContracts.RequestDto.AddMenuItemRequest;
import com.ren.orderingSystem.ApiContracts.RequestDto.UpdateMenuItemRequest;
import com.ren.orderingSystem.ApiContracts.ResponseDto.AddMenuItemResponse;
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
@RequestMapping("/restaurant")

public class RestaurantMenuController {

    private final MenuService menuService;
    private final S3Service s3Service;

    public RestaurantMenuController(MenuService menuService, S3Service s3Service) {

        this.menuService= menuService;
        this.s3Service = s3Service;
    }

    @PostMapping("/addMenu/{userId}")
    public ResponseEntity<AddMenuItemResponse> addMenu(@RequestBody AddMenuItemRequest addMenuItemDto, @PathVariable UUID userId){
        AddMenuItemResponse addMenuItemResponse = menuService.addMenuItem(addMenuItemDto, userId);
        return new ResponseEntity<>(addMenuItemResponse, HttpStatus.CREATED);

    }

    @GetMapping("/uploadMenuImage/{userId}")
    public ResponseEntity<?> uploadMenuImage(@PathVariable UUID userId, @RequestParam Long menuItemId){

        S3UrlResponse presignedPutUrl = s3Service.generatePutPresignedUrl(menuItemId.toString(), userId.toString());
        return new ResponseEntity<>(presignedPutUrl, HttpStatus.OK);
    }

    @GetMapping("/getMenuImage/{userId}")
    public ResponseEntity<?> getMenuImage(@PathVariable UUID userId, @RequestParam Long menuItemId){

        S3UrlResponse presignedGetUrl = s3Service.generateGetPresignedUrl(menuItemId.toString(), userId.toString());
        return new ResponseEntity<>(presignedGetUrl, HttpStatus.OK);
    }

    @GetMapping("/getMenu/{userId}")
    public ResponseEntity<?> getMenu(@PathVariable UUID userId){

        List<GetMenuItemResponse> menuItemsDtos = menuService.showMenuItemsToUser(userId);
        return new ResponseEntity<>(menuItemsDtos,HttpStatus.OK);
    }

    @PutMapping("/update-menuitem/{userId}")
    public ResponseEntity<?>  updateMenu(@PathVariable UUID userId, @RequestBody UpdateMenuItemRequest updateMenuItemRequest){

        GetMenuItemResponse updatedMenuItemResponse = menuService.updateMenuItem(updateMenuItemRequest, userId);
        return new ResponseEntity<>(updatedMenuItemResponse, HttpStatus.OK);

    }
}
