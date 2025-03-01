package vn.demo_shipping.shipping.dto.request;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
public class ShippingRequest {
    private List<ShippingProductRequest> products;
    private ShippingAddressRequest order;
}

// @Getter
// @Setter
// @Valid
// @Builder
// @NoArgsConstructor
// @AllArgsConstructor
// public class ShippingOrder implements Serializable {
// private Set<Product> products;

// private Long shippingOrderId;

// @NotBlank(message = "Pick name is blank!")
// private String pick_name;

// private Double pick_money;

// @NotBlank(message = "Pick address is blank!")
// private String pick_address;

// @NotBlank(message = "Pick province is blank!")
// private String pick_province;

// @NotBlank(message = "Pick district is blank!")
// private String pick_district;

// @Pattern(regexp = "^\\d{10}$", message = "Invalid phone number")
// private String pick_tel;

// @NotBlank(message = "Name is blank!")
// private String name;

// @NotBlank(message = "Address is blank!")
// private String address;

// @NotBlank(message = "Province is blank!")
// private String province;

// @NotBlank(message = "District is blank!")
// private String district;

// @NotBlank(message = "Ward is blank!")
// private String ward;

// @NotBlank(message = "Street is blank!")
// private String street;

// @NotBlank(message = "Hamlet is blank!")
// private String hamlet;

// @Pattern(regexp = "^\\d{10}$", message = "Invalid phone number")
// private String tel;

// @Email(message = "Invalid email!")
// private String email;

// @NotBlank(message = "Return name is blank!")
// private String return_name;

// @NotBlank(message = "Return address is blank!")
// private String return_address;

// @NotBlank(message = "Return province is blank!")
// private String return_province;

// @NotBlank(message = "Return ward is blank!")
// private String return_ward;

// @Pattern(regexp = "^\\d{10}$", message = "Invalid phone number")
// private String return_tel;

// @Email(message = "Invalid email!")
// private String return_email;

// private Double value;
// }
