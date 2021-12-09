package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.ContactType;
import com.urise.webapp.model.Resume;
import com.urise.webapp.sql.SqlHelper;

import java.sql.*;
import java.util.*;

public class SqlStorage implements Storage {
    SqlHelper helper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        helper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        helper.execute("DELETE FROM resume");
    }

    @Override
    public void update(Resume r) {
        helper.transactionalExecute((connection) -> {
            try (PreparedStatement ps = connection.prepareStatement("UPDATE resume SET full_name = ? where uuid = ?")) {
                ps.setString(1, r.getFullName());
                ps.setString(2, r.getUuid());
                if (!isExist(connection, r.getUuid())) {
                    throw new NotExistStorageException(r.getUuid());
                }
                ps.execute();
                return null;
            }
        });
        deleteContacts(r);
        insertContacts(r);
    }


    @Override
    public void save(Resume r) {

        helper.transactionalExecute(connection -> {
            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO resume (uuid, full_name) " +
                            "VALUES (?,?)")) {
                setResumePreparedStatement(ps, r);
                if (isExist(connection, r.getUuid())) {
                    throw new ExistStorageException(r.getUuid());
                }
                ps.execute();
            }
            return null;
        });
        insertContacts(r);
    }

    @Override
    public int size() {
        ///??? ? why I changed e.printStackTrce to throw and don't need return statement
        return helper.execute("SELECT count(*) FROM resume", (connection, ps) -> {
            ps.execute();
            ResultSet rs = ps.getResultSet();
            rs.next();
            return rs.getInt(1);
        });
    }

    @Override
    public Resume get(String uuid) {
        return helper.execute(
                "SELECT * FROM resume r " +
                        "LEFT JOIN contact c " +
                        "ON (r.uuid = c.resume_uuid) " +
                        "WHERE r.uuid = ?",
                (connection, ps) -> {
                    ps.setString(1, uuid);
                    ps.execute();
                    ResultSet rs = ps.getResultSet();
                    if (!rs.next()) {
                        throw new NotExistStorageException(uuid);
                    }
                    Resume resume = new Resume(uuid, rs.getString("full_name"));
                    while (rs.next()) {
                        addContact(rs, resume);
                    }
                    return resume;
                });
    }

    @Override
    public void delete(String uuid) {
        helper.execute("DELETE FROM resume WHERE uuid = ?", (connection, ps) -> {
            ps.setString(1, uuid);
            if (!isExist(connection, uuid)) {
                throw new NotExistStorageException(uuid);
            }
            ps.execute();
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
     return helper.execute(
                "SELECT * FROM resume " +
                        "LEFT JOIN contact с " +
                        "ON resume.uuid = с.resume_uuid " +
                        "ORDER BY full_name, uuid", (connection, ps) ->
                {
                    ResultSet set = ps.executeQuery();
                    Map<String, Resume> mapNew = new LinkedHashMap<>();
                    while (set.next()) {
                        String resumeUuid = set.getString("uuid");
                        String resumeFullName = set.getString("full_name");
                        Resume resume = mapNew.get(resumeUuid);
                        if (!mapNew.containsKey(resumeUuid)) {
                            resume = new Resume(resumeUuid,resumeFullName);
                            addContact(set,resume);
                            mapNew.put(resumeUuid, resume);
                        }else {
                            addContact(set, resume);
                            mapNew.put(resumeUuid, resume);
                        }
                    }
                    return new ArrayList<>(mapNew.values());
                });
    }


//        List<Resume> resumes = new ArrayList<>();
//        helper.execute(
//                "SELECT * FROM resume " +
//                        "ORDER BY full_name, uuid", (connection, ps) -> {
//                    ps.execute();
//                    ResultSet set = ps.getResultSet();
//                    while (set.next()) {
//                        resumes.add(new Resume(set.getString("uuid"), set.getString("full_name")));
//                    }
//                    return resumes;
//                });
//
//        Map<String, EnumMap> map = new HashMap<>();
//        helper.execute(
//                "SELECT * FROM contact " +
//                        "ORDER BY resume_uuid", (connection, ps) -> {
//                    ps.execute();
//                    ResultSet set = ps.getResultSet();
//                    EnumMap<ContactType, String> contactMap = new EnumMap<ContactType, String>(ContactType.class);
//                    String resumeUuid;
//
//                    while (set.next()) {
//                        resumeUuid = set.getString("resume_uuid");
//                        while (set.getString("resume_uuid").equals(resumeUuid)) {
//                            contactMap.put(ContactType.valueOf(set.getString("type")), set.getString("value"));
//                            boolean setCondition = set.next();
//                            if (!setCondition) {
//                                break;
//                            }
//                        }
//                        map.put(resumeUuid, new EnumMap(contactMap));
//                        contactMap.clear();
//                        set.previous();
//                    }
//                    return null;
//                });
//
//        List<Resume> resumesFinal = new ArrayList<>();
//        for (Resume resume :
//                resumes) {
//            for (Map.Entry<String, EnumMap> entry
//                    : map.entrySet()) {
//                if (resume.getUuid().equals(entry.getKey())) {
//                    resume.addContacts(entry.getValue());
//                    resumesFinal.add(resume);
//                }
//            }
//        }

//        return resumesFinal;
    public boolean isExist(Connection connection, String uuid) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT count(*) from resume where uuid = ?");
        preparedStatement.setString(1, uuid);
        preparedStatement.execute();
        ResultSet rs = preparedStatement.getResultSet();
        rs.next();
        return rs.getInt(1) > 0;
    }

    public void deleteContacts(Resume r) {
        helper.execute("DELETE from contact where resume_uuid = ?",
                (connection, ps) -> {
                    ps.setString(1, r.getUuid());
                    ps.execute();
                    return null;
                });
    }

    public void insertContacts(Resume r) {
        helper.transactionalExecute((connection) ->{
            try(PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO contact (type, value, resume_uuid) " +
                            "Values(?,?,?)")){
                for (Map.Entry<ContactType, String> entry :
                        r.getContacts().entrySet()) {
                    setContactsPreparedStatement(ps, r, entry);
                    ps.addBatch();
                }
                ps.executeBatch();
                return null;
            }
        });
    }

    private void addContact(ResultSet rs, Resume resume) throws SQLException {
        String value = rs.getString("value");
        if(value != null) {
            resume.addContact(ContactType.valueOf(rs.getString("type")), value);
        }
    }

    public void setResumePreparedStatement(PreparedStatement ps, Resume r) throws SQLException {
        ps.setString(1, r.getUuid());
        ps.setString(2, r.getFullName());
    }

    public void setContactsPreparedStatement(PreparedStatement ps, Resume
            r, Map.Entry<ContactType, String> entry) throws SQLException {
        ps.setString(1, entry.getKey().toString());
        ps.setString(2, entry.getValue());
        ps.setString(3, r.getUuid());
    }
}


