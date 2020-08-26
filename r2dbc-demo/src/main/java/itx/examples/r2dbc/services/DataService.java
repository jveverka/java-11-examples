package itx.examples.r2dbc.services;

import itx.examples.r2dbc.dto.UserData;

import java.util.Collection;
import java.util.Optional;

public interface DataService extends AutoCloseable {

    Optional<UserData> findById(String id);

    UserData save(String email, String password);

    Collection<UserData> findAll();

    boolean deleteById(String id);

    boolean update(UserData userData);

}
