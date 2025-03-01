package vn.demo_shipping.shipping.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
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
@Entity
@Table(name = "order_detail")
public class OrderDetail {

    @EmbeddedId
    private InvoiceProductId id;

    private Double tax;

    private Integer quantity;

    @ManyToOne
    @MapsId("product_id")
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product product;

    @ManyToOne
    @MapsId("invoice_id")
    @JoinColumn(name = "invoice_id")
    @JsonBackReference
    private Invoice invoice;
}
