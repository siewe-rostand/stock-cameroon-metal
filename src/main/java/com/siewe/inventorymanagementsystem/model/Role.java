package com.siewe.inventorymanagementsystem.model;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Role{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long roleId;

    private String name;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(name = "created_date")
    @CreationTimestamp
    private Timestamp createdDate;

    public Role(Long id) {
        this.roleId = id;
    }

    public Role(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy="role")
    @ToString.Exclude
    private List<RolePermission> rolePermissions;

    @OneToMany(mappedBy="role")
    @ToString.Exclude
    private List<UserRole> userRoles;

    @Override
    public String toString() {
        return "huru.Role[ id=" + roleId + " ]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Role role = (Role) o;
        return roleId != null && Objects.equals(roleId, role.roleId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
