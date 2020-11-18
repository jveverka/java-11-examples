package itx.examples.redis.services;

import itx.examples.redis.dto.UserData;

import java.util.Collection;
import java.util.Optional;

/**
 * Synchronous data service implementation using transactions.
 * Each method call runs in own transaction.
 */
public interface DataService {

    Optional<UserData> findById(String id) throws DataServiceException;

    UserData save(String email, String password) throws DataServiceException;

    Collection<UserData> findAll() throws DataServiceException;

    void deleteById(String id) throws DataServiceException;

    void update(UserData userData) throws DataServiceException;

}
