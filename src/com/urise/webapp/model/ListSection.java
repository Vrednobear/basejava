package com.urise.webapp.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class ListSection extends Section {
    public static final Section EMPTY = new ListSection("") ;
    private static final long serialVersionUID = 1L;
    private List<String> values;

    public ListSection() {
    }

    public ListSection(List<String> values) {
        Objects.requireNonNull(values, "values must not be null");
        this.values = values;
    }

    public ListSection(String... values) {
        this(Arrays.asList(values));
    }

    public List<String> getValues() {
        return values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListSection that = (ListSection) o;
        return values.equals(that.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(values);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (String v :
                values) {
            builder.append(v + "\n");
        }
        return builder.toString();
    }

    public String toHtml(){
        StringBuilder builder = new StringBuilder();
        for (String v :
                values) {
            builder.append("<li>"+ v +"</li>");
        }
        return builder.toString();
    }
}
