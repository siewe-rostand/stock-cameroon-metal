package com.siewe.inventorymanagementsystem.model;


import lombok.*;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.persistence.*;

/**
 *
 * @author medilox
 */
@Entity
@Table(name = "user_login")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLogin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @Column(name = "dateTime")
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private LocalDateTime dateTime;

    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User user;


    public String getDateTime() {
        String pattern = "dd-MM-yyyy HH:mm";
        if(dateTime != null) {
            return dateTime.toString(pattern);
        }
        return null;
    }

    public void setDateTime(String dateTime) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm");
        LocalDateTime cd = null;
        if(dateTime!=null)
            cd = LocalDateTime.parse(dateTime, formatter);
        this.dateTime = cd;
    }
}
