package itx.examples.jdbc.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class DBUtils {

    private DBUtils() {
    }

    public static void initDb(Connection connection) throws DataServiceException {
        try {
            connection.setAutoCommit(false);
            String query = "CREATE TABLE users ( id varchar(255), email varchar(255), password varchar(255), PRIMARY KEY ( id ) )";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            connection.commit();
        } catch (SQLException e) {
            throw new DataServiceException(e);
        }
    }

}
