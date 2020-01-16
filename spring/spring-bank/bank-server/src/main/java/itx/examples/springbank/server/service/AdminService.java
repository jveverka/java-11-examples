package itx.examples.springbank.server.service;

import itx.examples.springbank.common.dto.Client;
import itx.examples.springbank.common.dto.CreateClientRequest;
import itx.examples.springbank.common.dto.ClientId;
import itx.examples.springbank.common.dto.ServiceException;

import java.util.Collection;

public interface AdminService {

    ClientId createClient(CreateClientRequest createClientRequest) throws ServiceException;

    Collection<Client> getClients();

}
