package com.ren.orderingSystem.Controller;

import com.ren.orderingSystem.ApiContracts.RequestDto.RestaurantLoginRequest;
import com.ren.orderingSystem.ApiContracts.RequestDto.RegisterRestaurantRequest;
import com.ren.orderingSystem.ApiContracts.ResponseDto.RestaurantLoginResponse;
import com.ren.orderingSystem.Service.OAuthService;
import com.ren.orderingSystem.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user/public")
public class UserController {

   private final UserService userService;
   private final OAuthService oAuthService;

   @Autowired
   public UserController(UserService userService, OAuthService oAuthService){
       this.userService = userService;
       this.oAuthService = oAuthService;
   }

    @PostMapping("/register-restaurant")
    public ResponseEntity<String> registerAdmin(@Valid @RequestBody RegisterRestaurantRequest registerAdminRequest) {
       userService.registerRestaurant(registerAdminRequest);
        return new ResponseEntity<>("Restaurant has been registered", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<RestaurantLoginResponse> loginAdmin(@Valid @RequestBody RestaurantLoginRequest restaurantLoginRequest){
        RestaurantLoginResponse verify = userService.verify(restaurantLoginRequest);
        return new ResponseEntity<>(verify, HttpStatus.OK);

    }

    @GetMapping("Oauth/google")
    public ResponseEntity<RestaurantLoginResponse> googleOauthHandler(@RequestParam String code){
        RestaurantLoginResponse restaurantLoginResponse = oAuthService.GoogleOauthHandler(code);
        return new ResponseEntity<>(restaurantLoginResponse, HttpStatus.OK);
    }
}
