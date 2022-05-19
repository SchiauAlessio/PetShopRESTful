package core.service.impl;

import core.model.Product;
import core.model.exceptions.ServiceException;
import core.model.exceptions.ValidatorException;
import core.model.validators.ProductValidator;
import core.model.validators.Validator;
import core.repository.OrderProductRepository;
import core.repository.ProductRepository;
import core.service.IProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements IProductService {
    public static final Logger logger = LoggerFactory.getLogger(ClientServiceImpl.class);
    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;
    private final Validator<Product> productValidator;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, OrderProductRepository orderProductRepository) {
        this.productRepository = productRepository;
        this.orderProductRepository = orderProductRepository;
        productValidator = new ProductValidator();
    }

    @Override
    public List<Product> getAllProducts() {
        logger.trace("Getting the list of all products");
        List<Product> products = productRepository.findAll();
        logger.trace("Got the list of all products");
        return products;
    }

    @Override
    public List<Product> getFilteredProducts(double maxPrice) {
        logger.trace("Getting the list of  products with price <= " + maxPrice);
        List<Product> products = productRepository.findAllByPriceIsLessThanEqual(maxPrice);
        logger.trace("Got the list of filtered products");
        return products;
    }

    @Override
    public List<Product> getSortedProducts() {
        logger.trace("Getting the list of products sorted by price");
        List<Product> products = productRepository.findAllByOrderByPriceDesc();
        logger.trace("Got the list of sorted products");
        return products;
    }

    @Override
    public List<Product> getProductsByOrder(Integer orderId) {
        logger.trace("Getting the list of all products for order with id " + orderId);
        List<Integer> productIds = orderProductRepository.findAllByOrder_Id(orderId)
                .stream().map(op -> op.getId().getProductId()).collect(Collectors.toList());
        List<Product> products = productRepository.findAll()
                .stream().filter(p -> productIds.contains(p.getId())).collect(Collectors.toList());
        logger.trace("Got the list of all products for order with id " + orderId);
        return products;
    }

    @Override
    public Product addProduct(Product product) throws ValidatorException {
        productValidator.validate(product);
        logger.trace("Adding product " + product);
        productRepository.save(product);
        logger.trace("Added product " + product);
        return product;
    }

    @Override
    public Product updateProduct(Product product) throws ValidatorException {
        productValidator.validate(product);
        logger.trace("Updating product with id " + product.getId());
        productRepository.findById(product.getId()).ifPresentOrElse(
                p -> productRepository.save(product),
                () -> {
                    throw new ServiceException("Product was not found!");
                }
        );
        logger.trace("Updated product: " + product);
        return product;
    }

    @Override
    public void deleteProduct(Integer id) {
        logger.trace("Deleting product with id " + id);
        productRepository.findById(id).ifPresentOrElse(
                productRepository::delete,
                () -> {
                    throw new ServiceException("Product was not found!");
                }
        );
        logger.trace("Deleted product with id " + id);
    }
}
