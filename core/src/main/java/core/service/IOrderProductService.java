package core.service;

import core.model.OrderProduct;
import core.model.exceptions.ValidatorException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IOrderProductService {

    @Transactional
    OrderProduct addOrderProduct(OrderProduct orderProduct) throws ValidatorException;

    void deleteOrderProduct(Integer orderId, Integer productId) throws ValidatorException;

    List<OrderProduct> getAllOrderProducts();
}
