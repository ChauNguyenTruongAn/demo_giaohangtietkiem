package vn.demo_shipping.shipping.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
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
@Table(name = "orders")
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

    @OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "invoice_id")
    @JsonBackReference
    private Invoice invoice;

    @OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "warehouse_id")
    @JsonBackReference
    private Warehouse warehouses;

    public void addInvoice(Invoice invoice) {
        if (invoice == null)
            throw new IllegalArgumentException("Invalid invoice");
        this.invoice = invoice;
    }

    public void removeInvoice(Invoice invoice) {
        if (invoice == null)
            throw new IllegalArgumentException("Invalid invoice");
        this.invoice = null;
    }

    public void addWarehouse(Warehouse warehouse) {
        if (warehouse == null)
            throw new IllegalArgumentException("Invalid invoice");
        this.warehouses = warehouse;
    }

    public void removeWarehouse(Warehouse warehouse) {
        if (warehouse == null)
            throw new IllegalArgumentException("Invalid invoice");
        this.warehouses = null;
    }

}
