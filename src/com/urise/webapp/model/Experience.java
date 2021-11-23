package com.urise.webapp.model;

import com.urise.webapp.util.DateUtil;
import com.urise.webapp.util.LocalDateAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.Objects;

import static com.urise.webapp.util.DateUtil.NOW;

@XmlAccessorType(XmlAccessType.FIELD)
public class Experience implements Serializable {
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate startDate;
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate endDate;
    private String description;
    private String title;

    public Experience() {
    }

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
