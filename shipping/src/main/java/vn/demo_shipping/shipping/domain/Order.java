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
@Table(name = "invoices")
public class Order extends AbstractEntity<Long> {
    private String note;
    private String return_name;
    private String return_address;
    private String return_province;
    private String return_district;
    private String return_ward;
    private String return_street;
    private String return_tel;
    private String return_email;
    private Integer is_freeship;
    private Double weight_option;
    private Double total_weight;
    private Double value;

    @OneToOne
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    @OneToMany(mappedBy = "order")
    private Set<Warehouse> warehouses;
}
