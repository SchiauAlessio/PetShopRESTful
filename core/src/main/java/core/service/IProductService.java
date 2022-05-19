package core.service;

import core.model.Product;
import core.model.exceptions.ValidatorException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IProductService {
    List<Product> getAllProducts();
    List<Product> getProductsByOrder(Integer orderId);
    Product addProduct(Product product) throws ValidatorException;

    @Transactional
    Product updateProduct(Product product) throws ValidatorException;

    @Transactional
    void deleteProduct(Integer id);

    List<Product> getFilteredProducts(double maxPrice);

    List<Product> getSortedProducts();
}
