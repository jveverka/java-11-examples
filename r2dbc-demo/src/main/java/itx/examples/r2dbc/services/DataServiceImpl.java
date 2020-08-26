package itx.examples.r2dbc.services;

import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactory;
import itx.examples.r2dbc.dto.UserData;
import org.reactivestreams.Publisher;

import java.util.Collection;
import java.util.Optional;

public class DataServiceImpl implements DataService {

    private final Publisher<? extends Connection> connection;

    public DataServiceImpl(ConnectionFactory factory) {
        this.connection = factory.create();
    }

    @Override
    public Optional<UserData> findById(String id) {
        return Optional.empty();
    }

    @Override
    public UserData save(String email, String password) {
        return null;
    }

    @Override
    public Collection<UserData> findAll() {
        return null;
    }

    @Override
    public boolean deleteById(String id) {
        return false;
    }

    @Override
    public boolean update(UserData userData) {
        return false;
    }

    @Override
    public void close() throws Exception {

    }

}
