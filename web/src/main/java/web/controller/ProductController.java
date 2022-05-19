package web.controller;

import core.model.exceptions.ValidatorException;
import web.converter.ProductConverter;
import web.dto.ProductDto;
import web.dto.ProductsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import core.service.IProductService;

@RestController
public class ProductController {
    private final IProductService productService;
    private final ProductConverter productConverter;

    @Autowired
    public ProductController(IProductService productService, ProductConverter productConverter) {
        this.productService = productService;
        this.productConverter = productConverter;
    }

    @RequestMapping(value = "/products", method = RequestMethod.GET)
    ProductsDto getAllProducts() {
        return new ProductsDto(productConverter.convertModelsToDtos(productService.getAllProducts()));
    }

    @RequestMapping(value = "/products/filter/{maxPrice}", method = RequestMethod.GET)
    ProductsDto getFilteredProducts(@PathVariable double maxPrice) {
        return new ProductsDto(productConverter.convertModelsToDtos(productService.getFilteredProducts(maxPrice)));
    }

    @RequestMapping(value = "/products/sorted", method = RequestMethod.GET)
    ProductsDto getSortedProducts() {
        return new ProductsDto(productConverter.convertModelsToDtos(productService.getSortedProducts()));
    }

    @RequestMapping(value = "/products/by-order/{orderId}", method = RequestMethod.GET)
    ProductsDto getOrdersByProduct(@PathVariable Integer orderId) {
        return new ProductsDto(productConverter.convertModelsToDtos(productService.getProductsByOrder(orderId)));
    }

    @RequestMapping(value = "/products/add", method = RequestMethod.POST)
    ResponseEntity<?> addProduct(@RequestBody ProductDto productDto) {
        try {
            productService.addProduct(productConverter.convertDtoToModel(productDto));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ValidatorException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/products/update", method = RequestMethod.PUT)
    ResponseEntity<?> updateProduct(@RequestBody ProductDto productDto) {
        try {
            productService.updateProduct(productConverter.convertDtoToModel(productDto));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ValidatorException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @RequestMapping(value = "/products/delete/{id}", method = RequestMethod.DELETE)
    ResponseEntity<?> deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
