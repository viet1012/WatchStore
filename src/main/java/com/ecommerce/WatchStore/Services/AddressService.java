package com.ecommerce.WatchStore.Services;

import com.ecommerce.WatchStore.Repositories.AddressRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ecommerce.WatchStore.Entities.Address;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {


    @Autowired
    private AddressRepository addressRepository;


    public List<Address> getAll()
    {
        return addressRepository.findAll();
    }
    public long Total()
    {
        return addressRepository.count();
    }
    public Address addAddressForUser(Address address , long userId) {
        Address newAddress = new Address();
        newAddress.setUserId(userId);
        newAddress.setWard(address.getWard());
        newAddress.setDistrict(address.getDistrict());
        newAddress.setHouseNumber(address.getHouseNumber());
        newAddress.setNote(address.getNote());
        newAddress.setActive(address.isActive());

        return addressRepository.save(newAddress);
    }
    public Address updateAddress(Address updatedAddress) {
        Optional<Address> existingAddress = addressRepository.findById(updatedAddress.getId());
        if (existingAddress.isPresent()) {
            Address address = existingAddress.get();
            address.setWard(updatedAddress.getWard());
            address.setDistrict(updatedAddress.getDistrict());
            address.setHouseNumber(updatedAddress.getHouseNumber());
            address.setNote(updatedAddress.getNote());
            address.setActive(updatedAddress.isActive());
            return addressRepository.save(address);
        } else {
            // Handle the case when the address with the given ID does not exist
            return null;
        }
    }

    public void deleteAddressById(long addressId) {
        addressRepository.deleteById(addressId);
    }

    public void deleteAllAddresses() {
        addressRepository.deleteAll();
    }


}
