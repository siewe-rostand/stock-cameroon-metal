package com.siewe.inventorymanagementsystem.model;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.persistence.Column;
import java.math.BigDecimal;

public class BaseEntity {

    private BigDecimal version;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "last_modified_date")
    private LocalDate lastModifiedDateTime;

    @Column(name = "last_modified_user")
    private String lastModifiedUser;

    @Column(name = "created_user")
    private String createdUser;

    public String getCreatedDate() {
        String pattern = "yyyy-MM-dd";
        if(createdDate != null) {
            return createdDate.toString(pattern);
        }
        return null;
    }

    public void setCreatedDate(String createdDate) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        LocalDate cd = null;
        if(createdDate!=null && !createdDate.isEmpty())
            cd = formatter.parseLocalDate(createdDate);
        this.createdDate = cd;
    }

    public String getUpdatedDate() {
        String pattern = "yyyy-MM-dd";
        if(lastModifiedDateTime != null) {
            return lastModifiedDateTime.toString(pattern);
        }
        return null;
    }

    public void setUpdatedDate(String updatedDate) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        LocalDate cd = null;
        if(lastModifiedDateTime!=null && !updatedDate.isEmpty())
            cd = formatter.parseLocalDate(updatedDate);
        this.lastModifiedDateTime = cd;
    }
}
