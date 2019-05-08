package com.architec.crediti.models;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long userId;

    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "studentennummer", nullable = false)
    private String studentennummer;

    @Column(name = "gsm")
    private String gsm;

    @Column(name = "zap_mobility")
    private String zapOrMobility;

    @Column(name = "first_login", nullable = false)
    private boolean firstLogin;

    @Column(name = "login_count")
    private int loginCount;

    public User() {
    }

    public User(String firstname, String lastname, String email, String role, String studentennummer, boolean firstLogin) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.role = role;
        this.studentennummer = studentennummer;
        this.firstLogin = firstLogin;
    }

    public String getGsm() {
        return gsm;
    }

    public void setGsm(String gsm) {
        this.gsm = gsm;
    }

    public String getZapOrMobility() {
        return zapOrMobility;
    }

    public void setZapOrMobility(String zapOrMobility) {
        this.zapOrMobility = zapOrMobility;
    }

    public boolean isFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(boolean firstLogin) {
        this.firstLogin = firstLogin;
    }

    public int getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(int loginCount) {
        this.loginCount = loginCount;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
