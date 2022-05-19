package web.converter;

import web.dto.ProductDto;
import core.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductConverter extends BaseConverter<Integer, Product, ProductDto> {
    @Override
    public Product convertDtoToModel(ProductDto dto) {
        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        return product;
    }

    @Override
    public ProductDto convertModelToDto(Product product) {
        ProductDto productDto = new ProductDto(product.getName(), product.getPrice());
        productDto.setId(product.getId());
        return productDto;
    }
}
