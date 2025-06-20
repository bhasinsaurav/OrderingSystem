package com.ren.orderingSystem.Controller;

import com.ren.orderingSystem.Service.OAuthService;
import com.ren.orderingSystem.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/google")
public class GoogleAuthController {

    private final OAuthService oAuthService;
    public GoogleAuthController(OAuthService oAuthService){
        this.oAuthService = oAuthService;
    }
    @GetMapping("/callback")
    public ResponseEntity<?> handleGoogleCallback(@RequestParam String code){
        oAuthService.handleGoogleCallback(code);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
