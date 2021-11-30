package com.urise.webapp.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.ObjectStreamClass;
import java.io.PipedOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class Organization implements Serializable {
    private String organizationName;
    private Link organizationLink;

    List<Experience> experiences;

    public Organization() {
    }

    public Organization(String organizationName, String url, Experience...experiences) {
        this(new Link(organizationName,url),Arrays.asList(experiences));
    }

    public Organization(Link organizationLink,List<Experience> experiences) {
        this.organizationName = organizationLink.getName();
        this.organizationLink = organizationLink;
        this.experiences = new ArrayList<>(experiences);
    }

    public void addExperience(Experience experience){
        experiences.add(experience);
    }

    public String getOrganizationName() {
        return organizationName;
    }
    public Link getOrganizationLink() {
        return organizationLink;
    }
    public List<Experience> getExperiences() {
        return experiences;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization that = (Organization) o;
         return Objects.equals(organizationName,that.organizationName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(organizationName);
    }

    @Override
    public String toString() {
        return "Organization{" +
                ", organizationName=" + organizationName + "\n" +
                ", organizationLink=" + organizationLink + "\n" +
                experiences.toString() + "\n";
    }

}
