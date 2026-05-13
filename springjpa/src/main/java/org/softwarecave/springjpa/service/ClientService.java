package org.softwarecave.springjpa.service;

import org.softwarecave.springjpa.model.Client;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ClientService {

    private ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client addClient(Client client) {
        if (client.getId() != null) {
            throw new DataValidationException("New client must have null key");
        }
        return clientRepository.save(client);
    }

    public List<Client> getClients() {
        return clientRepository.findAll(Sort.by(Sort.Order.asc("firstName")));
    }
}
