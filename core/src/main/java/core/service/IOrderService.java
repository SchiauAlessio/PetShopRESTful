package core.service;

import core.model.Orders;
import core.model.exceptions.ValidatorException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IOrderService {
    List<Orders> getAllOrders();
    List<Orders> getOrdersByClient(Integer clientId);
    List<Orders> getOrdersByProduct(Integer productId);

    @Transactional
    Orders addOrder(Orders order, Integer clientId) throws ValidatorException;

    @Transactional
    Orders updateOrder(Orders order, Integer newClientId) throws ValidatorException;

    @Transactional
    void deleteOrder(Integer id);

    List<Orders> getFilteredOrders(String partialDetails);
    List<Orders> getSortedOrders();
}
