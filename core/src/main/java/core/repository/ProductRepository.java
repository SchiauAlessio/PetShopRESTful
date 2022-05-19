package core.repository;

import core.model.Product;

import java.util.List;

public interface ProductRepository extends IRepository<Product, Integer> {
    List<Product> findAllByPriceIsLessThanEqual(double maxPrice);
    List<Product> findAllByOrderByPriceDesc();
}
