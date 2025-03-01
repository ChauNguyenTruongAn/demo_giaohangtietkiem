package vn.demo_shipping.shipping.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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
public class InvoiceRequest {

    private Double total;

    private InvoiceProductId order_detail_id;

    private Long user_id;
}
