package web.converter;

import web.dto.OrderProductDto;
import core.model.OrderProduct;
import core.model.OrderProductKey;
import org.springframework.stereotype.Component;

@Component
public class OrderProductConverter extends BaseConverter<OrderProductKey, OrderProduct, OrderProductDto> {
    @Override
    public OrderProduct convertDtoToModel(OrderProductDto dto) {
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setId(dto.getId());
        return orderProduct;
    }

    @Override
    public OrderProductDto convertModelToDto(OrderProduct orderProduct) {
        OrderProductDto orderProductDto = new OrderProductDto();
        orderProductDto.setId(orderProduct.getId());
        return orderProductDto;
    }
}
