package com.urise.webapp.model;

import java.util.*;

public class OrganizationSection extends Section {
  //private List<Organization> organizations;
  private static final long serialVersionUID = 1L;
  private Set<Organization> organizations;

    public OrganizationSection(Organization...organizations) {
        this(Set.of(organizations));
    }

    public OrganizationSection(Set<Organization> organizations) {
        Objects.requireNonNull(organizations,"organizations must not be null");
        this.organizations = organizations;
    }


    public Set<Organization> getOrganizations() {
        return organizations;
    }


    public void putOrganization(Organization organization){
        if(!organizations.contains(organization)){
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
        return organizations.toString();
    }
}
