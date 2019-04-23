package com.architec.demo.models;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class User {

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String Role;

    public User() {
    }

    public User(String firstName, String lastName, String Role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.Role = Role;
    }
}