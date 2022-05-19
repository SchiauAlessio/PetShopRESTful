package core.model.validators;

import core.model.exceptions.ValidatorException;
import core.model.Client;

public class ClientValidator implements Validator<Client> {
    @Override
    public void validate(Client entity) throws ValidatorException {
        if(entity.getName().isEmpty()){
            throw new ValidatorException("Client name shouldn't be an empty string");
        }
    }
}
