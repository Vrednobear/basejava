package com.urise.webapp.model;

import java.io.ObjectStreamClass;
import java.io.PipedOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

public class Organization implements Serializable {
    private final String organizationName;
    private final Link organizationLink;

    List<Experience> experiences;

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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization that = (Organization) o;
        return organizationName.equals(that.organizationName);
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
