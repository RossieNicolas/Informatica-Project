package com.architec.crediti.models;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long userId;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "first_login", nullable = false)
    private boolean firstLogin;


    public User() {
    }

    public User(String firstname, String lastname, String email, String role, boolean firstLogin) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.role = role;
        this.firstLogin = firstLogin;
    }


    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(boolean firstLogin) {
        this.firstLogin = firstLogin;
    }

    @Override
    public String toString() {
        return this.firstname + " " + this.lastname;
    }
}

