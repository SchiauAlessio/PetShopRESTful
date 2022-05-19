package core.model.validators;


import core.model.Orders;
import core.model.exceptions.ValidatorException;

public class OrderValidator implements Validator<Orders> {
    @Override
    public void validate(Orders order) throws ValidatorException {

        if (order.getDetails().isEmpty())
            throw new ValidatorException("Missing details.");
    }
}