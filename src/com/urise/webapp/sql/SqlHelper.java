package com.urise.webapp.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {

   private ConnectionFactory connectionFactory;

    public SqlHelper(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }


    public void execute(String sqlQuery) {
        execute(sqlQuery, (connection, ps) -> {
            ps.execute();
            return null;
        });
    }

    public <T> T execute(String sqlQuery, SqlExecutor<T> executor) {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
            T value = executor.execute(connection, ps);
            return value;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }
}


//    public T execute(ConnectionFactory connectionFactory, String sqlQuery, SqlExecutor<T> executor) {
//        try (Connection connection = connectionFactory.getConnection();
//             PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
//            T value = executor.execute(connection, ps);
//            return value;
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//        return null;
//    }
//}
