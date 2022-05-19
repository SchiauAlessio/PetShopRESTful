package client.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.converter.OrderConverter;
import web.dto.OrderDto;
import web.dto.OrdersDto;
import core.model.Orders;
import core.model.exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Service
public class OrderService {
    private final RestTemplate restTemplate;
    private final ExecutorService executorService;
    private final OrderConverter orderConverter;
    private static final Logger logger= LoggerFactory.getLogger(OrderService.class);

    @Autowired
    public OrderService(RestTemplate restTemplate, ExecutorService executorService, OrderConverter orderConverter) {
        this.restTemplate = restTemplate;
        this.executorService = executorService;
        this.orderConverter = orderConverter;
    }

    public CompletableFuture<Iterable<Orders>> getAllOrders() {
        logger.trace("getAllOrders() method entered");
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = "http://localhost:8080/api/orders";
                OrdersDto ordersDto = restTemplate.getForObject(url, OrdersDto.class);
                if (ordersDto == null)
                    throw new ServiceException("Could not get orders!");
                return orderConverter.convertDtosToModels(ordersDto.getOrders());
            } catch (ResourceAccessException e) {
                throw new ServiceException(e.getMessage());
            }
        }, executorService);
    }

    public CompletableFuture<Iterable<Orders>> getFilteredOrders(String partialName) {
        logger.trace("getFilteredOrders() method entered");
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = "http://localhost:8080/api/orders/filter/{partialName}";
                OrdersDto ordersDto = restTemplate.getForObject(url, OrdersDto.class, partialName);
                if (ordersDto == null)
                    throw new ServiceException("Could not get orders!");
                return orderConverter.convertDtosToModels(ordersDto.getOrders());
            } catch (ResourceAccessException e) {
                throw new ServiceException(e.getMessage());
            }
        }, executorService);
    }

    public CompletableFuture<Iterable<Orders>> getSortedOrders() {
        logger.trace("getSortedOrders() method entered");
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = "http://localhost:8080/api/orders/sorted";
                OrdersDto ordersDto = restTemplate.getForObject(url, OrdersDto.class);
                if (ordersDto == null)
                    throw new ServiceException("Could not get orders!");
                return orderConverter.convertDtosToModels(ordersDto.getOrders());
            } catch (ResourceAccessException e) {
                throw new ServiceException(e.getMessage());
            }
        }, executorService);
    }

    public CompletableFuture<Iterable<Orders>> getOrdersByProduct(Integer productId) {
        logger.trace("getOrdersByProduct() method entered");
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = "http://localhost:8080/api/orders/by-product/{productId}";
                OrdersDto ordersDto = restTemplate.getForObject(url, OrdersDto.class, productId);
                if (ordersDto == null)
                    throw new ServiceException("Could not get orders!");
                return orderConverter.convertDtosToModels(ordersDto.getOrders());
            } catch (ResourceAccessException e) {
                throw new ServiceException(e.getMessage());
            }
        }, executorService);
    }

    public CompletableFuture<Iterable<Orders>> getOrdersByClient(Integer clientId) {
        logger.trace("getOrdersByClient() method entered");
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = "http://localhost:8080/api/orders/by-client/{clientId}";
                OrdersDto ordersDto = restTemplate.getForObject(url, OrdersDto.class, clientId);
                if (ordersDto == null)
                    throw new ServiceException("Could not get orders!");
                return orderConverter.convertDtosToModels(ordersDto.getOrders());
            } catch (ResourceAccessException e) {
                throw new ServiceException(e.getMessage());
            }
        }, executorService);
    }

    public CompletableFuture<String> addOrder(String details, Integer clientId) {
        logger.trace("addOrder() method entered");
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = "http://localhost:8080/api/orders/add-to-client/{clientId}";
                OrderDto orderDto = new OrderDto(details, clientId);
                restTemplate.postForObject(url, orderDto, OrderDto.class, clientId);
                return "Added order";
            } catch (ResourceAccessException e) {
                throw new ServiceException("Could not add order!");
            }
        }, executorService);
    }

    public CompletableFuture<String> updateOrder(Integer id, String details, Integer newClientId) {
        logger.trace("updateOrder() method entered");
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = "http://localhost:8080/api/orders/update/{newClientId}";
                OrderDto orderDto = new OrderDto(details, newClientId);
                orderDto.setId(id);
                restTemplate.put(url, orderDto, newClientId);
                return "Updated order";
            } catch (ResourceAccessException e) {
                throw new ServiceException("Could not update order!");
            }
        }, executorService);
    }

    public CompletableFuture<String> deleteOrder(Integer id) {
        logger.trace("deleteOrder() method entered");
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = "http://localhost:8080/api/orders/delete/{id}";
                restTemplate.delete(url, id);
                return "Deleted order";
            } catch (ResourceAccessException e) {
                throw new ServiceException("Could not delete order!");
            }
        }, executorService);
    }
}

