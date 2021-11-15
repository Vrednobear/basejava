package com.urise.webapp.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ListSection extends Section {
  private final List<String> values;

    public ListSection(List <String> values) {
        Objects.requireNonNull(values,"values must not be null");
        this.values = values;
    }

    public List<String> getValues() {
        return values ;
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
      return values.toString();
    }


}
