package itx.examples.r2dbc.services;

import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactory;
import itx.examples.r2dbc.services.subscribers.ConnectionSubscriber;
import itx.examples.r2dbc.services.subscribers.ResultSubscriber;

import java.util.concurrent.Future;

public final class DBUtils {

    private DBUtils() {
    }

    public static Connection getConnection(ConnectionFactory factory) throws DataServiceException {
        try {
            ConnectionSubscriber connectionSubscriber = new ConnectionSubscriber();
            factory.create().subscribe(connectionSubscriber);
            Future<Connection> result = connectionSubscriber.getResult();
            return result.get();
        } catch (Exception e) {
            throw new DataServiceException(e);
        }
    }

    public static void initDb(ConnectionFactory factory) throws DataServiceException {
        Connection connection = getConnection(factory);
        try {
            connection.beginTransaction();
            ResultSubscriber resultSubscriber = new ResultSubscriber();
            connection.createStatement("CREATE TABLE users ( id varchar(255), email varchar(255), password varchar(255), PRIMARY KEY ( id ) )")
                    .execute().subscribe(resultSubscriber);
            resultSubscriber.getResult().get();
            connection.commitTransaction();
        } catch (Exception e) {
            throw new DataServiceException(e);
        } finally {
            connection.close();
        }
    }

}
