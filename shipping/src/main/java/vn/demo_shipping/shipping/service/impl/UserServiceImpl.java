package vn.demo_shipping.shipping.service.impl;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.demo_shipping.shipping.domain.Address;
import vn.demo_shipping.shipping.domain.Invoice;
import vn.demo_shipping.shipping.domain.User;
import vn.demo_shipping.shipping.dto.request.AddressRequest;
import vn.demo_shipping.shipping.dto.request.InvoiceRequest;
import vn.demo_shipping.shipping.dto.request.UserRequest;
import vn.demo_shipping.shipping.exception.NotFoundException;
import vn.demo_shipping.shipping.exception.NullObjectException;
import vn.demo_shipping.shipping.repository.AddressRepository;
import vn.demo_shipping.shipping.repository.InvoiceRepository;
import vn.demo_shipping.shipping.repository.UserRepository;
import vn.demo_shipping.shipping.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final InvoiceRepository invoiceRepository;
    private final AddressRepository addressRepository;

    @Override
    public User addUser(UserRequest request) {
        if (request == null)
            throw new IllegalArgumentException("Invalid request!!");

        User existingUser = new User();
        existingUser.setFull_name(request.getFull_name());
        existingUser.setGender(request.getGender());
        existingUser.setDate_of_birth(request.getDate_of_birth());
        existingUser.setTel(request.getTel());
        existingUser.setEmail(request.getEmail());
        existingUser.setUsername(request.getUsername());
        existingUser.setPassword(request.getPassword());
        existingUser.setAddresses(null);
        existingUser.setInvoices(null);

        return userRepository.save(existingUser);
    }

    @Override
    public String deleteUser(Long id) {
        if (!userRepository.existsById(id))
            throw new NullObjectException("User is null!!");
        userRepository.deleteById(id);
        return "Delete user success";
    }

    @Override
    public Page<User> getAllUser(int page, int size, String request) {
        Sort sort;
        if (request.compareTo("asc") == 0) {
            sort = Sort.by(Sort.Order.asc("id"));
        } else if (request.compareTo("desc") == 0) {
            sort = Sort.by(Sort.Order.desc("id"));
        } else {
            sort = Sort.by(Sort.Order.asc("id"));
        }
        Pageable pageable = PageRequest.of(page, size, sort);
        return userRepository.findAll(pageable);

    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new NotFoundException("User not found"));
    }

    @Override
    public User updateUser(Long id, UserRequest request) {

        // get address

        // Set<Address> newAddresses;

        // newAddresses = request.getAddresses() == null ? new HashSet<>() : request.getAddresses();

        // // get invoice
        // Set<Invoice> newInvoices;

        // newInvoices = request.getInvoices() == null ? new HashSet<>() : request.getInvoices();

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with ID " + id + " not found"));

        existingUser.setFull_name(request.getFull_name());
        existingUser.setGender(request.getGender());
        existingUser.setDate_of_birth(request.getDate_of_birth());
        existingUser.setTel(request.getTel());
        existingUser.setEmail(request.getEmail());
        existingUser.setUsername(request.getUsername());
        existingUser.setPassword(request.getPassword());
        //existingUser.setAddresses(newAddresses);
        //existingUser.setInvoices(newInvoices);
        return userRepository.save(existingUser);
    }

    @Override
    public User addAddressToUser(Long id, AddressRequest request) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new NotFoundException("User not found"));

        Address address = Address.builder()
                .province(request.getProvince())
                .district(request.getDistrict())
                .ward(request.getWard())
                .street(request.getStreet())
                .address(request.getAddress())
                .hamlet(request.getHamlet())
                .user(user)
                .build();

        user.addAddress(address);
        return userRepository.save(user);
    }

    @Override
    public User removeAddressFromUser(Long id, Long addressId) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new NotFoundException("User not found"));

        Address address = addressRepository.findById(addressId).orElseThrow(
                () -> new NotFoundException("Address not found"));

        if (!user.getAddresses().contains(address)) {
            throw new IllegalArgumentException("Address does not belong to the user");
        }

        user.removeAddress(address);
        return userRepository.save(user);
    }

    @Override
    public User addInvoice(Long id, InvoiceRequest request) {

        User user = userRepository.findById(id).orElseThrow(
                () -> new NotFoundException("User not found"));

        Invoice invoice = Invoice.builder()
                .total(request.getTotal())
                // .orderDetail(request.getOrder_detail_id)
                .build();

        user.addInvoice(invoice);
        return userRepository.save(user);
    }

    @Override
    public User remInvoice(Long id, Long invoiceRequest) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new NotFoundException("User not found"));

        Invoice invoice = invoiceRepository.findById(invoiceRequest).orElseThrow(
                () -> new NotFoundException("Invoice not found"));

        if (!user.getInvoices().contains(invoice)) {
            throw new IllegalArgumentException("Invoice does not belong to the user");
        }

        user.removeInvoice(invoice);
        return userRepository.save(user);
    }

    @Override
    public User editAddress(Long id, Long address_id, AddressRequest address) {
        try {
            User user = userRepository.findById(id).orElseThrow(
                    () -> new NotFoundException("User does not exists in database"));

            Address currentAddress = addressRepository.findById(address_id)
                    .orElseThrow(() -> new NotFoundException("Can't find product in database"));

            currentAddress.setProvince(address.getProvince());
            currentAddress.setDistrict(address.getDistrict());
            currentAddress.setWard(address.getWard());
            currentAddress.setStreet(address.getStreet());
            currentAddress.setAddress(address.getAddress());
            currentAddress.setHamlet(address.getHamlet());
            user.addAddress(currentAddress);
            return userRepository.save(user);
        } catch (NotFoundException e) {
            throw new NotFoundException("Can't find product in database");
        }
    }

}
