package core.service.impl;

import core.model.Client;
import core.model.exceptions.ServiceException;
import core.model.exceptions.ValidatorException;
import core.model.validators.ClientValidator;
import core.model.validators.Validator;
import core.repository.ClientRepository;
import core.service.IClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl implements IClientService {
    public static final Logger logger = LoggerFactory.getLogger(ClientServiceImpl.class);
    private final ClientRepository clientRepository;
    private final Validator<Client> clientValidator;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
        clientValidator = new ClientValidator();
    }

    @Override
    public List<Client> getAllClients() {
        logger.trace("Getting the list of all clients");
        List<Client> clients = clientRepository.findAll();
        logger.trace("Got the list of all clients");
        return clients;
    }

    @Override
    public List<Client> getFilteredClients(String partialName) {
        logger.trace("Getting the list of clients containing " + partialName);
        List<Client> clients = clientRepository.findAllByNameContainingIgnoreCase(partialName);
        logger.trace("Got the list of filtered clients");
        return clients;
    }

    @Override
    public List<Client> getSortedClients() {
        logger.trace("Getting the list of clients sorted by name");
        List<Client> clients = clientRepository.findAllByOrderByNameAsc();
        logger.trace("Got the list of sorted clients");
        return clients;
    }

    @Override
    public Client addClient(Client client) throws ValidatorException {
        clientValidator.validate(client);
        logger.trace("Adding client " + client);
        clientRepository.save(client);
        logger.trace("Added client " + client);
        return client;
    }

    @Override
    public Client updateClient(Client client) throws ValidatorException {
        clientValidator.validate(client);
        logger.trace("Updating client with id " + client.getId());
        clientRepository.findById(client.getId()).ifPresentOrElse(
                c -> clientRepository.save(client),
                () -> {
                    String error = "Client with id " + client.getId() + " was not found!";
                    logger.trace(error);
                    throw new ServiceException(error);
                }

        );
        logger.trace("Updated client: " + client);
        return client;
    }

    @Override
    public void deleteClient(Integer id) {
        logger.trace("Deleting client with id " + id);
        clientRepository.findById(id).ifPresentOrElse(
                clientRepository::delete,
                () -> {
                    String error = "Client with id " + id + " was not found!";
                    logger.trace("Client with id " + id + " was not found!");
                    throw new ServiceException(error);
                }
        );
        logger.trace("Deleted client with id " + id);
    }
}
