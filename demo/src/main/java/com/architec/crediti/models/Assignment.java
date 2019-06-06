package com.architec.crediti.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;
@Entity
@SequenceGenerator(name="seq", initialValue=1050)
@Table(name = "assignments")
public class Assignment {
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

    @NotNull
    @Column(name = "archived")
    private boolean archived;

    @NotNull
    @Column(name = "validated")
    private boolean validated;

    @Column(name = "amount_students_full")
    private boolean full;

    @ManyToMany
    @JoinTable(
            name = "tag_assign",
            joinColumns = @JoinColumn(name = "assign_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags;

    @ManyToMany(mappedBy = "assignments")
    private Set<Student> assignStudents;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "assigner_user_id", referencedColumnName = "user_id")
    private User assignerUserId;

    public Assignment() {
    }

    public Assignment(String title, String type, String task, int amountHours, int maxStudents, String startDate,
                      String endDate, boolean archived, boolean validated, User assignerUserId) {
        this.title = title;
        this.type = type;
        this.task = task;
        this.amountHours = amountHours;
        this.maxStudents = maxStudents;
        this.startDate = startDate;
        this.endDate = endDate;
        this.archived = archived;
        this.validated = validated;
        this.assignerUserId = assignerUserId;
    }

    public long getAssignmentId() {
        return assignmentId;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getTask() {
        return task;
    }

    public int getAmountHours() {
        return amountHours;
    }

    public int getAmountStudents() {
        return amountStudents;
    }

    public int getMaxStudents() {
        return maxStudents;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getFormatStartDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        startDate= LocalDate.parse(startDate, formatter2).format(formatter);
        return startDate;
    }

    public String getFormatEndDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        endDate= LocalDate.parse(endDate, formatter2).format(formatter);
        return endDate;
    }

    public boolean isArchived() {
        return archived;
    }

    public boolean isValidated() {
        return validated;
    }

    public long getAssignerUserId() {
        return assignerUserId.getUserId();
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

    public String getAssigner() {
        return assignerUserId.getFirstname() + " " + assignerUserId.getLastname();
    }

    public String getAssignerEmail() {
        return "Naam: " + assignerUserId.getFirstname() + " " + assignerUserId.getLastname() + " \t Email: " + assignerUserId.getEmail();
    }


    public void setAssignerUserId(User assignerUserId) {
        this.assignerUserId = assignerUserId;
    }

    public Set<Student> getAssignStudents() {
        return assignStudents;
    }

    public void setAssignStudents(Set<Student> assignStudents) {
        this.assignStudents = assignStudents;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }
    public String getEmail(){
        return assignerUserId.getEmail();
    }
    public String getName(){
        return assignerUserId.getFirstname() + " " + assignerUserId.getLastname();
    }

    public boolean isFull() {
        return full;
    }

    public void setFull(boolean full) {
        this.full = full;
    }
}
