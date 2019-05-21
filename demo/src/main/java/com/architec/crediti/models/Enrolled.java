package com.architec.crediti.models;

import javax.persistence.*;

@Entity
@Table(name = "enrolled")
public class Enrolled {
    @Id
    @Column(name = "enrolled_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long enrolledId;

    @Column(name = "fullname")
    private String fullname;

    @Column(name = "email")
    private String email;

    @Column(name = "assignment_id")
    private long assignmentId;

    @Column(name = "title")
    private String title;

    @Column(name = "user_id")
    private long userId;

    public Enrolled() {
    }

    public Enrolled(String fullname, String email, long assignmentId, String title, long userId) {
        this.fullname = fullname;
        this.email = email;
        this.assignmentId = assignmentId;
        this.title = title;
        this.userId = userId;
    }

    public long getEnrolledId() {
        return enrolledId;
    }

    public void setEnrolledId(long enrolledId) {
        this.enrolledId = enrolledId;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getAssignment() {
        return assignmentId;
    }

    public void setAssignment(long assignmentId) {
        this.assignmentId = assignmentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}

