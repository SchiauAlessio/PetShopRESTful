package core.service.impl;

import core.model.Orders;
import core.model.OrderProduct;
import core.model.OrderProductKey;
import core.model.Product;
import core.model.exceptions.ServiceException;
import core.model.exceptions.ValidatorException;
import core.model.validators.OrderProductValidator;
import core.model.validators.Validator;
import core.service.IOrderProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import core.repository.OrderProductRepository;
import core.repository.OrderRepository;
import core.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
public class OrderProductServiceImpl implements IOrderProductService {

    public static final Logger logger = LoggerFactory.getLogger(OrderProductServiceImpl.class);
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;
    private final Validator<OrderProduct> orderProductValidator;

    @Autowired
    public OrderProductServiceImpl(OrderRepository orderRepository, ProductRepository productRepository, OrderProductRepository orderProductRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderProductRepository = orderProductRepository;
        orderProductValidator = new OrderProductValidator();
    }

    @Override
    public OrderProduct addOrderProduct(OrderProduct orderProduct) throws ValidatorException {
        orderProductValidator.validate(orderProduct);
        logger.trace("Adding OrderProduct " + orderProduct);
        Integer orderId = orderProduct.getId().getOrderId();
        Integer productId = orderProduct.getId().getProductId();
        Optional<Orders> order = orderRepository.findById(orderId);
        order.ifPresentOrElse((Orders o) -> {
            Optional<Product> product = productRepository.findById(productId);
            product.ifPresentOrElse((Product p) -> {
                orderProductRepository.save(orderProduct);
            }, () -> {
                throw new ServiceException("Product was not found!");
            });
        }, () -> {
            throw new ServiceException("Order was not found!");
        });
        logger.trace("Added OrderProduct " + orderProduct);
        return orderProduct;
    }

    @Override
    public List<OrderProduct> getAllOrderProducts() {
        logger.trace("Getting the list of all OrderProducts");
        List<OrderProduct> orderProducts = orderProductRepository.findAll();
        logger.trace("Got the list of all OrderProducts");
        return orderProducts;
    }

    @Override
    public void deleteOrderProduct(Integer orderId, Integer productId) {
        logger.trace("Deleting OrderProduct (" + orderId + "," + productId + ")");
        orderProductRepository.findById(new OrderProductKey(orderId, productId))
                .ifPresentOrElse(
                        orderProduct -> orderProductRepository.deleteById(orderProduct.getId()),
                        () -> {
                            throw new ServiceException("OrderProduct was not found!");
                        }
                );
        logger.trace("Deleted OrderProduct (" + orderId + "," + productId + ")");
    }
}
