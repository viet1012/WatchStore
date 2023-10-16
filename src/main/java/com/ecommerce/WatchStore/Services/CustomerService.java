package com.ecommerce.WatchStore.Services;

import com.ecommerce.WatchStore.Entities.User;
import com.ecommerce.WatchStore.Repositories.CustomerRepository;
import com.ecommerce.WatchStore.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.ecommerce.WatchStore.DTO.CustomerDTO;
import com.ecommerce.WatchStore.Entities.Customer;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private UserRepository userRepository;
    public Customer createCustomer(CustomerDTO customerDTO, Long userId) {
        Customer customer = new Customer();
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isPresent())
        {
            User user = optionalUser.get();
            customer.setUser(user);

            customer.setFullname(customerDTO.getFullname());
            customer.setCode(customerDTO.getCode());
            customer.setLastName(customerDTO.getLastName());
            customer.setFirstName(customerDTO.getFirstName());
            customer.setGender(customerDTO.getGender());
            customer.setDateOfBirth(customerDTO.getDateOfBirth());
            customer.setAddress(customerDTO.getAddress());
            customer.setEmail(customerDTO.getEmail());
            customer.setPhoneNumber(customerDTO.getPhoneNumber());
            customer.setCreatedBy(customerDTO.getCreatedBy());
            customer.setActive(customerDTO.getActive());
            return customerRepository.save(customer);
        }
        else{
            return  null;
        }

    }

}
