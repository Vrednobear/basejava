package com.urise.webapp.serializer;

import com.urise.webapp.model.*;
import com.urise.webapp.util.DateUtil;

import java.io.*;
import java.time.Month;
import java.util.*;

public class DataStreamSerializer implements SerializationStrategy {
    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());

            Map<ContactType, String> contacts = r.getContacts();
            writeCollection(dos, contacts.entrySet(), (entry) -> {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            });

            Map<SectionType, Section> sections = r.getSections();
            writeCollection(dos, sections.entrySet(), (entry -> {
                String type = entry.getKey().name();
                Section section = entry.getValue();
                dos.writeUTF(type);
                Set<Organization> organizationSet;
                List<String> sectionList;
                switch (type) {
                    case "PERSONAL":
                    case "OBJECTIVE":
                        dos.writeUTF(String.valueOf(entry.getValue()));
                        break;
                    case "ACHIEVEMENT":
                    case "QUALIFICATIONS":
                        sectionList = ((ListSection) section).getValues();
                        writeCollection(dos, sectionList, dos::writeUTF);
                        break;
                    case "EDUCATION":
                    case "EXPERIENCE":
                        organizationSet = ((OrganizationSection) section).getOrganizations();
                        writeCollection(dos, organizationSet, organization -> {
                            dos.writeUTF(organization.getOrganizationName());
                            dos.writeUTF(organization.getOrganizationLink().getUrl());

                            List<Experience> listExp = organization.getExperiences();
                            writeCollection(dos, listExp, experience -> {
                                dos.writeInt(experience.getStartDate().getYear());
                                dos.writeUTF(experience.getStartDate().getMonth().name());
                                dos.writeInt(experience.getEndDate().getYear());
                                dos.writeUTF(experience.getEndDate().getMonth().name());
                                dos.writeUTF(experience.getTitle());
                                dos.writeUTF(experience.getDescription());
                            });
                        });
                }
            }));

        }
    }

    private <T> void writeCollection(DataOutputStream dos, Collection<T> collection, CollectionWriter<T> writer) throws IOException {
        dos.writeInt(collection.size());
        for (T t :
                collection) {
            writer.write(t);
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        Resume resume;
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            resume = new Resume(uuid, fullName);


            int contactsSize = dis.readInt();
            for (int i = 0; i < contactsSize; i++) {
                resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF());
            }

            int sectionsSize = dis.readInt();
            for (int i = 0; i < sectionsSize; i++) {
                String type = dis.readUTF();
                switch (type) {
                    case "PERSONAL":
                    case "OBJECTIVE":
                        resume.addSection(SectionType.valueOf(type), new TextSection(dis.readUTF()));
                        break;
                    case "ACHIEVEMENT":
                    case "QUALIFICATIONS":
                        int listSize = dis.readInt();
                        List<String> list = new ArrayList<>(listSize);
                        readCollection(dis, list, () -> list.add(dis.readUTF()));
                        resume.addSection(SectionType.valueOf(type), new ListSection(list));
                        break;
                    case "EDUCATION":
                    case "EXPERIENCE":
                        int orgSetSize = dis.readInt();
                        Set<Organization> organizationsSet = new LinkedHashSet<>(orgSetSize);
                        readCollection(dis, organizationsSet, () -> {
                            String orgName = dis.readUTF();
                            String orgUrl = dis.readUTF();

                            int expListSize = dis.readInt();
                            List<Experience> experiences = new ArrayList<>(expListSize);
                            readCollection(dis, experiences, () -> {
                                        int stYear = dis.readInt();
                                        String stMonth = dis.readUTF();
                                        int endYear = dis.readInt();
                                        String endMonth = dis.readUTF();
                                        String title = dis.readUTF();
                                        String description = dis.readUTF();
                                        experiences.add(new Experience(
                                                DateUtil.of(stYear, Month.valueOf(stMonth)),
                                                DateUtil.of(endYear, Month.valueOf(endMonth)),
                                                title,
                                                description));
                                    }
                            );
                            organizationsSet.add(new Organization(new Link(orgName, orgUrl), experiences));
                        });
                        resume.addSection(SectionType.valueOf(type), new OrganizationSection(organizationsSet));
                }
            }
        }
        return resume;
    }

    private <T> void readCollection(DataInputStream dis, Collection<T> collection, CollectionReader<T> reader) throws IOException {
        for (int i = 0; i < collection.size(); i++) {
            reader.read();
        }
    }


    private interface CollectionWriter<T> {
        void write(T t) throws IOException;
    }

    private interface CollectionReader<T> {
        void read() throws IOException;

    }
}
