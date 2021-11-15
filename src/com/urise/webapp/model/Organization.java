package com.urise.webapp.model;

import java.io.ObjectStreamClass;
import java.time.LocalDate;
import java.util.Objects;

public class Organization {
    private final Link organizationLink;

    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String description;
    private final String title;

    public Organization(String organizationName, String url, LocalDate startDate, LocalDate endDate, String title, String description) {
        Objects.requireNonNull(startDate, "startDate must not be null");
        Objects.requireNonNull(endDate, "endDate must not be null");
        Objects.requireNonNull(title, "title must not be null");

        this.organizationLink = new Link(organizationName, url);
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
        this.description = description;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization that = (Organization) o;
        return organizationLink.equals(that.organizationLink) &&
                startDate.equals(that.startDate) &&
                endDate.equals(that.endDate) &&
                Objects.equals(description, that.description) &&
                title.equals(that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(organizationLink, startDate, endDate, description, title);
    }

    @Override
    public String toString() {
        return "Organization{" +
                ", organizationLink=" + organizationLink +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", description='" + description + '\'' +
                '}';
    }
}
