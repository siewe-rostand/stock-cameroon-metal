package com.siewe.inventorymanagementsystem.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Receipt implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double amount;
    @Column(name = "total_amount")
    private double total_amount;

    @Column(name = "payment_mode")
    private String payment_mode;

    @Column(name = "payment_status")
    private String payment_status;

    private String type;

    private Boolean deleted;

    @OneToOne
    private Order order;

    @OneToOne(optional=false)
    @JoinColumn(
            name="customer_id", unique=true, nullable=false, updatable=false)
    private User customer;

    @OneToOne(optional=false)
    @JoinColumn(
            name="employee_id", unique=true, nullable=false, updatable=false)
    private User employee;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    @Column(name = "deleted_date")
    private LocalDateTime deletedDate;

    public String getCreatedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if(createdDate != null) {
            return createdDate.format(formatter);
        }
        return null;
    }
    public void setCreatedDate(String createdDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime cd = null;
        if(createdDate!=null)
            cd = LocalDateTime.parse(createdDate,formatter);
        this.createdDate = cd;
    }

    public String getUpdatedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if(updatedDate != null) {
            return updatedDate.format(formatter);
        }
        return null;
    }

    public void setUpdatedDate(String createdDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime cd = null;
        if(createdDate!=null)
            cd = LocalDateTime.parse(createdDate,formatter);
        this.updatedDate = cd;
    }
    public String getDeletedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if(deletedDate != null) {
            return deletedDate.format(formatter);
        }
        return null;
    }

    public void setDeletedDate(String deletedDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime cd = null;
        if(deletedDate!=null)
            cd = LocalDateTime.parse(deletedDate,formatter);
        this.deletedDate = cd;
    }
}
