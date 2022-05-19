package web.controller;

import core.model.exceptions.ValidatorException;
import core.service.IOrderProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.converter.OrderProductConverter;
import web.dto.OrderProductDto;
import web.dto.OrderProductsDto;

@RestController
public class OrderProductController {
    private final IOrderProductService orderProductService;
    private final OrderProductConverter orderProductConverter;

    @Autowired
    public OrderProductController(IOrderProductService orderProductService, OrderProductConverter orderProductConverter) {
        this.orderProductService = orderProductService;
        this.orderProductConverter = orderProductConverter;
    }

    @RequestMapping(value = "/order-products", method = RequestMethod.GET)
    OrderProductsDto getAllOrderProducts() {
        return new OrderProductsDto(orderProductConverter.convertModelsToDtos(orderProductService.getAllOrderProducts()));
    }

    @RequestMapping(value = "/order-products/add", method = RequestMethod.POST)
    ResponseEntity<?> addOrderProduct(@RequestBody OrderProductDto orderProductDto) {
        try {
            orderProductService.addOrderProduct(orderProductConverter.convertDtoToModel(orderProductDto));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ValidatorException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @RequestMapping(value = "/order-products/delete/{orderId}/{productId}", method = RequestMethod.DELETE)
    ResponseEntity<?> deleteOrderProduct(@PathVariable Integer orderId, @PathVariable Integer productId) {
        try {
            orderProductService.deleteOrderProduct(orderId, productId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ValidatorException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
