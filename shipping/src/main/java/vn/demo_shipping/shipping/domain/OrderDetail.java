package vn.demo_shipping.shipping.domain;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
public class OrderDetail extends AbstractEntity<Long> {
    private Double total;

    @OneToMany(mappedBy = "order_detail")
    private Set<Product> products;

    @OneToOne
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;
}
