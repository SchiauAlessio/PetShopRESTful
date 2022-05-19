package web.controller;

import core.model.exceptions.ValidatorException;
import web.converter.ClientConverter;
import web.dto.ClientDto;
import web.dto.ClientsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import core.service.IClientService;

@RestController
public class ClientController {
    private final IClientService clientService;
    private final ClientConverter clientConverter;

    @Autowired
    public ClientController(IClientService clientService, ClientConverter clientConverter) {
        this.clientService = clientService;
        this.clientConverter = clientConverter;
    }

    @RequestMapping(value = "/clients", method = RequestMethod.GET)
    ClientsDto getAllClients() {
        return new ClientsDto(clientConverter.convertModelsToDtos(clientService.getAllClients()));
    }

    @RequestMapping(value = "/clients/filter/{partialName}", method = RequestMethod.GET)
    ClientsDto getFilteredClients(@PathVariable String partialName) {
        return new ClientsDto(clientConverter.convertModelsToDtos(clientService.getFilteredClients(partialName)));
    }

    @RequestMapping(value = "/clients/sorted", method = RequestMethod.GET)
    ClientsDto getSortedClients() {
        return new ClientsDto(clientConverter.convertModelsToDtos(clientService.getSortedClients()));
    }

    @RequestMapping(value = "/clients/add", method = RequestMethod.POST)
    ResponseEntity<?> addClient(@RequestBody ClientDto clientDto) {
        try {
            clientService.addClient(clientConverter.convertDtoToModel(clientDto));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ValidatorException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/clients/update", method = RequestMethod.PUT)
    ResponseEntity<?> updateClient(@RequestBody ClientDto clientDto) {
        try {
            clientService.updateClient(clientConverter.convertDtoToModel(clientDto));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ValidatorException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/clients/delete/{id}", method = RequestMethod.DELETE)
    ResponseEntity<?> deleteClient(@PathVariable Integer id) {
        clientService.deleteClient(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
