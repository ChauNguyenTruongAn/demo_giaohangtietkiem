package vn.demo_shipping.shipping.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "GHTK_Order")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GHTKOrder extends AbstractEntity<Long> {

    @Column(name = "partner_id")
    private String partnerId;

    @Column(name = "label")
    private String label;

    @Column(name = "area")
    private int area;

    @Column(name = "fee")
    private int fee;

    @Column(name = "insurance_fee")
    private int insuranceFee;

    @Column(name = "estimated_pick_time")
    private String estimatedPickTime;

    @Column(name = "estimated_deliver_time")
    private String estimatedDeliverTime;

    @Column(name = "tracking_id")
    private long trackingId;

    @Column(name = "sorting_code")
    private String sortingCode;

    @Column(name = "date_to_delay_pick")
    private String dateToDelayPick;

    @Column(name = "date_to_delay_deliver")
    private String dateToDelayDeliver;

    @Column(name = "deliver_work_shift")
    private int deliverWorkShift;

    @Column(name = "package_id")
    private String packageId;

    // Getters và Setters
}
