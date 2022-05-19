package client.services;

import core.service.impl.OrderProductServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.converter.ClientConverter;
import web.dto.ClientDto;
import web.dto.ClientsDto;
import core.model.Client;
import core.model.exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Service
public class ClientService {
    private final RestTemplate restTemplate;
    private final ExecutorService executorService;
    private final ClientConverter clientConverter;
    public static final Logger logger= LoggerFactory.getLogger(ClientService.class);

    @Autowired
    public ClientService(RestTemplate restTemplate, ExecutorService executorService, ClientConverter clientConverter) {
        this.restTemplate = restTemplate;
        this.executorService = executorService;
        this.clientConverter = clientConverter;
    }

    public CompletableFuture<Iterable<Client>> getAllClients() {
        logger.trace("getAllClients() method called");
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = "http://localhost:8080/api/clients";
                ClientsDto clientDtos = restTemplate.getForObject(url, ClientsDto.class);
                if (clientDtos == null)
                    throw new ServiceException("Could not get clients!");
                return clientConverter.convertDtosToModels(clientDtos.getClients());
            } catch (ResourceAccessException e) {
                throw new ServiceException(e.getMessage());
            }
        }, executorService);
    }

    public CompletableFuture<Iterable<Client>> getFilteredClients(String partialName) {
        logger.trace("getFilteredClients() method called");
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = "http://localhost:8080/api/clients/filter/{partialName}";
                ClientsDto clientDtos = restTemplate.getForObject(url, ClientsDto.class, partialName);
                if (clientDtos == null)
                    throw new ServiceException("Could not get clients!");
                return clientConverter.convertDtosToModels(clientDtos.getClients());
            } catch (ResourceAccessException e) {
                throw new ServiceException(e.getMessage());
            }
        }, executorService);
    }

    public CompletableFuture<Iterable<Client>> getSortedClients() {
        logger.trace("getSortedClients() method called");
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = "http://localhost:8080/api/clients/sorted";
                ClientsDto clientDtos = restTemplate.getForObject(url, ClientsDto.class);
                if (clientDtos == null)
                    throw new ServiceException("Could not get clients!");
                return clientConverter.convertDtosToModels(clientDtos.getClients());
            } catch (ResourceAccessException e) {
                throw new ServiceException(e.getMessage());
            }
        }, executorService);
    }

    public CompletableFuture<String> addClient(String name) {
        logger.trace("addClient() method called");
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = "http://localhost:8080/api/clients/add";
                ClientDto clientDto = new ClientDto(name);
                restTemplate.postForObject(url, clientDto, ClientDto.class);
                return "Added client";
            } catch (ResourceAccessException e) {
                throw new ServiceException("Could not add client!");
            }
        }, executorService);
    }

    public CompletableFuture<String> updateClient(Integer id, String name) {
        logger.trace("updateClient() method called");
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = "http://localhost:8080/api/clients/update";
                ClientDto clientDto = new ClientDto(name);
                clientDto.setId(id);
                restTemplate.put(url, clientDto);
                return "Updated client";
            } catch (ResourceAccessException e) {
                throw new ServiceException("Could not update client!");
            }
        }, executorService);
    }

    public CompletableFuture<String> deleteClient(Integer id) {
        logger.trace("deleteClient() method called");
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = "http://localhost:8080/api/clients/delete/{id}";
                restTemplate.delete(url, id);
                return "Deleted client";
            } catch (ResourceAccessException e) {
                throw new ServiceException("Could not delete client!");
            }
        }, executorService);
    }
}
