package com.ecommerce.WatchStore.Services;

import com.ecommerce.WatchStore.Entities.Accessory;
import com.ecommerce.WatchStore.Repositories.AccessoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccessoryService {

    @Autowired
    public AccessoryRepository accessoryRepository;
    public List<Accessory> getAllAccessories() {
        return accessoryRepository.findAll();
    }

    public Accessory getAccessoryById(Long id) {
        return accessoryRepository.findById(id).orElse(null);
    }

    public Accessory createAccessory(Accessory accessory) {
        return accessoryRepository.save(accessory);
    }

    public Accessory updateAccessory(Long id, Accessory accessory) {
        accessory.setId(id);
        return accessoryRepository.save(accessory);
    }

    public void deleteAccessory(Long id) {
        accessoryRepository.deleteById(id);
    }
}
