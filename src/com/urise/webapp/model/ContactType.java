package com.urise.webapp.model;

public enum ContactType {
    PHONE("Phone number"),
    SKYPE("Skype login"),
    EMAIL("Email address"),
    LINKEDIN("LinkedIn profile"),
    GITHUB("Github profile"),
    STACKOVERFLOW("StackOverflow profile"),
    HOME_PAGE("Home Page");

    private final String description;

    ContactType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
