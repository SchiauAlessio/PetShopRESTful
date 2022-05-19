package web.converter;

import web.dto.ClientDto;
import core.model.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientConverter extends BaseConverter<Integer, Client, ClientDto> {
    @Override
    public Client convertDtoToModel(ClientDto dto) {
        Client client = new Client();
        client.setId(dto.getId());
        client.setName(dto.getName());
        return client;
    }

    @Override
    public ClientDto convertModelToDto(Client client) {
        ClientDto clientDto = new ClientDto(client.getName());
        clientDto.setId(client.getId());
        return clientDto;
    }
}
