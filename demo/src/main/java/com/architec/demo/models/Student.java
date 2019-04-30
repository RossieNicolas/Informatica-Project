package com.architec.demo.models;

import javax.persistence.*;

@Entity
@Table(name = "students")
public class Student {
    @Id
    @Column(name = "student_id")
    private String studentId;

    @Column(name = "class", nullable = false)
    private String studentClass;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REMOVE })
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User userId;

    public Student() {
    }

    public Student(String studentID, String s_class) {
        this.studentId = studentID;
        this.studentClass = s_class;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(String studentClass) {
        this.studentClass = studentClass;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

}
