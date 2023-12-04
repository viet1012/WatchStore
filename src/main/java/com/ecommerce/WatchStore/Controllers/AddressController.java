package com.ecommerce.WatchStore.Controllers;

import com.ecommerce.WatchStore.DTO.AddressDTO;
import com.ecommerce.WatchStore.DTO.ProductDTO;
import com.ecommerce.WatchStore.Entities.Address;
import com.ecommerce.WatchStore.Entities.Product;
import com.ecommerce.WatchStore.Entities.User;
import com.ecommerce.WatchStore.Response.ResponseWrapper;
import com.ecommerce.WatchStore.Services.AddressService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping("/GetAll")
    public ResponseEntity<ResponseWrapper<List<Address>>> getAll() {
        List<Address> addresses = addressService.getAll();
        long totalAddresses = addressService.Total();
        ResponseWrapper<List<Address>> response = new ResponseWrapper<>(HttpStatus.OK.value(), "Success", true, totalAddresses, addresses);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/Create")
    public ResponseEntity<ResponseWrapper<Address>> addAddressForUser(@RequestBody AddressDTO address) {
        try {
          //  Long userId = (Long) request.getAttribute("userId");

//            System.out.println("ID User khi POST: " + userId);
            Address newAddress = addressService.addAddressForUser(address);
            long totalAddresses = addressService.Total();

            ResponseWrapper<Address> response = new ResponseWrapper<>(HttpStatus.CREATED.value(), "Address created successfully", true, totalAddresses, newAddress);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @PutMapping("/Update")
    public ResponseEntity<Address> updateAddress(@RequestBody Address address) {
        Address updatedAddress = addressService.updateAddress(address);
        if (updatedAddress != null) {
            return ResponseEntity.ok(updatedAddress);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/Delete/{addressId}")
    public ResponseEntity<Void> deleteAddressById(@PathVariable long addressId) {
        addressService.deleteAddressById(addressId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<Void> deleteAllAddresses() {
        addressService.deleteAllAddresses();
        return ResponseEntity.noContent().build();
    }


}
