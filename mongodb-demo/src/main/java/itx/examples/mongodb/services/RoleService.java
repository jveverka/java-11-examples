package itx.examples.mongodb.services;

import itx.examples.mongodb.dto.Role;

import java.util.Collection;

/**
 * Service providing data access for Roles.
 */
public interface RoleService {

    /**
     * Return all available roles in the database.
     * @return
     */
    Collection<Role> getAll();

    /**
     * Insert Role into database.
     * @param role
     * @throws DataException
     */
    void insert(Role role) throws DataException;

    /**
     * Update Role
     * @param role
     * @throws DataException
     */
    void update(Role role) throws DataException;

    /**
     * Get Role by ID.
     * @param id
     * @return
     */
    Role get(String id);

    /**
     * Remove Role from database.
     * @param id
     * @throws DataException
     */
    void remove(String id) throws DataException;

    /**
     * Remove all roles from database.
     * @throws DataException
     */
    void removeAll() throws DataException;

}
