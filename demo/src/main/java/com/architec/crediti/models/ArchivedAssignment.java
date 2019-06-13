package com.architec.crediti.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@SequenceGenerator(name="seq", initialValue=223)
@Table(name = "archive")
public class ArchivedAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="seq")
    private long assignmentId;

    @NotNull(message = "Titel mag niet leeg zijn!")
    @NotBlank
    @Column(name = "title")
    private String title;

    @NotNull(message = "Type mag niet leeg zijn!")
    @NotBlank
    @Column(name = "type")
    private String type;

    @NotNull(message = "Beschrijving mag niet leeg zijn!")
    @NotBlank
    @Column(name = "task")
    private String task;

    @NotNull(message = "Totaal uur mag niet leeg zijn!")
    @Column(name = "amountHours")
    private int amountHours;

    @Column(name = "amountStudents")
    private int amountStudents;

    @NotNull(message = "Max studenten mag niet leeg zijn!")
    @Column(name = "maxStudents")
    private int maxStudents;

    @NotNull(message = "Startdatum mag niet leeg zijn!")
    @NotBlank
    @Column(name = "start_date")
    private String startDate;

    @NotNull(message = "Einddatum mag niet leeg zijn!")
    @NotBlank
    @Column(name = "end_date")
    private String endDate;

    @Column(name = "assigner_user")
    private String assignerUser;

    @Column(name = "tag_ids")
    private String tagIds;

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    @Column(name = "tag_name")
    private String tagName;

    public ArchivedAssignment() {
    }

    public ArchivedAssignment(@NotNull(message = "Titel mag niet leeg zijn!") @NotBlank String title, @NotNull(message = "Type mag niet leeg zijn!") @NotBlank String type, @NotNull(message = "Beschrijving mag niet leeg zijn!") @NotBlank String task, @NotNull(message = "Totaal uur mag niet leeg zijn!") int amountHours, @NotNull(message = "Max studenten mag niet leeg zijn!") int maxStudents, @NotNull(message = "Startdatum mag niet leeg zijn!") @NotBlank String startDate, @NotNull(message = "Einddatum mag niet leeg zijn!") @NotBlank String endDate, String assignerUser) {
        this.title = title;
        this.type = type;
        this.task = task;
        this.amountHours = amountHours;
        this.maxStudents = maxStudents;
        this.startDate = startDate;
        this.endDate = endDate;
        this.assignerUser = assignerUser;
    }

    public ArchivedAssignment(@NotNull(message = "Titel mag niet leeg zijn!") @NotBlank String title,
                              @NotNull(message = "Type mag niet leeg zijn!") @NotBlank String type,
                              @NotNull(message = "Beschrijving mag niet leeg zijn!") @NotBlank String task,
                              @NotNull(message = "Totaal uur mag niet leeg zijn!") int amountHours, int amountStudents,
                              @NotNull(message = "Max studenten mag niet leeg zijn!") int maxStudents,
                              @NotNull(message = "Startdatum mag niet leeg zijn!") @NotBlank String startDate,
                              @NotNull(message = "Einddatum mag niet leeg zijn!") @NotBlank String endDate,
                              String assignerUser, String tagIds) {
        this.title = title;
        this.type = type;
        this.task = task;
        this.amountHours = amountHours;
        this.amountStudents = amountStudents;
        this.maxStudents = maxStudents;
        this.startDate = startDate;
        this.endDate = endDate;
        this.assignerUser = assignerUser;
        this.tagIds = tagIds;
    }

    public void fillArchivedAssignment(Assignment a) {
        this.assignmentId = a.getAssignmentId();
        this.title = a.getTitle();
        this.type = a.getType();
        this.task = a.getTask();
        this.amountHours = a.getAmountHours();
        this.amountStudents = a.getAmountStudents();
        this.maxStudents = a.getMaxStudents();
        this.startDate = a.getStartDate();
        this.endDate = a.getEndDate();
        this.assignerUser = a.getAssigner();
    }

    public long getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(long assignmentId) {
        this.assignmentId = assignmentId;
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

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public int getAmountHours() {
        return amountHours;
    }

    public void setAmountHours(int amountHours) {
        this.amountHours = amountHours;
    }

    public int getAmountStudents() {
        return amountStudents;
    }

    public void setAmountStudents(int amountStudents) {
        this.amountStudents = amountStudents;
    }

    public int getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(int maxStudents) {
        this.maxStudents = maxStudents;
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

    public String getAssignerUser() {
        return assignerUser;
    }

    public void setAssignerUser(String assignerUser) {
        this.assignerUser = assignerUser;
    }

    public String getTagIds() {
        return tagIds;
    }

    public void setTagIds(String tagIds) {
        this.tagIds = tagIds;
    }
}
