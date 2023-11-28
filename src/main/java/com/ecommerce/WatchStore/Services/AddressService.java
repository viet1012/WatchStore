package com.ecommerce.WatchStore.Services;

import com.ecommerce.WatchStore.Repositories.AddressRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ecommerce.WatchStore.Entities.Address;
@Service
public class AddressService {


    @Autowired
    private AddressRepository addressRepository;

    public Address addAddressForUser(Address address) {
        Address newAddress = new Address();
        newAddress.setUserId(address.getUserId());
        newAddress.setWard(address.getWard());
        newAddress.setDistrict(address.getDistrict());
        newAddress.setHouseNumber(address.getHouseNumber());
        newAddress.setNote(address.getNote());
        newAddress.setActive(address.isActive());

        return addressRepository.save(address);
    }
}
