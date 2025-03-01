package vn.demo_shipping.shipping.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.demo_shipping.shipping.domain.Address;
import vn.demo_shipping.shipping.domain.User;
import vn.demo_shipping.shipping.dto.request.AddressRequest;
import vn.demo_shipping.shipping.exception.NotFoundException;
import vn.demo_shipping.shipping.exception.NullObjectException;
import vn.demo_shipping.shipping.repository.AddressRepository;
import vn.demo_shipping.shipping.repository.UserRepository;
import vn.demo_shipping.shipping.service.AddressService;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    @Override
    public Address addAddress(Address address) {
        return addressRepository.save(address);
    }

    @Override
    public String deleteAddress(Long id) {
        try {
            if (!addressRepository.existsById(id)) {
                throw new NotFoundException("Address with ID " + id + " does not exist.");
            }
            addressRepository.deleteById(id);
            return "Delete address success: " + id;
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred while deleting the address.");
        }
    }

    @Override
    public Address getAddressById(Long id) {
        try {
            if (!addressRepository.existsById(id))
                throw new NotFoundException("Address with ID " + id + " does not exists");
            return addressRepository.findById(id).orElseThrow(() -> new NullObjectException("Address is null"));
        } catch (NotFoundException e) {
            throw e;
        } catch (NullObjectException e) {
            throw e;
        }
    }

    @Override
    public Page<Address> getAllAddress(int page, int size, String request) {
        Sort sort;
        if (request.compareTo("asc") == 0) {
            sort = Sort.by(Sort.Order.asc("id"));
        } else if (request.compareTo("desc") == 0) {
            sort = Sort.by(Sort.Order.desc("id"));
        } else {
            sort = Sort.by(Sort.Order.asc("id"));
        }
        Pageable pageable = PageRequest.of(page, size, sort);
        return addressRepository.findAll(pageable);
    }

    @Override
    public List<Address> getAllAddress() {
        return addressRepository.findAll();
    }

    @Override
    public Address updateAddress(Long id, AddressRequest address) {
        try {
            User user = userRepository.findById(address.getUser_id()).orElseThrow(
                    () -> new NotFoundException("User does not exists in database"));

            Address currentAddress = addressRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Can't find product in database"));
            currentAddress.setProvince(address.getProvince());
            currentAddress.setDistrict(address.getDistrict());
            currentAddress.setWard(address.getWard());
            currentAddress.setStreet(address.getStreet());
            currentAddress.setAddress(address.getAddress());
            currentAddress.setHamlet(address.getHamlet());
            currentAddress.setUser(user);
            return addressRepository.save(currentAddress);
        } catch (NotFoundException e) {
            throw new NotFoundException("Can't find product in database");
        }
    }

}
