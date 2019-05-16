package com.architec.crediti.models;

import javax.persistence.*;

@Entity
@Table(name = "students")
public class Student {
    @Id
    @Column(name = "student_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long studentId;

    @Column(name = "gsm", nullable = false)
    private String gsm;

    @Column(name = "studentennummer", nullable = false)
    private int studentennummer;

    @Column(name = "zap")
    private boolean zap;

    @Column(name = "mobility")
    private boolean mobility;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REMOVE })
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User userId;

    public Student() {
    }

    public Student(String gsm, int studentennummer, User userId) {
        this.gsm = gsm;
        this.studentennummer = studentennummer;
        this.userId = userId;
    }
    public long getStudentId() {
        return this.studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
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

    public int getStudentennummer() {
        return studentennummer;
    }
    public void setStudentennummer(int studentennummer) {
        this.studentennummer = studentennummer;
    }
    public String findEmail(){
        return this.userId.getEmail();
    }


    public User getUserId() {
        return userId;
    }
}
