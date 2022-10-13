package com.javachallenge.basico.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collections;
import java.util.Set;

@Entity
@Table(name = "TB_USER", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class User implements Serializable {

    public enum Role {
        ROLE_ADMIN,
        ROLE_USER
    }

    private Long id;
    private String username;
    private String password;
    private Set<Role> roles;

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.roles = Collections.singleton(Role.ROLE_USER);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(nullable = false)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(nullable = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @ElementCollection(targetClass = Role.class)
    @CollectionTable(name = "TB_USER_ROLE", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role_name")
    @Enumerated(EnumType.STRING)
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role role) {
        if (roles == null) {
            roles = Collections.singleton(role);
        } else {
            roles.add(role);
        }
    }
}