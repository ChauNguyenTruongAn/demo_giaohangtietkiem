    package vn.demo_shipping.shipping.dto.request;

    import jakarta.validation.Valid;
    import jakarta.validation.constraints.Email;
    import jakarta.validation.constraints.Min;
    import jakarta.validation.constraints.NotBlank;
    import jakarta.validation.constraints.NotNull;
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
    @Valid
    public class WarehouseRequest {
        @NotBlank(message = "Name is blank!")
        private String pick_name;

        @NotNull(message = "Money is blank!")
        @Min(value = 0, message = "Money must be not negative")
        private Double pick_money;

        @NotNull(message = "Invalid address id!")
        @Min(0)
        private Integer pick_address_id;

        @NotBlank(message = "Address is blank!")
        private String pick_address;

        @NotBlank(message = "Province is blank!")
        private String pick_province;

        @NotBlank(message = "District is blank!")
        private String pick_district;

        @NotBlank(message = "Ward is blank!")
        private String pick_ward;

        @NotBlank(message = "Street is blank!")
        private String pick_street;

        @NotBlank(message = "Phone number is blank!")
        @Pattern(regexp = "^\\d{10}$", message = "Invalid phone number")
        private String pick_tel;

        @Email(message = "Email is blank!")
        private String pick_email;

        private Long order_id;
    }
