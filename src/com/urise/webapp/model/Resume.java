package com.urise.webapp.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Initial resume class
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Resume implements Comparable<Resume>, Serializable {
    private static final long serialVersionUID = 1L;

    // Unique identifier
    private  String uuid;
    private String fullName;

    //private final
    private final EnumMap<ContactType, String> contactMap = new EnumMap<>(ContactType.class);
    private final EnumMap<SectionType, Section> sectionMap = new EnumMap<>(SectionType.class);

    public Resume() {
    }

    public Resume(String fullName) {
        this(UUID.randomUUID().toString(), fullName);
    }

    public Resume(String uuid, String fullName) {
        Objects.requireNonNull(uuid, "uuid must not be null");
        Objects.requireNonNull(fullName, "fullName must not be null");
        this.uuid = uuid;
        this.fullName = fullName;

    }

    @Override
    public String toString() {
        return uuid + '(' + fullName + ')';
    }

    public String getUuid() {
        return uuid;
    }

    public String getFullName() {
        return fullName;
    }

    public String getContact(ContactType type) {
        return contactMap.get(type);
    }

    public Section getSection(SectionType type) {
        return sectionMap.get(type);
    }

    public void addContact(ContactType type,String contact) {
        contactMap.put(type,contact);
    }

    public void addSection(SectionType type,Section section) {
        sectionMap.put(type,section);
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Resume resume = (Resume) o;
//        return Objects.equals(uuid, resume.uuid) &&
//                Objects.equals(fullName, resume.fullName) &&
//                Objects.equals(contactMap, resume.contactMap) &&
//                Objects.equals(sectionMap, resume.sectionMap);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(uuid, fullName, contactMap, sectionMap);
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resume resume = (Resume) o;
        return uuid.equals(resume.uuid) &&
                fullName.equals(resume.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, fullName);
    }


    @Override
    public int compareTo(Resume o) {
        int compResult = (this.fullName).compareTo(o.fullName);
        return compResult != 0 ? compResult : uuid.compareTo(o.uuid);
    }
}