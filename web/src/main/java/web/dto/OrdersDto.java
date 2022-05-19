package web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrdersDto {
    private LinkedHashSet<OrderDto> orders;
}