//Version2
//-------------------------------------------------------------------------------------
// SqlHelper<Resume> helper = new SqlHelper();
//    public final ConnectionFactory connectionFactory;
//
//    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
//        connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
//    }
//
//
//    @Override
//    public void clear() {
//        helper.execute(connectionFactory, "DELETE  FROM resume", new SqlExecutor() {
//            @Override
//            public Object execute(Connection connection, PreparedStatement ps) throws SQLException {
//                ps.execute();
//                return null;
//            }
//        });
//    }
//
//    @Override
//    public void update(Resume r) {
//        helper.execute(connectionFactory, "UPDATE resume SET full_name = ? where uuid = ?", new SqlExecutor() {
//            @Override
//            public Object execute(Connection connection, PreparedStatement ps) throws SQLException {
//                ps.setString(1, r.getFullName());
//                ps.setString(2, r.getUuid());
//                if (!isExist(connection, r.getUuid())) {
//                    throw new NotExistStorageException(r.getUuid());
//                }
//                ps.execute();
//                return null;
//            }
//        });
//    }
//
//
//    @Override
//    public void save(Resume r) {
//        new SqlHelper().execute(connectionFactory,
//                "INSERT INTO resume (uuid, full_name) VALUES (?,?)", new SqlExecutor() {
//                    @Override
//                    public Object execute(Connection connection, PreparedStatement ps) throws SQLException {
//                        ps.setString(1, r.getUuid());
//                        ps.setString(2, r.getFullName());
//                        if (isExist(connection, r.getUuid())) {
//                            throw new ExistStorageException(r.getUuid());
//                        }
//                        ps.execute();
//                        return null;
//                    }
//                });
//    }
//
//
//    @Override
//    public Resume get(String uuid) {
//        return helper.execute(connectionFactory, "SELECT * FROM resume r WHERE r.uuid = ?", new SqlExecutor<Resume>() {
//            @Override
//            public Resume execute(Connection connection, PreparedStatement ps) throws SQLException {
//                ps.setString(1, uuid);
//                ps.execute();
//                ResultSet rs = ps.getResultSet();
//                if (!rs.next()) {
//                    throw new NotExistStorageException(uuid);
//                }
//                Resume resume = new Resume(uuid, rs.getString("full_name"));
//                return resume;
//            }
//        });
//    }
//
//    @Override
//    public void delete(String uuid) {
//        helper.execute(connectionFactory, "DELETE FROM resume WHERE uuid = ?", new SqlExecutor() {
//            @Override
//            public Object execute(Connection connection, PreparedStatement ps) throws SQLException {
//                ps.setString(1, uuid);
//                if (!isExist(connection, uuid)) {
//                    throw new NotExistStorageException(uuid);
//                }
//                ps.execute();
//                return null;
//            }
//        });
//    }
//
//    @Override
//    public List<Resume> getAllSorted() {
//        List<Resume> resumes = new ArrayList<>();
//        SqlHelper<List<Resume>> helper = new SqlHelper<>();
//        return helper.execute(connectionFactory, "SELECT * FROM resume ORDER BY full_name, uuid",
//                new SqlExecutor<List<Resume>>() {
//                    @Override
//                    public List<Resume> execute(Connection connection, PreparedStatement ps) throws SQLException {
//                        ps.execute();
//                        ResultSet set = ps.getResultSet();
//                        while (set.next()) {
//                            resumes.add(new Resume(set.getString("uuid"), set.getString("full_name")));
//                        }
//                        return resumes;
//                    }
//                });
//    }
//
//
//    @Override
//    public int size() {
//        SqlHelper<Integer> helper = new SqlHelper<>();
//        return helper.execute(connectionFactory, "SELECT count(*) FROM resume", new SqlExecutor<Integer>() {
//            @Override
//            public Integer execute(Connection connection, PreparedStatement ps) throws SQLException {
//                ps.execute();
//                ResultSet rs = ps.getResultSet();
//                rs.next();
//                return rs.getInt(1);
//            }  ///???? why I changed e.printStackTrce to throw and don't need return statement
//        });
//    }
//
//    public boolean isExist(Connection connection, String uuid) throws SQLException {
//        PreparedStatement preparedStatement = connection.prepareStatement("SELECT count(*) from resume where uuid = ?");
//        preparedStatement.setString(1, uuid);
//        preparedStatement.execute();
//        ResultSet rs = preparedStatement.getResultSet();
//        rs.next();
//        if (rs.getInt(1) > 0) {
//            return true;
//        }
//        return false;
//    }
//}


