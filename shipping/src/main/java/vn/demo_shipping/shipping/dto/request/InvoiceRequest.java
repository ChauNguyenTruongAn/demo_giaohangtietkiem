package vn.demo_shipping.shipping.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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
public class InvoiceRequest {
    @NotNull(message = "Please fill a value for total")
    @Min(value = 0, message = "Value not ")
    private Double total;

    @NotNull(message = "Please fill a value for order_detail_id")
    private Long order_detail_id;

    @NotNull(message = "Please fill a value for user id")
    private Long user_id;
}
