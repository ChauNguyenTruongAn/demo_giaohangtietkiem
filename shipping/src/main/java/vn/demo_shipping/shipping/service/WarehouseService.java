package vn.demo_shipping.shipping.service;

import java.util.List;

import org.springframework.data.domain.Page;

import vn.demo_shipping.shipping.domain.Warehouse;
import vn.demo_shipping.shipping.dto.request.WarehouseRequest;

public interface WarehouseService {
    Page<Warehouse> getAllWarehouse(int page, int size, String sort);

    List<Warehouse> getAllWarehouse();

    Warehouse getWarehouseById(Long id);

    Warehouse addWarehouse(WarehouseRequest orderDetail);

    Warehouse updateWarehouse(Long id, WarehouseRequest request);

    String deleteWarehouse(Long id);
}
