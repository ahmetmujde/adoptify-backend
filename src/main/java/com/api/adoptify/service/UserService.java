package com.api.adoptify.service;

import com.api.adoptify.dto.UserDto;
import com.api.adoptify.entity.User;
import com.api.adoptify.mapper.UserMapper;
import com.api.adoptify.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.mapToUser(userDto);
        User savedUser = userRepository.save(user);

        return UserMapper.mapToUserDto(savedUser);
    }
}
