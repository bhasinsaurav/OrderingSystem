package com.ren.orderingSystem.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @PostMapping("/register-admin")
    private ResponseEntity<?> registerAdmin(@RequestBody RegisterAdminRequest registerAdminRequest){

    }
}
