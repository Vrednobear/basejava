package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.*;
import com.urise.webapp.sql.SqlHelper;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class SqlStorage implements Storage {
    SqlHelper helper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
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
            }
            deleteContacts(r, connection);
            insertContacts(r, connection);
            deleteSections(r, connection);
            insertSections(r, connection);

            return null;
        });
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
            insertContacts(r, connection);
            insertSections(r, connection);
            return null;
        });

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
        Resume r = helper.execute(
                "SELECT * FROM resume r " +
                        "LEFT JOIN contact c ON (r.uuid = c.resume_uuid) " +
                        "WHERE r.uuid = ?",
                (connection, ps) -> {
                    ps.setString(1, uuid);
                    ps.execute();
                    ResultSet rs = ps.getResultSet();
                    if (!rs.next()) {
                        throw new NotExistStorageException(uuid);
                    }
                    Resume resume = new Resume(uuid, rs.getString("full_name"));

                    do {
                        addContact(rs, resume);
                    } while (rs.next());

                    return resume;
                });
        return helper.execute(
                "SELECT * FROM resume r " +
                        "LEFT JOIN section s ON (r.uuid = s.resume_uuid) " +
                        "WHERE r.uuid = ?",
                (connection, ps) -> {
                    ps.setString(1, uuid);
                    ps.execute();
                    ResultSet rs = ps.getResultSet();
                    if (!rs.next()) {
                        throw new NotExistStorageException(uuid);
                    }
                    do {
                        addSection(rs, r, connection);
                    } while (rs.next());
                    return r;
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
        List<Resume> listWithContacts = helper.execute(
                "SELECT * FROM resume " +
                        "LEFT JOIN contact с " +
                        "ON resume.uuid = с.resume_uuid " +
                        "ORDER BY full_name, uuid", (connection, ps) ->
                {
                    ResultSet rs = ps.executeQuery();
                    Map<String, Resume> ResumesMap = new LinkedHashMap<>();
                    while (rs.next()) {
                        String resumeUuid = rs.getString("uuid");
                        String resumeFullName = rs.getString("full_name");
                        Resume resume = ResumesMap.get(resumeUuid);
                        if (!ResumesMap.containsKey(resumeUuid)) {
                            resume = new Resume(resumeUuid, resumeFullName);
                            addContact(rs, resume);
                            ResumesMap.put(resumeUuid, resume);
                        } else {
                            addContact(rs, resume);
                            ResumesMap.put(resumeUuid, resume);
                        }
                    }
                    return new ArrayList<>(ResumesMap.values());
                });

        List<Resume> listWithSections = helper.execute(
                "SELECT * FROM resume " +
                        "LEFT JOIN section s " +
                        "ON resume.uuid = s.resume_uuid " +
                        "ORDER BY full_name, uuid", (connection, ps) ->
                {
                    ResultSet rs = ps.executeQuery();
                    Map<String, Resume> ResumesMap = new LinkedHashMap<>();
                    while (rs.next()) {
                        String resumeUuid = rs.getString("uuid");
                        String resumeFullName = rs.getString("full_name");
                        Resume resume = ResumesMap.get(resumeUuid);
                        if (!ResumesMap.containsKey(resumeUuid)) {
                            resume = new Resume(resumeUuid, resumeFullName);
                            addSection(rs, resume, connection);
                            ResumesMap.put(resumeUuid, resume);
                        } else {
                            addSection(rs, resume, connection);
                            ResumesMap.put(resumeUuid, resume);
                        }
                    }
                    return new ArrayList<>(ResumesMap.values());
                });

        Iterator<Resume> iterator = listWithSections.listIterator();
        while (iterator.hasNext()) {
            for (Resume r :
                    listWithContacts) {
                EnumMap<SectionType, Section> sections = iterator.next().getSections();
                for (Map.Entry<SectionType, Section> entry :
                        sections.entrySet()) {
                    r.addSection(entry.getKey(), entry.getValue());
                }
            }
        }
        return listWithContacts;
    }

    public boolean isExist(Connection connection, String uuid) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT count(*) from resume where uuid = ?");
        preparedStatement.setString(1, uuid);
        preparedStatement.execute();
        ResultSet rs = preparedStatement.getResultSet();
        rs.next();
        return rs.getInt(1) > 0;
    }

    public void deleteContacts(Resume r, Connection connection) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("DELETE from contact where resume_uuid = ?")) {
            ps.setString(1, r.getUuid());
            ps.execute();
        }
    }

    public void insertContacts(Resume r, Connection connection) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO contact (type, value, resume_uuid) " +
                        "Values(?,?,?)")) {
            for (Map.Entry<ContactType, String> entry :
                    r.getContacts().entrySet()) {
                setContactsPreparedStatement(ps, r, entry);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    public void deleteSections(Resume r, Connection connection) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("DELETE from section where resume_uuid = ?")) {
            ps.setString(1, r.getUuid());
            ps.execute();
        }
    }

    //TODO: nullpointer if the entrySet is empty!!!Now I added data in update test method bit without it will be exception
    public void insertSections(Resume r, Connection connection) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO section (type, section, resume_uuid) " +
                        "Values(?,?,?) returning id")) {
            for (Map.Entry<SectionType, Section> entry :
                    r.getSections().entrySet()) {
                setSectionsPreparedStatement(ps, r, entry);
                ps.execute();
                ResultSet resultSet = ps.getResultSet();
                resultSet.next();
                if (entry.getKey().equals(SectionType.EDUCATION) || entry.getKey().equals(SectionType.EXPERIENCE)) {
                    int sectionId = resultSet.getInt("id");
                    OrganizationSection section = (OrganizationSection) entry.getValue();
                    insertOrganizations(sectionId, section.getOrganizations(), connection);
                }
            }
        }
    }

    public void insertOrganizations(int sectionId, Set<Organization> organizations, Connection connection) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO organization (organization_name, organization_link, section_id) " +
                        "Values(?,?,?) returning id")) {
            for (Organization organization :
                    organizations) {
                ps.setString(1, organization.getOrganizationName());
                ps.setString(2, organization.getOrganizationLink().getUrl());
                ps.setInt(3, sectionId);
                ps.execute();
                ResultSet resultSet = ps.getResultSet();
                resultSet.next();
                int organizationId = resultSet.getInt("id");
                insertExperiences(organization.getExperiences(), organizationId, connection);
            }
        }
    }

    public void insertExperiences(List<Experience> experiences, int organizationId, Connection connection) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO experience (start_date,end_date,title,description,organization_id) " +
                        "Values(?,?,?,?,?)")) {
            for (Experience experience :
                    experiences) {
                ps.setDate(1, Date.valueOf(experience.getStartDate()));
                ps.setDate(2, Date.valueOf(experience.getEndDate()));
                ps.setString(3, experience.getTitle());
                ps.setString(4, experience.getDescription());
                ps.setInt(5, organizationId);
                ps.execute();
            }
        }
    }

    private void addContact(ResultSet rs, Resume resume) throws SQLException {
        String value = rs.getString("value");
        if (value != null) {
            resume.addContact(ContactType.valueOf(rs.getString("type")), value);
        }
    }

    private void addSection(ResultSet rs, Resume resume, Connection connection) throws SQLException {
        String value = rs.getString("section");
        SectionType type = SectionType.valueOf(rs.getString("type"));
        int secId = rs.getInt("id");
        switch (type) {
            case PERSONAL:
            case OBJECTIVE:
                resume.addSection(type, new TextSection(value));
                break;
            case ACHIEVEMENT:
            case QUALIFICATIONS:
                List<String> values;
                String[] arr = value.split("\n");
                values = Arrays.asList(arr);
                resume.addSection(type, new ListSection(values));
                break;
            case EDUCATION:
            case EXPERIENCE:
                try (PreparedStatement ps = connection.prepareStatement(
                        "SELECT section_id, " +
                                "organization_name," +
                                "organization_link," +
                                "start_date," +
                                "end_date," +
                                "title," +
                                "description " +
                                "from organization o " +
                                "LEFT JOIN experience e " +
                                "ON(o.id = e.organization_id) " +
                                //   "ORDER BY section_id" +
                                "WHERE section_id=?"
                )) {
                    ps.setInt(1, secId);
                    ResultSet resultSet = ps.executeQuery();
                    List<Integer> sectionIdList = new ArrayList<>();
                    Set<Organization> organizationSet = new HashSet<>();

                    while (resultSet.next()) {
                        createOrganizationWithExperience(resultSet, organizationSet);
//                        if (!sectionIdList.contains(resultSet.getInt("section_id"))) {
//                            sectionIdList.add(resultSet.getInt("section_id"));
//                            createOrganizationWithExperience(resultSet, organizationSet);
//                        } else {
//                            createOrganizationWithExperience(resultSet, organizationSet);
//                        }
                    }
                    resume.addSection(type, new OrganizationSection(organizationSet));
                }
        }
    }

    private void createOrganizationWithExperience(ResultSet resultSet, Set<Organization> organizationSet) throws SQLException {
        Experience experience = new Experience(
                resultSet.getDate("start_date").toLocalDate(),
                resultSet.getDate("end_date").toLocalDate(),
                resultSet.getString("title"),
                resultSet.getString("description"));
        Organization organization = new Organization(
                resultSet.getString("organization_name"),
                resultSet.getString("organization_link"),
                experience);
        if (organizationSet.contains(organization)) {
            for (Organization o :
                    organizationSet) {
                if (o.equals(organization)) {
                    o.addExperience(experience);
                    organizationSet.add(o);
                    return;
                }
            }
        }
        organizationSet.add(organization);
    }

    public void setResumePreparedStatement(PreparedStatement ps, Resume r) throws SQLException {
        ps.setString(1, r.getUuid());
        ps.setString(2, r.getFullName());
    }

    public void setContactsPreparedStatement(PreparedStatement ps, Resume r,
                                             Map.Entry<ContactType, String> entry) throws SQLException {
        ps.setString(1, entry.getKey().toString());
        ps.setString(2, entry.getValue());
        ps.setString(3, r.getUuid());
    }

    public void setSectionsPreparedStatement(PreparedStatement ps, Resume r,
                                             Map.Entry<SectionType, Section> entry) throws SQLException {
        SectionType st = entry.getKey();
        ps.setString(1, entry.getKey().toString());
        StringBuilder sb = new StringBuilder();
        switch (st) {
            case PERSONAL:
            case OBJECTIVE:
                ps.setString(2, entry.getValue().toString());
                break;
            case QUALIFICATIONS:
            case ACHIEVEMENT:
                ListSection ls = (ListSection) entry.getValue();
                List<String> values = ls.getValues();
                Iterator<String> iterator = values.iterator();
                while (iterator.hasNext()) {
                    sb.append(iterator.next()).append("\n");
                }
                ps.setString(2, sb.toString());
                break;
            case EDUCATION:
            case EXPERIENCE:
                ps.setString(2, null);
        }
        ps.setString(3, r.getUuid());
    }
}
