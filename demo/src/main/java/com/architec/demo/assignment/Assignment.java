package com.architec.demo.assignment;

import javax.persistence.*;

@Entity
@Table(name = "assignments")
public class Assignment {
//    @Id
//    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long assignmentId;

    private String title;
    private String type;
//    private String task;
//    private int amountHours;
//    private int amountStudents;
//    private String startDate;
//    private String endDate;
//    private boolean archived;
//    private boolean validated;
//    private String extraInfo;
//    private String assignerUserId;

    public Assignment() {
    }

    public Assignment(long assignmentId, String title, String type) {
        this.assignmentId = assignmentId;
        this.title = title;
        this.type = type;
    }

//    public Assignment(String title, String type, String task, int amountHours, int amountStudents, String startDate, String endDate, boolean archived, boolean validated, String extraInfo, String assignerUserId) {
//        this.title = title;
//        this.type = type;
//        this.task = task;
//        this.amountHours = amountHours;
//        this.amountStudents = amountStudents;
//        this.startDate = startDate;
//        this.endDate = endDate;
//        this.archived = archived;
//        this.validated = validated;
//        this.extraInfo = extraInfo;
//        this.assignerUserId = assignerUserId;
//    }

//    public Assignment(int assignmentId, String title, String type, String task, int amountHours, int amountStudents, String startDate, String endDate, boolean archived, boolean validated, String extraInfo, String assignerUserId) {
//        this.assignmentId = assignmentId;
//        this.title = title;
//        this.type = type;
//        this.task = task;
//        this.amountHours = amountHours;
//        this.amountStudents = amountStudents;
//        this.startDate = startDate;
//        this.endDate = endDate;
//        this.archived = archived;
//        this.validated = validated;
//        this.extraInfo = extraInfo;
//        this.assignerUserId = assignerUserId;
//    }
//
//    public Assignment(int ficheID, String titel, String soort, String omschrijving, int aantalUren, int aantalStudenten, String beginDatum, String eindDatum, String opdrachtgever) {
//        this.assignmentId = ficheID;
//        this.title = titel;
//        this.type = soort;
//        this.task = omschrijving;
//        this.amountHours = aantalUren;
//        this.amountStudents = aantalStudenten;
//        this.startDate = beginDatum;
//        this.endDate = eindDatum;
//        this.assignerUserId = opdrachtgever;
//    }

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

    public void setAssignmentId(long assignmentId) {
        this.assignmentId = assignmentId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

//    public String getTask() {
//        return task;
//    }
//
//    public int getAmountHours() {
//        return amountHours;
//    }
//
//    public int getAmountStudents() {
//        return amountStudents;
//    }
//
//    public String getStartDate() {
//        return startDate;
//    }
//
//    public String getEndDate() {
//        return endDate;
//    }
//
//    public String getAssignerUserId() {
//        return assignerUserId;
//    }
//
//    public boolean isArchived() {
//        return archived;
//    }
//
//    public boolean isValidated() {
//        return validated;
//    }
//
//    public String getExtraInfo() {
//        return extraInfo;
//    }
}
