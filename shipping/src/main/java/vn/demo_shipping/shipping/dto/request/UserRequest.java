package vn.demo_shipping.shipping.dto.request;

import java.util.Date;
import java.util.Set;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.demo_shipping.shipping.domain.Address;
import vn.demo_shipping.shipping.domain.Invoice;
import vn.demo_shipping.shipping.dto.validator.GenderSubset;
import vn.demo_shipping.shipping.util.Gender;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Valid
public class UserRequest {
    @NotBlank(message = "Please fill a value for FullName")
    private String full_name;

    @GenderSubset(message = "Gender does not invalid", anyOf = { Gender.MALE, Gender.FEMALE })
    private Gender gender;

    @NotNull(message = "Date of birth must be fill")
    private Date date_of_birth;

    @Pattern(regexp = "^\\d{10}$", message = "Invalid phone number")
    private String tel;

    @Email(message = "Invalid email!")
    private String email;

    @NotBlank(message = "Username must be fill, username does not empty")
    private String username;

    @NotBlank(message = "Please fill a value for password!")
    private String password;

    private Set<Address> addresses;

    private Set<Invoice> invoices;
}
