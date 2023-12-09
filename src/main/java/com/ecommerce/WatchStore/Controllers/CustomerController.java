package com.ecommerce.WatchStore.Controllers;

import com.ecommerce.WatchStore.Common.BrandNotFoundException;
import com.ecommerce.WatchStore.Common.ProductNotFoundException;
import com.ecommerce.WatchStore.DTO.CustomerDTO;
import com.ecommerce.WatchStore.DTO.UserDTO;
import com.ecommerce.WatchStore.Entities.Customer;
import com.ecommerce.WatchStore.Entities.Product;
import com.ecommerce.WatchStore.Entities.User;
import com.ecommerce.WatchStore.Response.ResponseWrapper;
import com.ecommerce.WatchStore.Services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/Customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @GetMapping("/GetAll")
    public ResponseEntity<ResponseWrapper<List<Customer>>> getAll() {
        long totalCustomers = customerService.getTotalCustomers();
        List<Customer> customers = customerService.getAllCustomers();
        ResponseWrapper<List<Customer>> response = new ResponseWrapper<>(HttpStatus.OK.value(), "Success", true, totalCustomers, customers);
        return ResponseEntity.ok(response);
    }
    @PutMapping("/Update")
    public ResponseEntity<ResponseWrapper<Customer>> updateUser(@RequestBody CustomerDTO customerDTO) {
        Customer updatedUser = customerService.updateCustomer(customerDTO);
        ResponseWrapper<Customer> response = new ResponseWrapper<>(HttpStatus.ACCEPTED.value(), "Customer updated successfully", true, updatedUser);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

}
