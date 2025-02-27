package vn.demo_shipping.shipping.service;

import java.util.List;

import org.springframework.data.domain.Page;

import vn.demo_shipping.shipping.domain.Address;
import vn.demo_shipping.shipping.dto.request.AddressRequest;

public interface AddressService {
    Page<Address> getAllAddress(int page, int size, String sort);

    List<Address> getAllAddress();

    Address getAddressById(Long id);

    Address addAddress(Address Address);

    Address updateAddress(Long id, AddressRequest Address);

    String deleteAddress(Long id);
}
