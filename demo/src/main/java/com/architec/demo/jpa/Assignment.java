package com.architec.demo.jpa;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "assignment")
public class Assignment {
    @Id
    @GeneratedValue
    @Column(name = "assignment_id")
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "type")
    private String type;

    @Column(name = "task")
    private String summary;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
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
}
