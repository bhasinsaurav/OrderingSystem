package com.ren.orderingSystem.Controller;

import com.ren.orderingSystem.ApiContracts.RequestDto.RegisterAdminRequest;
import com.ren.orderingSystem.Entity.User;
import com.ren.orderingSystem.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

   private final UserService userService;
   public UserController(UserService userService){
       this.userService = userService;
   }

    @PostMapping("/register-admin")
    public ResponseEntity<?> registerAdmin(@RequestBody RegisterAdminRequest registerAdminRequest){

    }
}
