package core.repository;

import core.model.Orders;

import java.util.List;

public interface OrderRepository extends IRepository<Orders, Integer> {
    List<Orders> findAllByClient_Id(Integer client_id);
    List<Orders> findAllByDetailsContainingIgnoreCase(String partialDetails);
    List<Orders> findAllByOrderByDetailsAsc();
}
