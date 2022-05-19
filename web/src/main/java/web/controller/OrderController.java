package web.controller;

import core.model.exceptions.ValidatorException;
import core.service.IClientService;
import core.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.converter.OrderConverter;
import web.dto.OrderDto;
import web.dto.OrdersDto;

@RestController
public class OrderController {
    private final IOrderService orderService;
    private final IClientService clientService;
    private final OrderConverter orderConverter;

    @Autowired
    public OrderController(IOrderService orderService, IClientService clientService, OrderConverter orderConverter) {
        this.orderService = orderService;
        this.clientService = clientService;
        this.orderConverter = orderConverter;
    }

    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    OrdersDto getAllOrders() {
        return new OrdersDto(orderConverter.convertModelsToDtos(orderService.getAllOrders()));
    }

    @RequestMapping(value = "/orders/filter/{partialDetails}", method = RequestMethod.GET)
    OrdersDto getFilteredOrders(@PathVariable String partialDetails) {
        return new OrdersDto(orderConverter.convertModelsToDtos(orderService.getFilteredOrders(partialDetails)));
    }

    @RequestMapping(value = "/orders/sorted", method = RequestMethod.GET)
    OrdersDto getSortedOrders() {
        return new OrdersDto(orderConverter.convertModelsToDtos(orderService.getSortedOrders()));
    }

    @RequestMapping(value = "/orders/by-product/{productId}", method = RequestMethod.GET)
    OrdersDto getOrdersByProduct(@PathVariable Integer productId) {
        return new OrdersDto(orderConverter.convertModelsToDtos(orderService.getOrdersByProduct(productId)));
    }

    @RequestMapping(value = "/orders/by-client/{clientId}", method = RequestMethod.GET)
    OrdersDto getOrdersByClient(@PathVariable Integer clientId) {
        return new OrdersDto(orderConverter.convertModelsToDtos(orderService.getOrdersByClient(clientId)));
    }

    @RequestMapping(value = "/orders/add-to-client/{clientId}", method = RequestMethod.POST)
    ResponseEntity<?> addOrder(@RequestBody OrderDto orderDto, @PathVariable Integer clientId) {
        try {
            orderService.addOrder(orderConverter.convertDtoToModel(orderDto), clientId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ValidatorException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/orders/update/{newClientId}", method = RequestMethod.PUT)
    ResponseEntity<?> updateOrder(@RequestBody OrderDto orderDto, @PathVariable Integer newClientId) {
        try {
            orderService.updateOrder(orderConverter.convertDtoToModel(orderDto), newClientId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ValidatorException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @RequestMapping(value = "/orders/delete/{id}", method = RequestMethod.DELETE)
    ResponseEntity<?> deleteOrder(@PathVariable Integer id) {
        orderService.deleteOrder(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
