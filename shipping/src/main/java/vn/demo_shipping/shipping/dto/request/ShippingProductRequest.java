package vn.demo_shipping.shipping.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShippingProductRequest {
    private String name;
    private Double weight;
    private Integer quantity;
    private String product_code;
}
