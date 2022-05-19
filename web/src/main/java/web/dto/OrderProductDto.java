package web.dto;

import lombok.*;
import core.model.Orders;
import core.model.OrderProductKey;
import core.model.Product;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OrderProductDto extends BaseDto<OrderProductKey> {
    private Orders order;
    private Product product;
}
