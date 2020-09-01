package itx.examples.r2dbc.services;

import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.Result;
import io.r2dbc.spi.Statement;
import itx.examples.r2dbc.dto.UserData;
import itx.examples.r2dbc.services.subscribers.ResultSubscriber;
import itx.examples.r2dbc.services.subscribers.RowsUpdatedSubscriber;
import itx.examples.r2dbc.services.subscribers.UserDataListSubscriber;
import itx.examples.r2dbc.services.subscribers.UserDataSubscriber;
import itx.examples.r2dbc.services.subscribers.VoidSubscriber;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Future;

public class DataServiceImpl implements DataService {

    private static final Logger LOG = LoggerFactory.getLogger(DataServiceImpl.class);

    private final ConnectionFactory factory;

    public DataServiceImpl(ConnectionFactory factory) {
        this.factory = factory;
    }

    @Override
    public Optional<UserData> findById(String id) throws DataServiceException {
        LOG.info("findById: id={}", id);
        Connection connection = DBUtils.getConnection(factory);
        try {
            ResultSubscriber resultSubscriber = new ResultSubscriber();
            VoidSubscriber voidSubscriber = new VoidSubscriber();
            connection.beginTransaction().subscribe(voidSubscriber);
            voidSubscriber.await();
            Statement statement = connection.createStatement("SELECT * FROM users WHERE id=$1");
            statement.bind(0, id);
            statement.execute().subscribe(resultSubscriber);
            voidSubscriber = new VoidSubscriber();
            connection.commitTransaction().subscribe(voidSubscriber);
            voidSubscriber.await();
            Future<Result> futureResult = resultSubscriber.getResult();
            Result result = futureResult.get();
            Publisher<UserData> results = result.map((row, rowMetadata) -> {
                String idUser = row.get("id", String.class);
                String email = row.get("email", String.class);
                String password = row.get("password", String.class);
                LOG.info("findById: user found: id={} email={}", idUser, email);
                return new UserData(idUser, email, password);
            });
            UserDataSubscriber userDataSubscriber = new UserDataSubscriber();
            results.subscribe(userDataSubscriber);
            UserData userData = userDataSubscriber.getResult().get();
            return Optional.of(userData);
        } catch (Exception e) {
            connection.rollbackTransaction();
            return Optional.empty();
        } finally {
            connection.close();
        }
    }

    @Override
    public UserData save(String email, String password) throws DataServiceException {
        Connection connection = DBUtils.getConnection(factory);
        try {
            VoidSubscriber voidSubscriber = new VoidSubscriber();
            connection.beginTransaction().subscribe(voidSubscriber);
            voidSubscriber.await();
            String id = UUID.randomUUID().toString();
            ResultSubscriber resultSubscriber = new ResultSubscriber();
            Statement statement = connection.createStatement("INSERT INTO users ( id, email, password ) VALUES ( $1, $2, $3 )");
            statement.bind(0, id).bind(1, email).bind(2, password).execute().subscribe(resultSubscriber);
            Result result = resultSubscriber.getResult().get();
            RowsUpdatedSubscriber rowsUpdatedSubscriber = new RowsUpdatedSubscriber();
            result.getRowsUpdated().subscribe(rowsUpdatedSubscriber);
            Integer integer = rowsUpdatedSubscriber.getResult().get();
            LOG.info("save: rows updated: {}", integer);
            voidSubscriber = new VoidSubscriber();
            connection.commitTransaction().subscribe(voidSubscriber);
            voidSubscriber.await();
            LOG.info("user created: id={} email={}", id, email);
            return new UserData(id, email, password);
        } catch (Exception e) {
            connection.rollbackTransaction();
            throw new DataServiceException(e);
        } finally {
            connection.close();
        }
    }

