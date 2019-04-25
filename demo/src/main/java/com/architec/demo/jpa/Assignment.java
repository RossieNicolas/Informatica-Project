package com.architec.demo.jpa;

import javax.persistence.*;

@Entity
@Table(name = "assignment")
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "assignment_id")
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "type")
    private String type;

    @Column(name = "task")
    private String summary;

    @Column(name = "start_date")
    private String startDate;

    @Column(name = "end_date")
    private String endDate;

    @Column(name = "credit")
    private int credit;

    @Column(name = "amount_hours")
    private int total_hours;

    @Column(name = "max_students")
    private int max_students;

    @Column(name = "extra_info")
    private String extra_info;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public int getTotal_hours() {
        return total_hours;
    }

    public void setTotal_hours(int total_hours) {
        this.total_hours = total_hours;
    }

    public int getMax_students() {
        return max_students;
    }

    public void setMax_students(int max_students) {
        this.max_students = max_students;
    }

    public String getExtra_info() {
        return extra_info;
    }

    public void setExtra_info(String extra_info) {
        this.extra_info = extra_info;
    }

    public Assignment() {
    }

    public Assignment(String title, String type) {
        this.title = title;
        this.type = type;
    }

    public Assignment(String title, String type, String summary, String startDate, String endDate, int credit, int total_hours, int max_students, String extra_info) {
        this.title = title;
        this.type = type;
        this.summary = summary;
        this.startDate = startDate;
        this.endDate = endDate;
        this.credit = credit;
        this.total_hours = total_hours;
        this.max_students = max_students;
        this.extra_info = extra_info;
    }
}
