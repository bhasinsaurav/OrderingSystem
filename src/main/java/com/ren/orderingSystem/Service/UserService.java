package com.ren.orderingSystem.Service;

import com.ren.orderingSystem.ApiContracts.RequestDto.AdminLoginRequest;
import com.ren.orderingSystem.ApiContracts.RequestDto.RegisterRestaurantRequest;
import com.ren.orderingSystem.Entity.Restaurant;
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

    public String verify(AdminLoginRequest adminLoginRequest) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(adminLoginRequest.getUserName(), adminLoginRequest.getPassword()));

        return authentication.isAuthenticated() ? jwtService.generateToken(adminLoginRequest.getUserName()) : "Failure";

    }
}

