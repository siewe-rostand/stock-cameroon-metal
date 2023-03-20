package com.siewe.inventorymanagementsystem.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BaseEntity {

    private BigDecimal version;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "deleted_date")
    private LocalDateTime deletedDate;

    @Column(name = "last_modified_user")
    private String lastModifiedUser;

    @Column(name = "created_user")
    private String createdUser;

    public String getCreatedDate() {
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if(createdDate != null) {
            return createdDate.format(formatter);
        }
        return null;
    }

    public void setCreatedDate(String createdDate) {
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime cd = null;
        if(createdDate!=null)
            cd = LocalDateTime.parse(createdDate,formatter);
        this.createdDate = cd;
    }

    public String getUpdatedDate() {
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if(updatedDate != null) {
            return updatedDate.format(formatter);
        }
        return null;
    }

    public void setUpdatedDate(String updatedDate) {
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime cd = null;
        if(updatedDate!=null)
            cd = LocalDateTime.parse(updatedDate,formatter);
        this.updatedDate = cd;
    }
    public String getDeletedDate() {
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if(deletedDate != null) {
            return deletedDate.format(formatter);
        }
        return null;
    }

    public void setDeletedDate(String deletedDate) {
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime cd = null;
        if(deletedDate!=null)
            cd = LocalDateTime.parse(deletedDate,formatter);
        this.deletedDate = cd;
    }
}
