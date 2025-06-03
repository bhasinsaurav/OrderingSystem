package com.ren.orderingSystem.Service;

import com.ren.orderingSystem.ApiContracts.RequestDto.RegisterAdminRequest;
import com.ren.orderingSystem.ApiContracts.ResponseDto.RegisterAdminResponse;
import com.ren.orderingSystem.Entity.User;
import com.ren.orderingSystem.Mappers.UserMapper;
import com.ren.orderingSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper){
        this.userRepository = userRepository;
        this.userMapper= userMapper;
    }

    private RegisterAdminResponse registerRestaurant(RegisterAdminRequest registerAdminRequest){
        User user = new User();
        user.setUserTimestamp(LocalDateTime.now());
        userMapper.toUserEntity(registerAdminRequest, user);

    }
}
