package com.ren.orderingSystem.Controller;

import com.ren.orderingSystem.ApiContracts.RequestDto.AdminLoginRequest;
import com.ren.orderingSystem.ApiContracts.RequestDto.RegisterRestaurantRequest;
import com.ren.orderingSystem.ApiContracts.ResponseDto.RegisterAdminResponse;
import com.ren.orderingSystem.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/public")
public class UserController {

   private final UserService userService;
   public UserController(UserService userService){
       this.userService = userService;
   }

    @PostMapping("/register-restaurant")
    public ResponseEntity<?> registerAdmin(@RequestBody RegisterRestaurantRequest registerAdminRequest) throws Exception {
       userService.registerRestaurant(registerAdminRequest);
        return new ResponseEntity<>("Restaurant has been registered", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginAdmin(@RequestBody AdminLoginRequest adminLoginRequest){
       try {
           String verify = userService.verify(adminLoginRequest);
           return new ResponseEntity<>(verify, HttpStatus.OK);
       }
       catch(AuthenticationException ex){
           return new ResponseEntity<>("Login Failed", HttpStatus.UNAUTHORIZED);
       }
    }
}
