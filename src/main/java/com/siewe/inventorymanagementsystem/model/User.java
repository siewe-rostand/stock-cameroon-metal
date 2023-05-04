package com.siewe.inventorymanagementsystem.model;

import lombok.*;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Builder
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

    @Basic(optional = false)
//    @NotNull
//    @Size(min = 1,max = 16)
    private String username;

    private String firstname;

    private String lastname;

    private String email;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
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

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(name = "created_date")
    private Timestamp created_date;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(name = "reset_date", nullable = true)
    private LocalDateTime resetDate = null;

    @OneToMany(mappedBy="user")
    private List<UserRole> userRoles;

    @OneToOne(optional=false, mappedBy="customer")
    private Receipt receipt;

    @OneToOne(optional=false, mappedBy="employee")
    private Receipt userReceipt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = {
            @JoinColumn(name = "user_id", referencedColumnName = "id")}, inverseJoinColumns = {
            @JoinColumn(name = "role_id", referencedColumnName = "id")})
    private Set<Role> roles;

    @OneToMany(mappedBy = "user")
    private List<Vente> ventes;

    @OneToMany(mappedBy = "customer")
    private  List<Order> orders;

//    public String getCreatedDate() {
//        String pattern = "yyyy-MM-dd HH:mm";
//        if(createdDate != null) {
//            return createdDate.toString(pattern);
//        }
//        return null;
//    }
//
//    public void setCreatedDate(String createdDate) {
//        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
//        LocalDateTime rd = null;
//        if(createdDate!=null)
//            rd = LocalDateTime.parse(createdDate, formatter);
//        this.createdDate = rd;
//    }
    public Timestamp getCreated_date() {
        return created_date;
    }
    public void setCreated_date(Timestamp createDate) {
        this.created_date = createDate;
    }
    public String getUpdatedDate() {
        String pattern = "yyyy-MM-dd HH:mm";
        if(updatedDate != null) {
            return updatedDate.toString(pattern);
        }
        return null;
    }

    public void setUpdatedDate(String updatedDate) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        LocalDateTime rd = null;
        if(updatedDate!=null)
            rd = LocalDateTime.parse(updatedDate, formatter);
        this.updatedDate = rd;
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
    public String username() {
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
