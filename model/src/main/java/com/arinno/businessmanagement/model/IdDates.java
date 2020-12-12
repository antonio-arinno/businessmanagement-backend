package com.arinno.businessmanagement.model;

import java.util.Date;

public class IdDates {

    private Long id;

    private DateRange dateRange;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DateRange getDateRange() {
        return dateRange;
    }

    public void setDateRange(DateRange dateRange) {
        this.dateRange = dateRange;
    }

    @Override
    public String toString() {
        return "IdDates{" +
                "id=" + id +
                ", dateRange=" + dateRange +
                '}';
    }
}
