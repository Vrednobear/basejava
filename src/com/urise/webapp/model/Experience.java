package com.urise.webapp.model;

import com.urise.webapp.util.DateUtil;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.Objects;

import static com.urise.webapp.util.DateUtil.NOW;

public class Experience implements Serializable {
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String description;
    private final String title;

    public Experience(LocalDate startDate, LocalDate endDate, String title, String description) {
        Objects.requireNonNull(startDate, "startDate must not be null");
        Objects.requireNonNull(endDate, "endDate must not be null");
        Objects.requireNonNull(title, "title must not be null");

        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
        this.description = description;
    }

    public Experience(int startDateYear, Month startDateMonth, String title, String description) {
        this(DateUtil.of(startDateYear,startDateMonth),NOW,title,description);
    }
    public Experience(int startDateYear, Month startDateMonth, int endDateYear, Month endDateMonth,
            String title, String description) {
        this(DateUtil.of(startDateYear,startDateMonth),
                DateUtil.of(endDateYear,endDateMonth),title,description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Experience that = (Experience) o;
        return startDate.equals(that.startDate) &&
                endDate.equals(that.endDate) &&
                Objects.equals(description, that.description) &&
                title.equals(that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate, description, title);
    }

    @Override
    public String toString() {
        return "Experience{" + "\n" +
                "startDate=" + startDate + "\n" +
                ", endDate=" + endDate + "\n" +
                ", title='" + title + "\n" +
                ", description='" + description + "\n" +
                '}';
    }
}
