package com.urise.webapp.model;

import com.urise.webapp.util.DateUtil;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class OrganizationSection extends Section {
    //private List<Organization> organizations;
    private static final long serialVersionUID = 1L;
    private Set<Organization> organizations;

    public OrganizationSection() {
    }

    public OrganizationSection(Organization... organizations) {
        this(Set.of(organizations));
    }

    public OrganizationSection(Set<Organization> organizations) {
        Objects.requireNonNull(organizations, "organizations must not be null");
        this.organizations = organizations;
    }

    public Set<Organization> getOrganizations() {
        return organizations;
    }

    public void putOrganization(Organization organization) {
        if (!organizations.contains(organization)) {
            organizations.add(organization);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganizationSection that = (OrganizationSection) o;
        return organizations.equals(that.organizations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(organizations);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Organization o :
                organizations) {
            builder.append("Organization: " + o.getOrganizationName() + "\n");
            builder.append("Web-site: " + o.getOrganizationLink().getUrl() + "\n");
            for (Experience e :
                    o.getExperiences()) {
                builder.append(e.toString());
            }
        }
        return builder.toString();
    }

    @Override
    public String toHtml() {
        StringBuilder builder = new StringBuilder();
        for (Organization organization :
                organizations) {
            builder.append("<h2>" + organization.getOrganizationName() + "</h2>");
            builder.append("<p><a href=" + organization.getOrganizationLink().getUrl() + ">Web-site</a></p>");
            for (Experience e :
                    organization.experiences) {
                builder.append("<nav>");
                builder.append("<p>" + e.getStartDate() + " -" + "</p>");
                if (!(e.getEndDate().isEqual(DateUtil.NOW))) {
                    builder.append("<p>" + e.getEndDate() + "</p>");
                } else builder.append("<p>" + "Now" + "</p>");

                builder.append("</nav>");

                builder.append("<article>");
                builder.append("<p>" + e.getTitle() + "</p>");
                builder.append("<p>" + e.getDescription() + "</p>");
                builder.append("</article>");
            }
        }
        return builder.toString();
    }
}
