package com.ecommerce.WatchStore.Services;

import com.ecommerce.WatchStore.Config.JwtTokenProvider;
import com.ecommerce.WatchStore.DTO.AddressDTO;
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
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    public List<Address> getAll()
    {
        return addressRepository.findAll();
    }
    public long Total()
    {
        return addressRepository.count();
    }
    public Address addAddressForUser(AddressDTO addressDTO) {
        Address newAddress = new Address();
//        Long userId = jwtTokenProvider.getUserIdFromGeneratedToken(addressDTO.getToken());
        newAddress.setUserId(addressDTO.getUserId());
        newAddress.setWard(addressDTO.getWard());
        newAddress.setDistrict(addressDTO.getDistrict());
        newAddress.setHouseNumber(addressDTO.getHouseNumber());
        newAddress.setNote(addressDTO.getNote());
        newAddress.setActive(true);

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
