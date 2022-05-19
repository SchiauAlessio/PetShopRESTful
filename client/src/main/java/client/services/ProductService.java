package client.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.converter.ProductConverter;
import web.dto.ProductDto;
import web.dto.ProductsDto;
import core.model.Product;
import core.model.exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Service
public class ProductService {
    private final RestTemplate restTemplate;
    private final ExecutorService executorService;
    private final ProductConverter productConverter;

    private static final Logger logger= LoggerFactory.getLogger(ProductService.class);

    @Autowired
    public ProductService(RestTemplate restTemplate, ExecutorService executorService, ProductConverter productConverter) {
        this.restTemplate = restTemplate;
        this.executorService = executorService;
        this.productConverter = productConverter;
    }

    public CompletableFuture<Iterable<Product>> getAllProducts() {
        logger.trace("getAllProducts() method entered");
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = "http://localhost:8080/api/products";
                ProductsDto productsDto = restTemplate.getForObject(url, ProductsDto.class);
                if (productsDto == null)
                    throw new ServiceException("Could not get products!");
                return productConverter.convertDtosToModels(productsDto.getProducts());
            } catch (ResourceAccessException e) {
                throw new ServiceException(e.getMessage());
            }
        }, executorService);
    }

    public CompletableFuture<Iterable<Product>> getFilteredProducts(double maxPrice) {
        logger.trace("getFilteredProducts() method entered");
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = "http://localhost:8080/api/products/filter/{maxPrice}";
                ProductsDto productsDto = restTemplate.getForObject(url, ProductsDto.class, maxPrice);
                if (productsDto == null)
                    throw new ServiceException("Could not get products!");
                return productConverter.convertDtosToModels(productsDto.getProducts());
            } catch (ResourceAccessException e) {
                throw new ServiceException(e.getMessage());
            }
        }, executorService);
    }

    public CompletableFuture<Iterable<Product>> getSortedProducts() {
        logger.trace("getSortedProducts() method entered");
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = "http://localhost:8080/api/products/sorted";
                ProductsDto productsDto = restTemplate.getForObject(url, ProductsDto.class);
                if (productsDto == null)
                    throw new ServiceException("Could not get products!");
                return productConverter.convertDtosToModels(productsDto.getProducts());
            } catch (ResourceAccessException e) {
                throw new ServiceException(e.getMessage());
            }
        }, executorService);
    }

    public CompletableFuture<Iterable<Product>> getProductByOrder(Integer orderId) {
        logger.trace("getProductByOrder() method entered");
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = "http://localhost:8080/api/products/by-order/{orderId}";
                ProductsDto productsDto = restTemplate.getForObject(url, ProductsDto.class, orderId);
                if (productsDto == null)
                    throw new ServiceException("Could not get products!");
                return productConverter.convertDtosToModels(productsDto.getProducts());
            } catch (ResourceAccessException e) {
                throw new ServiceException(e.getMessage());
            }
        }, executorService);
    }

    public CompletableFuture<String> addProduct(String details, double price) {
        logger.trace("addProduct() method entered");
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = "http://localhost:8080/api/products/add";
                ProductDto productDto = new ProductDto(details, price);
                restTemplate.postForObject(url, productDto, ProductDto.class);
                return "Added product";
            } catch (ResourceAccessException e) {
                throw new ServiceException("Could not add product!");
            }
        }, executorService);
    }

    public CompletableFuture<String> updateProduct(Integer id, String details, double price) {
        logger.trace("updateProduct() method entered");
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = "http://localhost:8080/api/products/update";
                ProductDto productDto = new ProductDto(details, price);
                productDto.setId(id);
                restTemplate.put(url, productDto);
                return "Updated product";
            } catch (ResourceAccessException e) {
                throw new ServiceException("Could not update product!");
            }
        }, executorService);
    }

    public CompletableFuture<String> deleteProduct(Integer id) {
        logger.trace("deleteProduct() method entered");
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = "http://localhost:8080/api/products/delete/{id}";
                restTemplate.delete(url, id);
                return "Deleted product";
            } catch (ResourceAccessException e) {
                throw new ServiceException("Could not delete product!");
            }
        }, executorService);
    }
}
