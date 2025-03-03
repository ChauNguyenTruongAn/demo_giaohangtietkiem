package vn.demo_shipping.shipping.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderResponse {
    @JsonProperty("partner_id")
    private String partnerId;

    private String label;
    private int area;
    private double fee;

    @JsonProperty("insurance_fee")
    private double insuranceFee;

    @JsonProperty("estimated_pick_time")
    private String estimatedPickTime;

    @JsonProperty("estimated_deliver_time")
    private String estimatedDeliverTime;

    private List<Object> products; // Giả sử mảng này có thể là một danh sách các đối tượng, bạn có thể tạo class
                                   // riêng cho Product nếu cần

    @JsonProperty("status_id")
    private int statusId;

    @JsonProperty("tracking_id")
    private long trackingId;

    @JsonProperty("sorting_code")
    private String sortingCode;

    @JsonProperty("date_to_delay_pick")
    private String dateToDelayPick;

    @JsonProperty("pick_work_shift")
    private int pickWorkShift;

    @JsonProperty("date_to_delay_deliver")
    private String dateToDelayDeliver;

    @JsonProperty("deliver_work_shift")
    private int deliverWorkShift;

    @JsonProperty("pkg_draft_id")
    private int pkgDraftId;

    @JsonProperty("package_id")
    private String packageId;

    @JsonProperty("cost_id")
    private Object costId; // Có thể là null hoặc đối tượng, cần xử lý tùy vào yêu cầu

    @JsonProperty("is_xfast")
    private int isXFast;
}
