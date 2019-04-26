package com.architec.demo.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "assignments")
public class Assignment {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long assignmentId;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "type", nullable = false)
	private String type;

	@Column(name = "task", nullable = false)
	private String task;

	@Column(name = "amount_hours", nullable = false)
	private int amountHours;

	@Column(name = "amount_students")
	private int amountStudents;

	@Column(name = "max_students", nullable = false)
	private int maxStudents;

	@Column(name = "start_date", nullable = false)
//    @Temporal(TemporalType.DATE)
	private String startDate;

	@Column(name = "end_date", nullable = false)
//    @Temporal(TemporalType.DATE)
	private String endDate;

	@Column(name = "archived", nullable = false)
	private boolean archived;

	@Column(name = "validated", nullable = false)
	private boolean validated;

	@Column(name = "extra_info", nullable = false)
	private String extraInfo;

	@ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.REMOVE })
	@JoinColumn(name = "assigner_user_id", referencedColumnName = "user_id")
	private User assignerUserId;

	public Assignment() {
	}

	public Assignment(String title, String type, String task, int amountHours, int amountStudents, int maxStudents,
                      String startDate, String endDate, boolean archived, boolean validated, String extraInfo,
			User assignerUserId) {
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

	public boolean isArchived() {
		return archived;
	}

	public boolean isValidated() {
		return validated;
	}

	public String getExtraInfo() {
		return extraInfo;
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

	public void setExtraInfo(String extraInfo) {
		this.extraInfo = extraInfo;
	}

	public void setAssignerUserId(User assignerUserId) {
		this.assignerUserId = assignerUserId;
	}
}
