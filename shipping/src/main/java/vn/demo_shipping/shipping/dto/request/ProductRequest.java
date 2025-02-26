package vn.demo_shipping.shipping.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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
public class ProductRequest {
    @NotBlank(message = "Please fill a value for name")
    private String name;

    @NotNull(message = "Please fill a value for price")
    @Min(value = 0, message = "Price must be a positive value")
    private Double price;

    @NotNull(message = "Please fill a value for weight")
    @Min(value = 0, message = "Weight must be a positive value")
    private Double weight;

    private String image;

    @NotNull(message = "Please fill a value for invention")
    private Integer inventory;

    @NotNull(message = "Product must belong to valid category")
    private Long category_id;
}
