package vn.demo_shipping.shipping.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Valid
public class OrderRequest {
    @NotBlank(message = "Note is blank!")
    private String note;

    @NotBlank(message = "Name is blank!")
    private String return_name;

    @NotBlank(message = "Address is blank!")
    private String return_address;

    @NotBlank(message = "Province is blank!")
    private String return_province;

    @NotBlank(message = "District is blank!")
    private String return_district;

    @NotBlank(message = "Ward is blank!")
    private String return_ward;

    @NotBlank(message = "Street is blank!")
    private String return_street;

    @NotBlank(message = "Phone number is blank!")
    @Pattern(regexp = "^\\d{10}$", message = "Invalid phone number")
    private String return_tel;

    @Email(message = "Name is blank!")
    private String return_email;

    @Pattern(regexp = "^[01]$", message = "Invalid value for freeship [0 1]")
    private Integer is_freeship;

    @NotBlank(message = "Weight option is blank!")
    @Min(0)
    private Double weight_option;

    @NotBlank(message = "Weight is blank!")
    @Min(0)
    private Double total_weight;

    @NotBlank(message = "Value is blank!")
    @Min(0)
    private Double value;

    @NotBlank(message = "Invoice id is blank")
    private Long invoice_id;

    @NotEmpty(message = "Invalid warehouse")
    private Long warehouse_id;
}
