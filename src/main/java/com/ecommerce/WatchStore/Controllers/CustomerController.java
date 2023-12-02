package com.ecommerce.WatchStore.Controllers;

import com.ecommerce.WatchStore.DTO.CustomerDTO;
import com.ecommerce.WatchStore.Entities.Customer;
import com.ecommerce.WatchStore.Entities.User;
import com.ecommerce.WatchStore.Response.ResponseWrapper;
import com.ecommerce.WatchStore.Services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


}
