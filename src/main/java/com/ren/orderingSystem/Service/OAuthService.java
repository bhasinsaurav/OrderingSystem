package com.ren.orderingSystem.Service;

import com.ren.orderingSystem.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class OAuthService {

    private final RestTemplate restTemplate;
    private final UserDetailsServiceImpl userDetailsService;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Autowired
    public OAuthService(RestTemplate restTemplate, UserDetailsServiceImpl userDetailsService){
        this.restTemplate = restTemplate;
        this.userDetailsService = userDetailsService;
    }

    public void handleGoogleCallback(String code) {

        String tokenEndpoint = "https://oauth2.googleapis.com/token";

        Map<String, String> params = new HashMap<>();
        params.put("code", code);
        params.put("client_id", clientId);
        params.put("client_secret", clientSecret);
        params.put("redirect_url", "https://developers.google.com/oauthplayground");
        params.put("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(tokenEndpoint, request, Map.class);
        String idToken = (String) tokenResponse.getBody().get("id_token");
        String userInfoUrl = "https://oauth2.googleapis.com/tokeninfo?id_token=" +idToken;

        ResponseEntity<Map> userInfoResponse = restTemplate.getForEntity(userInfoUrl, Map.class);

        if(userInfoResponse.getStatusCode() ==HttpStatus.OK) {
            Map<String, Object> userInfo = userInfoResponse.getBody();
            String email = (String) userInfo.get("email");
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            if(userDetails == null){

            }

        }
    }
}

