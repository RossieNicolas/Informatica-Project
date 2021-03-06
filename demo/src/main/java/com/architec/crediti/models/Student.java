package com.architec.crediti.models;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "students")
public class Student {
    @Id
    @Column(name = "student_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long studentId;

    @Column(name = "gsm")
    private String gsm;

    @Column(name = "studentennummer", nullable = false)
    private String studentNumber;

    @Column(name = "zap")
    private boolean zap;

    @Column(name = "mobility")
    private boolean mobility;

    @Column(name = "amoutHours")
    private double amoutHours;


    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "student_assign",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "assign_id"))
    private Set<Assignment> assignments;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER, cascade = {CascadeType.MERGE,
            CascadeType.REMOVE})
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User userId;

    public Student() {
    }

    public Student(String gsm, String studentNumber, User userId) {
        this.gsm = gsm;
        this.studentNumber = studentNumber;
        this.userId = userId;
        this.amoutHours = 0;
    }

    public boolean isZap() {
        return zap;
    }

    public void setZap(boolean zap) {
        this.zap = zap;
    }

    public boolean isMobility() {
        return mobility;
    }

    public void setMobility(boolean mobility) {
        this.mobility = mobility;
    }

    public String getGsm() {
        return gsm;
    }

    public void setGsm(String gsm) {
        this.gsm = gsm;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String findEmail() {
        return this.userId.getEmail();
    }


    public Set<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(Set<Assignment> assignments) {
        this.assignments = assignments;
    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public User getUserId() {
        return userId;
    }

    public String getEmail() {
        return userId.getEmail();
    }

    public double getAmoutHours() {
        return this.amoutHours;
    }

    public void setAmoutHours(double amoutHours) {
        this.amoutHours = amoutHours;
    }


}