    @Override
    public Collection<UserData> findAll() throws DataServiceException {
        LOG.info("findAll");
        Collection<UserData> userDataList = new ArrayList<>();
        Connection connection = DBUtils.getConnection(factory);
        try {
            VoidSubscriber voidSubscriber = new VoidSubscriber();
            connection.beginTransaction().subscribe(voidSubscriber);
            voidSubscriber.await();
            ResultSubscriber resultSubscriber = new ResultSubscriber();
            voidSubscriber = new VoidSubscriber();
            Statement statement = connection.createStatement("SELECT * FROM users");
            statement.execute().subscribe(resultSubscriber);
            Future<Result> futureResult = resultSubscriber.getResult();
            connection.commitTransaction().subscribe(voidSubscriber);
            voidSubscriber.await();
            Result result = futureResult.get();
            Publisher<UserData> results = result.map((row, rowMetadata) -> {
                String idUser = row.get("id", String.class);
                String email = row.get("email", String.class);
                String password = row.get("password", String.class);
                LOG.info("findAll: user found: id={} email={}", idUser, email);
                return new UserData(idUser, email, password);
            });
            UserDataListSubscriber userDataListSubscriber = new UserDataListSubscriber();
            results.subscribe(userDataListSubscriber);
            userDataList = userDataListSubscriber.waitAndGetResult();
        } catch (Throwable e) {
            connection.rollbackTransaction();
            throw new DataServiceException(e);
        } finally {
            connection.close();
        }
        return userDataList;
    }

    @Override
    public void deleteById(String id) throws DataServiceException {
        LOG.info("deleteById: id={}", id);
        Connection connection = DBUtils.getConnection(factory);
        try {
            VoidSubscriber voidSubscriber = new VoidSubscriber();
            connection.beginTransaction().subscribe(voidSubscriber);
            voidSubscriber.await();
            ResultSubscriber resultSubscriber = new ResultSubscriber();
            voidSubscriber = new VoidSubscriber();
            Statement statement = connection.createStatement("DELETE FROM users WHERE id=$1");
            statement.bind(0, id);
            statement.execute().subscribe(resultSubscriber);
            RowsUpdatedSubscriber rowsUpdatedSubscriber = new RowsUpdatedSubscriber();
            resultSubscriber.getResult().get().getRowsUpdated().subscribe(rowsUpdatedSubscriber);
            Integer integer = rowsUpdatedSubscriber.getResult().get();
            LOG.info("delete: rows updated: {}", integer);
            connection.commitTransaction().subscribe(voidSubscriber);
            voidSubscriber.await();
            if (integer == 0) {
                throw new DataServiceException();
            }
        } catch (Exception e) {
            connection.rollbackTransaction();
            throw new DataServiceException(e);
        } finally {
            connection.close();
        }
    }

    @Override
    public void update(UserData userData) throws DataServiceException {
        LOG.info("update: id={}", userData.getId());
        Connection connection = DBUtils.getConnection(factory);
        try {
            VoidSubscriber voidSubscriber = new VoidSubscriber();
            connection.beginTransaction().subscribe(voidSubscriber);
            voidSubscriber.await();
            ResultSubscriber resultSubscriber = new ResultSubscriber();
            voidSubscriber = new VoidSubscriber();
            Statement statement = connection.createStatement("UPDATE users SET email=$1, password=$2 WHERE id=$3");
            statement.bind(0, userData.getEmail());
            statement.bind(1, userData.getPassword());
            statement.bind(2, userData.getId());
            statement.execute().subscribe(resultSubscriber);
            RowsUpdatedSubscriber rowsUpdatedSubscriber = new RowsUpdatedSubscriber();
            resultSubscriber.getResult().get().getRowsUpdated().subscribe(rowsUpdatedSubscriber);
            Integer integer = rowsUpdatedSubscriber.getResult().get();
            LOG.info("update: rows updated: {}", integer);
            connection.commitTransaction().subscribe(voidSubscriber);
            voidSubscriber.await();
            if (integer == 0) {
                throw new DataServiceException();
            }
        } catch (Exception e) {
            connection.rollbackTransaction();
            throw new DataServiceException(e);
        } finally {
            connection.close();
        }
    }

}
