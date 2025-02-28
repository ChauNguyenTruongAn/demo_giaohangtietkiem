package vn.demo_shipping.shipping.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Valid
public class AddressRequest {
    @NotBlank(message = "Province does not blank")
    private String province;

    @NotBlank(message = "District does not blank")
    private String district;

    @NotBlank(message = "Ward does not blank")
    private String ward;

    @NotBlank(message = "Street does not blank")
    private String street;

    @NotBlank(message = "address does not blank")
    private String address;

    @NotBlank(message = "District does not hamlet")
    private String hamlet;

    @NotNull(message = "Invalid user")
    private Long user_id;
}
