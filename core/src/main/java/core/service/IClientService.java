package core.service;

import core.model.Client;
import core.model.exceptions.ValidatorException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IClientService {
    List<Client> getAllClients();

    List<Client> getFilteredClients(String partialName);

    List<Client> getSortedClients();

    Client addClient(Client client) throws ValidatorException;

    @Transactional
    Client updateClient(Client client) throws ValidatorException;

    @Transactional
    void deleteClient(Integer id);
}
