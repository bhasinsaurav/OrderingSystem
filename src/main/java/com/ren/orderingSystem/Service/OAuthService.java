package com.ren.orderingSystem.Service;

import com.ren.orderingSystem.ApiContracts.ResponseDto.RestaurantLoginResponse;
import com.ren.orderingSystem.Entity.User;
import com.ren.orderingSystem.Mappers.UserMapper;
import com.ren.orderingSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class OAuthService {

    private final UserService userService;
    private final RestTemplate restTemplate;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final JWTService jwtService;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Autowired
    private OAuthService(JWTService jwtService, UserService userService, RestTemplate restTemplate, UserDetailsServiceImpl userDetailsService, UserMapper userMapper, UserRepository userRepository){
        this.userService= userService;
        this.restTemplate = restTemplate;
        this.userDetailsService = userDetailsService;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public RestaurantLoginResponse GoogleOauthHandler(String code){
        RestaurantLoginResponse restaurantLoginResponse = new RestaurantLoginResponse();
        String tokenEndpoint = "https://oauth2.googleapis.com/token";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", "https://developers.google.com/oauthplayground");
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request =new HttpEntity<>(params, headers);
        ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(tokenEndpoint, request, Map.class);
        String idToken = (String) tokenResponse.getBody().get("id_token");

        String userInfoUrl = "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken;

        ResponseEntity<Map> userInfoResponse =restTemplate.getForEntity(userInfoUrl, Map.class);
        if(userInfoResponse.getStatusCode() == HttpStatus.OK){
            Map<String, Object> userInfo = userInfoResponse.getBody();
            String email = (String) userInfo.get("email");
            User user = new User();
            try{
                userDetailsService.loadUserByUsername(email);
                user = userRepository.findByUserName(email);
            } catch(Exception e){
                user = userMapper.oAuthToUser(user, userInfo);
                userRepository.save(user);
            }


            String jwt = jwtService.generateToken(email);
            Map<String,String> responseMap = new HashMap<>();
            responseMap.put("jwt", jwt);
            String userId = user.getUserId().toString();
            responseMap.put("user_id", userId);
            if(userService.isRestaurantAvailable(user)){
                responseMap.put("ResturantAvailable", "True");
            }
            else{
                responseMap.put("RestaurantAvaible", "False");
            }

            restaurantLoginResponse.setLoginResponse(responseMap);
            return restaurantLoginResponse;
        }
        return restaurantLoginResponse;
    }
}
