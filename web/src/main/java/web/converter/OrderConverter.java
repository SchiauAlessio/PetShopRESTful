package web.converter;

import web.dto.OrderDto;
import core.model.Orders;
import org.springframework.stereotype.Component;

@Component
public class OrderConverter extends BaseConverter<Integer, Orders, OrderDto> {
    @Override
    public Orders convertDtoToModel(OrderDto dto) {
        Orders order = new Orders();
        order.setId(dto.getId());
        order.setDetails(dto.getDetails());
        return order;
    }

    @Override
    public OrderDto convertModelToDto(Orders order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setDetails(order.getDetails());
        orderDto.setId(order.getId());
        return orderDto;
    }
}
