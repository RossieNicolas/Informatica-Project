package com.architec.demo.assignment;

import javax.persistence.*;

@Entity
@Table(name = "assignments")
public class Assignment {
    private long assignmentId;

    private String title;
    private String type;
    private String task;
    private int amountHours;
    private int amountStudents;
    private int maxStudents;
    private String startDate;
    private String endDate;
    private boolean archived;
    private boolean validated;
    private String extraInfo;
    private String assignerUserId;

    public Assignment() {
    }

    public Assignment(long assignmentId, String title, String type, String task, int amountHours, int amountStudents, int maxStudents, String startDate, String endDate, boolean archived, boolean validated, String extraInfo, String assignerUserId) {
        this.assignmentId = assignmentId;
        this.title = title;
        this.type = type;
        this.task = task;
        this.amountHours = amountHours;
        this.amountStudents = amountStudents;
        this.maxStudents = maxStudents;
        this.startDate = startDate;
        this.endDate = endDate;
        this.archived = archived;
        this.validated = validated;
        this.extraInfo = extraInfo;
        this.assignerUserId = assignerUserId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getAssignmentId() {
        return assignmentId;
    }

    @Column(name = "title", nullable = false)
    public String getTitle() {
        return title;
    }

    @Column(name = "type", nullable = false)
    public String getType() {
        return type;
    }

    @Column(name = "task", nullable = false)
    public String getTask() {
        return task;
    }

    @Column(name = "amount_hours", nullable = false)
    public int getAmountHours() {
        return amountHours;
    }

    @Column(name = "amount_students", nullable = false)
    public int getAmountStudents() {
        return amountStudents;
    }

    @Column(name = "max_students", nullable = false)
    public int getMaxStudents() {
        return maxStudents;
    }

    @Column(name = "start_date", nullable = false)
    public String getStartDate() {
        return startDate;
    }

    @Column(name = "end_date", nullable = false)
    public String getEndDate() {
        return endDate;
    }

    @Column(name = "archived", nullable = false)
    public boolean isArchived() {
        return archived;
    }

    @Column(name = "validated", nullable = false)
    public boolean isValidated() {
        return validated;
    }

    @Column(name = "extra_info", nullable = false)
    public String getExtraInfo() {
        return extraInfo;
    }

    //TODO: Foreign Key
    @Column(name = "assigner_user_id", nullable = false)
    public String getAssignerUserId() {
        return assignerUserId;
    }

    public void setAssignmentId(long assignmentId) {
        this.assignmentId = assignmentId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public void setAmountHours(int amountHours) {
        this.amountHours = amountHours;
    }

    public void setAmountStudents(int amountStudents) {
        this.amountStudents = amountStudents;
    }

    public void setMaxStudents(int maxStudents) {
        this.maxStudents = maxStudents;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public void setAssignerUserId(String assignerUserId) {
        this.assignerUserId = assignerUserId;
    }
}
