package itx.examples.jdbc.services;

import itx.examples.jdbc.dto.UserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public class DataServiceImpl implements DataService {

    private static final Logger LOG = LoggerFactory.getLogger(DataServiceImpl.class);

    private final Connection connection;

    public DataServiceImpl(Connection connection) {
        this.connection = connection;
        disableAutocommit();
    }

    @Override
    public Optional<UserData> findById(String id) throws DataServiceException {
        try {
            String query = "SELECT * FROM users WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String userId = resultSet.getString("id");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                return Optional.of(new UserData(userId, email, password));
            }
            this.connection.commit();
            return Optional.empty();
        } catch (SQLException e) {
            transactionRollback();
            throw new DataServiceException(e);
        }
    }

    @Override
    public UserData save(String email, String password) throws DataServiceException {
        try {
            String query = "INSERT INTO users ( id, email, password ) VALUES ( ?, ?, ? )";
            String id = UUID.randomUUID().toString();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, id);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.execute();
            this.connection.commit();
            return new UserData(id, email, password);
        } catch (SQLException e) {
            transactionRollback();
            throw new DataServiceException(e);
        }
    }

    @Override
    public Collection<UserData> findAll() throws DataServiceException {
        Collection<UserData> userDataCollection = new ArrayList<>();
        try {
            String query = "SELECT * FROM users";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                userDataCollection.add(new UserData(id, email, password));
            }
            this.connection.commit();
        } catch (SQLException e) {
            transactionRollback();
            throw new DataServiceException(e);
        }
        return userDataCollection;
    }

    @Override
    public void deleteById(String id) throws DataServiceException {
        try {
            if (findById(id).isEmpty()) {
                throw new DataServiceException();
            }
            String query = "DELETE FROM users WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, id);
            statement.execute();
            this.connection.commit();
        } catch (SQLException e) {
            transactionRollback();
            throw new DataServiceException(e);
        }
    }

    @Override
    public void update(UserData userData) throws DataServiceException {
        try {
            if (findById(userData.getId()).isEmpty()) {
                throw new DataServiceException();
            }
            String query = "UPDATE users SET email=?, password=? WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, userData.getEmail());
            statement.setString(2, userData.getPassword());
            statement.setString(3, userData.getId());
            statement.execute();
            this.connection.commit();
        } catch (SQLException e) {
            transactionRollback();
            throw new DataServiceException(e);
        }
    }

    private void transactionRollback() {
        try {
            this.connection.rollback();
        } catch (SQLException e) {
            LOG.error("Transaction rollback failed", e);
        }
    }

    private void disableAutocommit() {
        try {
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            LOG.error("Autocommit = false failed", e);
        }
    }

}
