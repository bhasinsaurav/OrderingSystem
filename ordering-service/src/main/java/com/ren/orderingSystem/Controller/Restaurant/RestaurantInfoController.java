package com.ren.orderingSystem.Controller.Restaurant;

import com.ren.orderingSystem.ApiContracts.RequestDto.AddRestaurantDetailsRequest;
import com.ren.orderingSystem.ApiContracts.RequestDto.UpdateRestaurantInfoRequestDto;
import com.ren.orderingSystem.ApiContracts.ResponseDto.RestaurantDetailsInfoResponse;
import com.ren.orderingSystem.Service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("restaurant/info/")

public class RestaurantInfoController {


    private final RestaurantService restaurantService;

    @Autowired
    public RestaurantInfoController(RestaurantService restaurantService){
        this.restaurantService=restaurantService;
    }
    @PostMapping("restaurant-detail-entry/{userId}")
    public ResponseEntity<?>  addRestaurantDetail(@PathVariable UUID userId, @RequestBody AddRestaurantDetailsRequest addRestaurantDetailsRequest){
        RestaurantDetailsInfoResponse restaurantDetailsInfoResponse = restaurantService.addRestaurantDetails(addRestaurantDetailsRequest, userId);
        return new ResponseEntity<>(restaurantDetailsInfoResponse,HttpStatus.OK);
    }

    @PutMapping("restaurant-detail-update/{userId}")
    public ResponseEntity<?>  updateRestaurantDetail(@PathVariable UUID userId, @RequestBody UpdateRestaurantInfoRequestDto updateRestaurantInfoRequestDto){
         restaurantService.updateRestaurantDetails(updateRestaurantInfoRequestDto, userId);
         return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("restaurant-detail/{userId}")
    public ResponseEntity<?> getRestaurantDetail(@PathVariable UUID userId){
        RestaurantDetailsInfoResponse restaurantDetail = restaurantService.getRestaurantDetail(userId);
        return new ResponseEntity<>(restaurantDetail, HttpStatus.OK);

    }
}
