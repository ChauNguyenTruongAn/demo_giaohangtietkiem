package vn.demo_shipping.shipping.domain;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
public class Invoice extends AbstractEntity<Long> {
    private Double total;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<OrderDetail> orderDetails;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    private Long address_id;

    public void addOrderDetail(OrderDetail orderDetail) {
        if (orderDetail == null) {
            throw new IllegalArgumentException("Invalid order detail");
        }
        if (!orderDetails.contains(orderDetail)) {
            orderDetails.add(orderDetail);
        }
    }

    public void removeOrderDetail(OrderDetail orderDetail) {
        if (orderDetail == null) {
            throw new IllegalArgumentException("Invalid order detail");
        }

        orderDetail.setInvoice(null);
        orderDetails.remove(orderDetail);
    }
}
