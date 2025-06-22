package com.ren.orderingSystem.Service;

import com.ren.orderingSystem.ApiContracts.RequestDto.RestaurantLoginRequest;
import com.ren.orderingSystem.ApiContracts.RequestDto.RegisterRestaurantRequest;
import com.ren.orderingSystem.ApiContracts.ResponseDto.RestaurantLoginResponse;
import com.ren.orderingSystem.Entity.User;
import com.ren.orderingSystem.Mappers.RestaurantMapper;
import com.ren.orderingSystem.Mappers.UserMapper;
import com.ren.orderingSystem.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RestaurantMapper restaurantMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public UserService(UserRepository userRepository, UserMapper userMapper, RestaurantMapper restaurantMapper, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JWTService jwtService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.restaurantMapper = restaurantMapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Transactional
    public User registerRestaurant(RegisterRestaurantRequest registerAdminRequest){

        User user = new User();
        user.setUserTimestamp(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode((registerAdminRequest.getPassword())));
        User userEntity = userMapper.toUserEntity(registerAdminRequest, user);
        userRepository.save(userEntity);
        User savedUser = userRepository.findByUserName(user.getUserName());
        return savedUser;

    }


    public  boolean isRestaurantAvailable(User user){
        return user.getRestaurant()!= null;
    }
    public RestaurantLoginResponse verify(RestaurantLoginRequest restaurantLoginRequest) {
        RestaurantLoginResponse loginResponse = new RestaurantLoginResponse();
        Map<String, String> responseMap = new HashMap<>();
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(restaurantLoginRequest.getUserName(), restaurantLoginRequest.getPassword()));
        User user = userRepository.findByUserName(authenticate.getName());
        String jwt = jwtService.generateToken(restaurantLoginRequest.getUserName());
        responseMap.put("jwt", jwt);

        if(isRestaurantAvailable(user)){
            responseMap.put("ResturantAvailable", "True");
        }
        else {
            responseMap.put("ResturantAvailable", "False");

        }
        String userId = user.getUserId().toString();
        responseMap.put("user_id", userId);
        loginResponse.setLoginResponse(responseMap);
        return loginResponse;

    }
}

