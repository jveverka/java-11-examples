package itx.examples.springbank.server.service;

import itx.examples.springbank.common.dto.Client;
import itx.examples.springbank.common.dto.CreateClientRequest;
import itx.examples.springbank.common.dto.ClientId;
import itx.examples.springbank.common.dto.ServiceException;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class AdminServiceImpl implements AdminService {

    @Override
    public ClientId createClient(CreateClientRequest createClientRequest) throws ServiceException {
        return null;
    }

    @Override
    public Collection<Client> getClients() {
        return null;
    }

    @Override
    public void deleteClient(ClientId id) {

    }

}