//Version 1
//---------------------------------------------------------------------------------------------------------------------
// SqlHelper<Resume> helper = new SqlHelper();
//    public final ConnectionFactory connectionFactory;
//
//    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
//        connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
//    }

//
//    @Override
//    public void clear() {
//        try (Connection connection = connectionFactory.getConnection();
//             PreparedStatement ps = connection.prepareStatement("DELETE  FROM resume")) {
//            ps.execute();
//        } catch (SQLException e) {
//            throw new StorageException(e);
//        }
//    }
//
//    @Override
//    public void update(Resume r) {
//        try (Connection connection = connectionFactory.getConnection();
//             PreparedStatement statement = connection.prepareStatement("UPDATE resume SET full_name = ? where uuid = ?")
//        ) {
//            statement.setString(1, r.getFullName());
//            statement.setString(2, r.getUuid());
//            if (!isExist(connection, r.getUuid())) {
//                throw new NotExistStorageException(r.getUuid());
//            }
//            statement.execute();
//        } catch (SQLException e) {
//            throw new StorageException(e);
//        }
//
//    }
//
//    @Override
//    public void save(Resume r) {
//        try (Connection connection = connectionFactory.getConnection();
//             PreparedStatement ps = connection.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?,?)")) {
//            ps.setString(1, r.getUuid());
//            ps.setString(2, r.getFullName());
//            if (isExist(connection, r.getUuid())) {
//                throw new ExistStorageException(r.getUuid());
//            }
//            ps.execute();
//        } catch (SQLException e) {
//            throw new StorageException(e);
//        }
//    }
//
//    @Override
//    public Resume get(String uuid) {
//        try (Connection connection = connectionFactory.getConnection();
//             PreparedStatement ps = connection.prepareStatement("SELECT * FROM resume r WHERE r.uuid = ?")) {
//            ps.setString(1, uuid);
//            ps.execute();
//            ResultSet rs = ps.getResultSet();
//            if (!rs.next()) {
//                throw new NotExistStorageException(uuid);
//            }
//            Resume resume = new Resume(uuid, rs.getString("full_name"));
//            return resume;
//        } catch (SQLException e) {
//            throw new StorageException(e);
//        }
//    }
//
//    @Override
//    public void delete(String uuid) {
//        try (Connection connection = connectionFactory.getConnection();
//             PreparedStatement statement = connection.prepareStatement("DELETE FROM resume WHERE uuid = ?")) {
//            statement.setString(1, uuid);
//            if (!isExist(connection, uuid)) {
//                throw new NotExistStorageException(uuid);
//            }
//            statement.execute();
//        } catch (SQLException e) {
//            throw new StorageException(e);
//        }
//
//    }
//
//    @Override
//    public List<Resume> getAllSorted() {
//        List<Resume> resumes = new ArrayList<>();
//        try (Connection connection = connectionFactory.getConnection();
//             PreparedStatement statement = connection.prepareStatement("SELECT * FROM resume ORDER BY full_name, uuid")) {
//            statement.execute();
//            ResultSet set = statement.getResultSet();
//            while (set.next()) {
//                resumes.add(new Resume(set.getString("uuid"), set.getString("full_name")));
//            }
//            return resumes;
//        } catch (SQLException e) {
//            throw new StorageException(e);
//        }
//    }
//
//
//    @Override
//    public int size() {
//        try (Connection connection = connectionFactory.getConnection();
//             PreparedStatement statement = connection.prepareStatement("SELECT count(*) FROM resume ")) {
//            statement.execute();
//            ResultSet rs = statement.getResultSet();
//            rs.next();
//            return rs.getInt(1);
//        } catch (SQLException e) {
//            throw new StorageException(e);
//        } ///???? why I changed e.printStackTrce to throw and don't need return statement
//    }
//
//    public Connection getConnection() throws SQLException {
//        Connection connection = connectionFactory.getConnection();
//        return connection;
//    }
//
//    public boolean isExist(Connection connection, String uuid) throws SQLException {
//        PreparedStatement preparedStatement = connection.prepareStatement("SELECT count(*) from resume where uuid = ?");
//        preparedStatement.setString(1, uuid);
//        preparedStatement.execute();
//        ResultSet rs = preparedStatement.getResultSet();
//        rs.next();
//        if (rs.getInt(1) > 0) {
//            return true;
//        }
//        return false;
//    }
//}
//
