package core.service.impl;

import core.model.Orders;
import core.model.exceptions.ServiceException;
import core.model.exceptions.ValidatorException;
import core.model.validators.OrderValidator;
import core.model.validators.Validator;
import core.repository.ClientRepository;
import core.repository.OrderProductRepository;
import core.repository.OrderRepository;
import core.service.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements IOrderService {
    public static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final OrderProductRepository orderProductRepository;
    private final Validator<Orders> orderValidator;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, ClientRepository clientRepository, OrderProductRepository orderProductRepository) {
        this.orderRepository = orderRepository;
        this.clientRepository = clientRepository;
        this.orderProductRepository = orderProductRepository;
        orderValidator = new OrderValidator();
    }

    @Override
    public List<Orders> getAllOrders() {
        logger.trace("Getting the list of all orders");
        List<Orders> orders = orderRepository.findAll();
        logger.trace("Got the list of all orders");
        return orders;
    }

    @Override
    public List<Orders> getFilteredOrders(String partialDetails) {
        logger.trace("Getting the list of orders with details containing " + partialDetails);
        List<Orders> orders = orderRepository.findAllByDetailsContainingIgnoreCase(partialDetails);
        logger.trace("Got the list of filtered orders");
        return orders;
    }

    @Override
    public List<Orders> getSortedOrders() {
        logger.trace("Getting the list of orders sorted by details");
        List<Orders> orders = orderRepository.findAllByOrderByDetailsAsc();
        logger.trace("Got the list of sorted orders");
        return orders;
    }

    @Override
    public List<Orders> getOrdersByProduct(Integer productId) {
        logger.trace("Getting the list of all orders for product with id " + productId);
        List<Integer> orderIds = orderProductRepository.findAllByProduct_Id(productId)
                .stream().map(op -> op.getId().getOrderId()).collect(Collectors.toList());
        List<Orders> orders = orderRepository.findAll()
                .stream().filter(o -> orderIds.contains(o.getId())).collect(Collectors.toList());
        logger.trace("Got the list of all orders for product with id " + productId);
        return orders;
    }

    @Override
    public List<Orders> getOrdersByClient(Integer clientId) {
        logger.trace("Getting the list of all orders for client with id " + clientId);
        List<Orders> orders = orderRepository.findAllByClient_Id(clientId);
        logger.trace("Got the list of of all orders for client with id " + clientId);
        return orders;
    }

    @Override
    public Orders addOrder(Orders order, Integer clientId) throws ValidatorException {
        orderValidator.validate(order);
        logger.trace("Adding order " + order + " to client " + clientId);
        clientRepository.findById(clientId).ifPresentOrElse(
                c -> {
                    orderRepository.save(order);
                    c.getOrders().add(order);
                    clientRepository.save(c);
                },
                () -> {
                    String error = "Client with id " + clientId + " was not found!";
                    logger.trace(error);
                    throw new ServiceException(error);
                }
        );
        logger.trace("Added order " + order);
        return order;
    }

    @Override
    public Orders updateOrder(Orders order, Integer newClientId) throws ValidatorException {
        orderValidator.validate(order);
        logger.trace("Updating order with id " + order.getId() + " with new client id " + newClientId);
        orderRepository.findById(order.getId()).ifPresentOrElse(
                o -> clientRepository.findById(newClientId).ifPresentOrElse(
                        c -> {
                            o.setDetails(order.getDetails());
                            o.setClient(c);
                            orderRepository.save(o);
                        },
                        () -> {
                            String error = "Client with id " + newClientId + " was not found!";
                            logger.trace(error);
                            throw new ServiceException(error);
                        }
                ),
                () -> {
                    throw new ServiceException("Order was not found!");
                }
        );
       logger.trace("Updated order: " + order);
       return order;
    }

    @Override
    public void deleteOrder(Integer id) {
        logger.trace("Deleting order with id " + id);
        orderRepository.findById(id).ifPresentOrElse(
                orderRepository::delete,
                () -> {
                    String error = "Order with id " + id + " was not found!";
                    logger.trace(error);
                    throw new ServiceException(error);
                }
        );
        logger.trace("Deleted order with id " + id);
    }
}
