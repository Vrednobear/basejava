package com.urise.webapp.sql;

import com.urise.webapp.exception.StorageException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
             PreparedStatement ps = connection.prepareStatement(sqlQuery, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            T value = executor.execute(connection, ps);
            return value;
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    public <T> T transactionalExecute(SqlTransaction<T> executor) {
        try (Connection connection = connectionFactory.getConnection()) {
            try {
                connection.setAutoCommit(false);
                T value = executor.execute(connection);
                connection.commit();
                return value;
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new StorageException(e);
        }
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
