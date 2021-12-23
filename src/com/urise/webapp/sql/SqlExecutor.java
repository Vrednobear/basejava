package com.urise.webapp.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public interface SqlExecutor<T>{
    T execute(Connection connection, PreparedStatement ps) throws SQLException;
}

//public interface SqlExecutor<T>{
//    T execute(Connection connection, PreparedStatement ps) throws SQLException;
//}
