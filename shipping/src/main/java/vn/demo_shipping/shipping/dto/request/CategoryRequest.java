package vn.demo_shipping.shipping.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
public class CategoryRequest {
    @NotBlank(message = "Name must be fill")
    private String name;

    @NotBlank(message = "Description must be fill")
    private String description;
}
