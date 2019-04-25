package com.architec.demo.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class User {

    @Id
    @GeneratedValue
    private int user_id;

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