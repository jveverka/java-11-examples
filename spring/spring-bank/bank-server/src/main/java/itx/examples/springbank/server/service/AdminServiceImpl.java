package itx.examples.springbank.server.service;

import itx.examples.springbank.common.dto.Account;
import itx.examples.springbank.common.dto.Client;
import itx.examples.springbank.common.dto.CreateClientRequest;
import itx.examples.springbank.common.dto.ClientId;
import itx.examples.springbank.common.dto.ServiceException;
import itx.examples.springbank.server.repository.ClientRepository;
import itx.examples.springbank.server.repository.model.AccountEntity;
import itx.examples.springbank.server.repository.model.ClientEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    @Transactional
    public ClientId createClient(CreateClientRequest createClientRequest) throws ServiceException {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setCredit(0L);
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setFirstName(createClientRequest.getFirstName());
        clientEntity.setLastName(createClientRequest.getLastName());
        clientEntity.setAccount(accountEntity);
        ClientEntity savedEntity = clientRepository.save(clientEntity);
        return new ClientId(savedEntity.getId().toString());
    }

    @Override
    @Transactional
    public Collection<Client> getClients() {
        List<Client> clients = new ArrayList<>();
        List<ClientEntity> allClientEntities = clientRepository.findAll();
        allClientEntities.forEach(c -> {
            AccountEntity accountEntity = c.getAccount();
            Account account = new Account(accountEntity.getCredit());
            ClientId id = new ClientId(c.getId().toString());
            Client client = new Client(id, c.getFirstName(), c.getLastName(), account);
            clients.add(client);
        });
        return clients;
    }

    @Override
    @Transactional
    public void deleteClient(ClientId id) {
        Long clientId = Long.parseLong(id.getId());
        clientRepository.deleteById(clientId);
    }

}
