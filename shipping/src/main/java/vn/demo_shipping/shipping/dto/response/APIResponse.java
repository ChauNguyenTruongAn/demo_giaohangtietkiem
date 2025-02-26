package vn.demo_shipping.shipping.dto.response;

import java.time.LocalDateTime;
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
public class APIResponse<T> {
    private Integer status;
    private String message;
    private T data;
    private LocalDateTime create_time;
}
