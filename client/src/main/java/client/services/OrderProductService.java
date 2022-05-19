package client.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.converter.OrderProductConverter;
import web.dto.OrderProductDto;
import web.dto.OrderProductsDto;
import core.model.OrderProduct;
import core.model.OrderProductKey;
import core.model.exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Service
public class OrderProductService {
    private final RestTemplate restTemplate;
    private final ExecutorService executorService;
    private final OrderProductConverter orderProductConverter;
    private static final Logger logger= LoggerFactory.getLogger(OrderProductService.class);

    @Autowired
    public OrderProductService(RestTemplate restTemplate, ExecutorService executorService, OrderProductConverter orderProductConverter) {
        this.restTemplate = restTemplate;
        this.executorService = executorService;
        this.orderProductConverter = orderProductConverter;
    }

    public CompletableFuture<Iterable<OrderProduct>> getAllOrderProducts() {
        logger.trace("getAllOrderProducts() method entered");
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = "http://localhost:8080/api/order-products";
                OrderProductsDto orderProductsDto = restTemplate.getForObject(url, OrderProductsDto.class);
                if (orderProductsDto == null)
                    throw new ServiceException("Could not get OrderProducts!");
                return orderProductConverter.convertDtosToModels(orderProductsDto.getOrderProducts());
            } catch (ResourceAccessException e) {
                throw new ServiceException(e.getMessage());
            }
        }, executorService);
    }

    public CompletableFuture<String> addOrderProduct(Integer orderId, Integer productId) {
        logger.trace("addOrderProduct() method entered");
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = "http://localhost:8080/api/order-products/add";
                OrderProductKey orderProductKey = new OrderProductKey(orderId, productId);
                OrderProductDto orderProductDto = new OrderProductDto();
                orderProductDto.setId(orderProductKey);
                restTemplate.postForObject(url, orderProductDto, OrderProductDto.class);
                return "Added OrderProduct";
            } catch (ResourceAccessException e) {
                throw new ServiceException("Could not add OrderProduct!");
            }
        }, executorService);
    }

    public CompletableFuture<String> deleteOrderProduct(Integer orderId, Integer productId) {
        logger.trace("deleteOrderProduct() method entered");
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = "http://localhost:8080/api/order-products/delete/{orderId}/{productId}";
                restTemplate.delete(url, orderId, productId);
                return "Deleted OrderProduct";
            } catch (ResourceAccessException e) {
                throw new ServiceException("Could not delete OrderProduct!");
            }
        }, executorService);
    }
}
