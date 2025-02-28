package vn.demo_shipping.shipping.service.impl;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.demo_shipping.shipping.domain.Order;
import vn.demo_shipping.shipping.domain.Warehouse;
import vn.demo_shipping.shipping.dto.request.WarehouseRequest;
import vn.demo_shipping.shipping.exception.NotFoundException;
import vn.demo_shipping.shipping.exception.NullObjectException;
import vn.demo_shipping.shipping.repository.OrderRepository;
import vn.demo_shipping.shipping.repository.WarehouseRepository;
import vn.demo_shipping.shipping.service.WarehouseService;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final OrderRepository orderRepository;

    @Override
    public Warehouse addWarehouse(WarehouseRequest request) {
        if (request == null)
            throw new NullObjectException("Warehouse is null!!");

        Order order = orderRepository.findById(request.getOrder_id()).orElseThrow(
                () -> new NotFoundException("Not found Order"));

        Warehouse existingWarehouse = new Warehouse();

        existingWarehouse.setPick_name(request.getPick_name());
        existingWarehouse.setPick_money(request.getPick_money());
        existingWarehouse.setPick_address_id(request.getPick_address_id());
        existingWarehouse.setPick_address(request.getPick_address());
        existingWarehouse.setPick_province(request.getPick_province());
        existingWarehouse.setPick_district(request.getPick_district());
        existingWarehouse.setPick_ward(request.getPick_ward());
        existingWarehouse.setPick_street(request.getPick_street());
        existingWarehouse.setPick_tel(request.getPick_tel());
        existingWarehouse.setPick_email(request.getPick_email());
        existingWarehouse.setOrder(order);

        return warehouseRepository.save(existingWarehouse);
    }

    @Override
    public String deleteWarehouse(Long id) {
        if (!warehouseRepository.existsById(id))
            throw new NullObjectException("Warehouse is null!!");
        warehouseRepository.deleteById(id);
        return "Delete warehouse success";
    }

    @Override
    public Page<Warehouse> getAllWarehouse(int page, int size, String request) {
        Sort sort;
        if (request.compareTo("asc") == 0) {
            sort = Sort.by(Sort.Order.asc("name"));
        } else if (request.compareTo("desc") == 0) {
            sort = Sort.by(Sort.Order.desc("name"));
        } else {
            sort = Sort.by(Sort.Order.asc("name"));
        }
        Pageable pageable = PageRequest.of(page, size, sort);
        return warehouseRepository.findAll(pageable);

    }

    @Override
    public List<Warehouse> getAllWarehouse() {
        return warehouseRepository.findAll();
    }

    @Override
    public Warehouse getWarehouseById(Long id) {
        return warehouseRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Warehouse not found"));
    }

    @Override
    public Warehouse updateWarehouse(Long id, WarehouseRequest request) {

        Order order = orderRepository.findById(request.getOrder_id()).orElseThrow(
                () -> new NotFoundException("Not found order"));

        Warehouse existingWarehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Warehouse with ID " + id + " not found"));

        existingWarehouse.setPick_name(request.getPick_name());
        existingWarehouse.setPick_money(request.getPick_money());
        existingWarehouse.setPick_address_id(request.getPick_address_id());
        existingWarehouse.setPick_address(request.getPick_address());
        existingWarehouse.setPick_province(request.getPick_province());
        existingWarehouse.setPick_district(request.getPick_district());
        existingWarehouse.setPick_ward(request.getPick_ward());
        existingWarehouse.setPick_street(request.getPick_street());
        existingWarehouse.setPick_tel(request.getPick_tel());
        existingWarehouse.setPick_email(request.getPick_email());
        existingWarehouse.setOrder(order);

        return warehouseRepository.save(existingWarehouse);
    }
}
