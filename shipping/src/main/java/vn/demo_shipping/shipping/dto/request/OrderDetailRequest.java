package vn.demo_shipping.shipping.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
@Valid
public class OrderDetailRequest {
    @NotNull(message = "Quantity is not null")
    @Min(0)
    private Integer quantity;

    @NotNull(message = "Tax is not null")
    @Min(0)
    private Double tax;

    @NotNull(message = "Invalid products")
    @NotEmpty(message = "Order details must have at least one product")
    private Long product_id;

    @NotNull(message = "Order detail must have associated with an invoice. Invoice id is null")
    private Long invoice_id;
}
