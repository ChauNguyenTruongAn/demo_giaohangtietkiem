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
import vn.demo_shipping.shipping.domain.InvoiceProductId;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Valid
public class OrderDetailRequest {

    @NotNull(message = "OrderDetail can not null")
    private InvoiceProductId id;

    @NotNull(message = "Total is not null")
    @Min(0)
    private double total;

    @NotNull(message = "Invalid products")
    @NotEmpty(message = "Order details must have at least one product")
    private Long[] products;

    @NotNull(message = "Order detail must have associated with an invoice. Invoice id is null")
    private Long invoice_id;
}
