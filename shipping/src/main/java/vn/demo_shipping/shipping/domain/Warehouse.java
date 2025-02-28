package vn.demo_shipping.shipping.domain;

import jakarta.persistence.Entity;
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
@Table(name = "warehouses")
public class Warehouse extends AbstractEntity<Long> {
    private String pick_name;
    private Double pick_money;
    private Integer pick_address_id;
    private String pick_address;
    private String pick_province;
    private String pick_district;
    private String pick_ward;
    private String pick_street;
    private String pick_tel;
    private String pick_email;

    @OneToOne(mappedBy = "warehouse")
    private Order order;
}
