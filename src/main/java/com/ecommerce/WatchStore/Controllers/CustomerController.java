package com.ecommerce.WatchStore.Controllers;

import com.ecommerce.WatchStore.DTO.CustomerDTO;
import com.ecommerce.WatchStore.Entities.Customer;
import com.ecommerce.WatchStore.Services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/Customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @PostMapping("Create")
    public ResponseEntity<Customer> createCustomer(@RequestBody CustomerDTO customer, @RequestParam Long userId) {
        Customer createdCustomer = customerService.createCustomer(customer, userId);
        return ResponseEntity.ok(createdCustomer);
    }
}
