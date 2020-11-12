package itx.examples.mongodb.services;

import itx.examples.mongodb.dto.Address;

import java.util.Collection;

/**
 * Service providing data access for Addresses.
 */
public interface AddressService {

    /**
     * Return all available Addresses in the database.
     * @return
     */
    Collection<Address> getAll();

    /**
     * Insert Address into database.
     * @param address
     * @throws DataException
     */
    void insert(Address address) throws DataException;

    /**
     * Update Address
     * @param address
     * @throws DataException
     */
    void update(Address address) throws DataException;

    /**
     * Get Address by ID
     * @param id
     * @return
     * @throws DataException
     */
    Address get(String id) throws DataException;

    /**
     * Remove Address from database by id.
     * @param id
     * @throws DataException
     */
    void remove(String id) throws DataException;

    /**
     * Remove all Addresses from database.
     * @throws DataException
     */
    void removeAll() throws DataException;

}
