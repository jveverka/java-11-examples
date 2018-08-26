package itx.examples.mongodb.services;

import itx.examples.mongodb.dto.Role;

import java.util.Collection;

public interface RoleService {

    Collection<Role> getRoles();

    void insertRole(Role role) throws DataException;

    void removeRole(String id) throws DataException;

    void removeAll() throws DataException;

}
