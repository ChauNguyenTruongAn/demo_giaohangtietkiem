package vn.demo_shipping.shipping.dto.request;


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
public class ShippingAddressRequest {
    private String id;
    private String pick_name;
    private Double pick_money;
    private String pick_address;
    private String pick_province;
    private String pick_district;
    private String pick_tel;
    private String name;
    private String address;
    private String province;
    private String district;
    private String ward;
    private String street;
    private String hamlet;
    private String tel;
    private String email;
    private String return_name;
    private String return_address;
    private String return_province;
    private String return_ward;
    private String return_tel;
    private String return_email;
    private Double value;

}
