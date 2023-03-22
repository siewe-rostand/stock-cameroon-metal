package com.siewe.inventorymanagementsystem.model;

import lombok.*;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "id")
    private Long userId;

    @Column(name = "fullname")
    private String name;

    private String username;

    private String firstname;

    private String lastname;

    private String email;

    private String password;

    private String telephone;

    @Column(name = "telephone_alt")
    private String telephoneAlt;
    @Column(name = "city")
    private String city;

    @Column(name = "quarter")
    private String quarter;

    @Column(name = "validated")
    private Boolean validated;

    @Column(name = "deleted")
    private Boolean deleted;


    @Column(name = "activated")
    private Boolean activated;

    //One Signal Player ID
    @Column(name = "player_id")
    private String playerId;

    @Size(min = 2, max = 5)
    @Column(name = "lang_key", length = 5)
    private String langKey;

    @Size(max = 20)
    @Column(name = "activation_key", length = 20)
    private String activationKey;

    @Size(max = 20)
    @Column(name = "reset_key", length = 20)
    private String resetKey;

    @Column(name = "created_date")
    private LocalDate createdDate;
    @Column(name = "updated_date")
    private LocalDate updatedDate;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(name = "reset_date", nullable = true)
    private LocalDateTime resetDate = null;

    @OneToMany(mappedBy="user")
    private List<UserRole> userRoles;

    @ManyToMany
    @JoinTable(name = "user_roles", joinColumns = {
            @JoinColumn(name = "user_id", referencedColumnName = "id")}, inverseJoinColumns = {
            @JoinColumn(name = "role_id", referencedColumnName = "id")})
    private Collection<Role> roles;

    @OneToMany(mappedBy = "user")
    private List<Vente> ventes;

    @OneToMany(mappedBy = "customer")
    private  List<Order> orders;

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
        if(updatedDate != null) {
            return updatedDate.toString(pattern);
        }
        return null;
    }

    public void setUpdatedDate(String updatedDate) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        LocalDate cd = null;
        if(updatedDate !=null && !updatedDate.isEmpty())
            cd = formatter.parseLocalDate(updatedDate);
        this.updatedDate = cd;
    }

    public String getResetDate() {
        String pattern = "yyyy-MM-dd HH:mm";
        if(resetDate != null) {
            return resetDate.toString(pattern);
        }
        return null;
    }

    public void setResetDate(String resetDate) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        LocalDateTime rd = null;
        if(resetDate!=null)
            rd = LocalDateTime.parse(resetDate, formatter);
        this.resetDate = rd;
    }

    public String getName() {
        String name = this.getLastname();
        if(this.getFirstname() != null){
            name += "_" + this.getFirstname();
        }
        return name;
    }
    public  String getFullName(){
        String name = "";
        if (this.firstname != null){
            name += " "+this.firstname;
        }

        if (this.lastname != null){
            name += " "+this.lastname;
        }
        return  name;
    }
}
