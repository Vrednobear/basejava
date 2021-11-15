package com.urise.webapp.model;

public enum SectionType {
    PERSONAL("Personal qualities"),
    OBJECTIVE("Position"),
    ACHIEVEMENT("Achievements"),
    QUALIFICATIONS("Qualification"),
    EXPERIENCE("Work experience"),
    EDUCATION("Education");

    private String title;

    SectionType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
