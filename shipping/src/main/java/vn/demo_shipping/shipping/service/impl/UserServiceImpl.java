package vn.demo_shipping.shipping.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.demo_shipping.shipping.domain.Address;
import vn.demo_shipping.shipping.domain.Invoice;
import vn.demo_shipping.shipping.domain.User;
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
    public User addUser(User user) {
        if (user == null)
            throw new NullObjectException("User is null!!");
        return userRepository.save(user);
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
            sort = Sort.by(Sort.Order.asc("name"));
        } else if (request.compareTo("desc") == 0) {
            sort = Sort.by(Sort.Order.desc("name"));
        } else {
            sort = Sort.by(Sort.Order.asc("name"));
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

        Set<Address> newAddresses = new HashSet<>();

        for (Long address_id : request.getAddresses()) {
            Address address = addressRepository.findById(address_id).orElseThrow(
                    () -> new NotFoundException("Invalid address or address is null"));
            newAddresses.add(address);
        }

        // get invoice
        Set<Invoice> newInvoices = new HashSet<>();
        for (Long invoice_id : request.getInvoices()) {
            Invoice invoice = invoiceRepository.findById(invoice_id).orElseThrow(
                    () -> new NotFoundException("Invalid invoice"));
            newInvoices.add(invoice);
        }

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with ID " + id + " not found"));

        existingUser.setFull_name(request.getFull_name());
        existingUser.setGender(request.getGender());
        existingUser.setDate_of_birth(request.getDate_of_birth());
        existingUser.setTel(request.getTel());
        existingUser.setEmail(request.getEmail());
        existingUser.setUsername(request.getUsername());
        existingUser.setPassword(request.getPassword());
        existingUser.setAddresses(newAddresses);
        existingUser.setInvoices(newInvoices);
        return userRepository.save(existingUser);
    }

}
