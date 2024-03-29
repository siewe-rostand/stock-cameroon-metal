package com.siewe.inventorymanagementsystem.model;


import lombok.*;
import org.hibernate.Hibernate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.persistence.*;
import java.util.Objects;

/**
 *
 * @author medilox
 */
@Entity
@Table(name = "user_login")
@Getter
@Setter
@ToString
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserLogin userLogin = (UserLogin) o;
        return id != null && Objects.equals(id, userLogin.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
