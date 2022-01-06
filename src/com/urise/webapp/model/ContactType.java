package com.urise.webapp.model;

public enum ContactType {
    PHONE("Phone number"),
    SKYPE("Skype login"){
        @Override
        public String toHtmlChosenType(String value) {
            return "<a href='skype:" + value + "'>" + value + "</a>";
        }
    },
    EMAIL("Email address"){
        @Override
        public String toHtmlChosenType(String value) {
            return "<a href='mailto:" + value + "'>" + value + "</a>";
        }
    },
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

    public String toHtmlChosenType(String value) {
       return description + ": " + value;
    }

    public String toHtml(String value) {
        return (value == null) ? "" : toHtmlChosenType(value);
    }

}
