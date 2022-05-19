package core.repository;

import core.model.OrderProduct;
import core.model.OrderProductKey;

import java.util.List;

public interface OrderProductRepository extends IRepository<OrderProduct, OrderProductKey> {
    List<OrderProduct> findAllByProduct_Id(Integer productId);
    List<OrderProduct> findAllByOrder_Id(Integer orderId);
}
