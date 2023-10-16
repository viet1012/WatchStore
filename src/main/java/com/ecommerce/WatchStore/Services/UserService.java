package com.ecommerce.WatchStore.Services;

import com.ecommerce.WatchStore.DTO.UserDTO;
import com.ecommerce.WatchStore.Entities.User;
import com.ecommerce.WatchStore.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getListUser(){
        return userRepository.findAll();
    }
    public User getUserById(Long userId){
        Optional<User> optionalUser =  userRepository.findById(userId);
        return optionalUser.orElse(null);
    }
    public User createUser(UserDTO userDTO) {

        User user = new User();

        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setDisplayName(userDTO.getDisplayName());
        user.setCreatedBy(userDTO.getCreatedBy());
        user.setCreatedDate(userDTO.getCreatedDate());
        user.setActive(true);
        return userRepository.save(user);
    }
}
