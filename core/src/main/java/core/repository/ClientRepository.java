package core.repository;

import core.model.Client;

import java.util.List;

public interface ClientRepository extends IRepository<Client, Integer> {
    List<Client> findAllByNameContainingIgnoreCase(String partialName);
    List<Client> findAllByOrderByNameAsc();
}
