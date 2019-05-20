package com.architec.crediti.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "archive")
public class ArchivedAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    private String start_date;

    @NotNull(message = "Einddatum mag niet leeg zijn!")
    @NotBlank
    @Column(name = "end_date")
    private String end_date;

    @Column(name = "assigner_user")
    private String assignerUser;

    @Column(name="tag_ids")
    private String tag_ids;

    public ArchivedAssignment() {
    }

    public ArchivedAssignment(@NotNull(message = "Titel mag niet leeg zijn!") @NotBlank String title, @NotNull(message = "Type mag niet leeg zijn!") @NotBlank String type, @NotNull(message = "Beschrijving mag niet leeg zijn!") @NotBlank String task, @NotNull(message = "Totaal uur mag niet leeg zijn!") int amountHours, int amountStudents, @NotNull(message = "Max studenten mag niet leeg zijn!") int maxStudents, @NotNull(message = "Startdatum mag niet leeg zijn!") @NotBlank String start_date, @NotNull(message = "Einddatum mag niet leeg zijn!") @NotBlank String end_date, String assignerUser, String tag_ids) {
        this.title = title;
        this.type = type;
        this.task = task;
        this.amountHours = amountHours;
        this.amountStudents = amountStudents;
        this.maxStudents = maxStudents;
        this.start_date = start_date;
        this.end_date = end_date;
        this.assignerUser = assignerUser;
        this.tag_ids = tag_ids;
    }

    public void fillArchivedAssignment(Assignment a){
        this.assignmentId = a.getAssignmentId();
        this.title = a.getTitle();
        this.type = a.getType();
        this.task = a.getTask();
        this.amountHours = a.getAmountHours();
        this.amountStudents = a.getAmountStudents();
        this.maxStudents = a.getMaxStudents();
        this.start_date = a.getStart_date();
        this.end_date = a.getEnd_date();
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

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getAssignerUser() {
        return assignerUser;
    }

    public void setAssignerUser(String assignerUser) {
        this.assignerUser = assignerUser;
    }

    public String getTag_ids() {
        return tag_ids;
    }

    public void setTag_ids(String tag_ids) {
        this.tag_ids = tag_ids;
    }
}
