package core.model.validators;

import core.model.Product;
import core.model.exceptions.ValidatorException;

public class ProductValidator implements Validator<Product> {
    @Override
    public void validate(Product entity) throws ValidatorException {

        if (entity.getPrice() < 0)
            throw new ValidatorException("Product price should be a positive double not " + entity.getPrice());
        if (entity.getName().isEmpty())
            throw new ValidatorException("Product name shouldn't be an empty string");
    }
}
