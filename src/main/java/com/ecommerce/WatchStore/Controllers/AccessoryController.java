package com.ecommerce.WatchStore.Controllers;


import com.ecommerce.WatchStore.Entities.Accessory;
import com.ecommerce.WatchStore.Services.AccessoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Admin/Accessories")
public class AccessoryController {

    @Autowired
    public AccessoryService accessoryService;

    @GetMapping("/GetAll")
    public List<Accessory> getAllAccessories() {
        return accessoryService.getAllAccessories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Accessory> getAccessoryById(@PathVariable Long id) {
        Accessory accessory = accessoryService.getAccessoryById(id);
        if (accessory == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(accessory, HttpStatus.OK);
    }

    @PostMapping("/Create")
    public ResponseEntity<Accessory> createAccessory(@RequestBody Accessory accessory) {
        Accessory createdAccessory = accessoryService.createAccessory(accessory);
        return new ResponseEntity<>(createdAccessory, HttpStatus.CREATED);
    }

    @PutMapping("/Update/{id}")
    public ResponseEntity<Accessory> updateAccessory(@PathVariable Long id, @RequestBody Accessory accessory) {
        Accessory updatedAccessory = accessoryService.updateAccessory(id, accessory);
        if (updatedAccessory == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedAccessory, HttpStatus.OK);
    }

    @DeleteMapping("/Delete/{id}")
    public ResponseEntity<Void> deleteAccessory(@PathVariable Long id) {
        accessoryService.deleteAccessory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
