package web.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OrderDto extends BaseDto<Integer> {
    private String details;
    private Integer clientId;
}
