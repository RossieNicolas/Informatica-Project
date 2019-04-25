package com.architec.demo.models;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.scheduling.config.Task;

@Entity
public class Fiche {
	@Id
	@GeneratedValue
	private long ficheID;

	@Column
	private String type;

	@Column
	private String title;

	@Column
	private String task;

	@Column
	private int amount_hours;

	@Column
	private int amount_students;

	@Column
	private int max_students;

	@Column
	private Date start_date;

	@Column
	private Date end_date;

	@Column
	private String given_by;

	@Column
	private Boolean Validated;

	@Column
	private Boolean Archived;

	public Fiche() {
	}

	public Fiche(String title, int binnenland, String description, int amount_hours, int amount_students,
			Date start_date, Date end_date, String given_by) {

	}

	public String getType() {
		return type;
	}

	public void setType(String binnenland) {
		this.type = binnenland;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTask() {
		return task;
	}

	public void setTask(String description) {
		this.task = description;
	}

	public int getAmount_hours() {
		return amount_hours;
	}

	public void setAmount_hours(int amount_hours) {
		this.amount_hours = amount_hours;
	}

	public int getAmount_students() {
		return amount_students;
	}

	public void setAmount_students(int amount_students) {
		this.amount_students = amount_students;
	}

	public Date getStart_date() {
		return start_date;
	}

	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	public Date getEnd_date() {
		return end_date;
	}

	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

	public String getGiven_by() {
		return given_by;
	}

	public void setGiven_by(String given_by) {
		this.given_by = given_by;
	}

	public int getMax_students() {
		return max_students;
	}

	public void setMax_students(int max_students) {
		this.max_students = max_students;
	}

	public Boolean getValidated() {
		return Validated;
	}

	public void setValidated(Boolean validated) {
		Validated = validated;
	}

	public Boolean getArchived() {
		return Archived;
	}

	public void setArchived(Boolean archived) {
		Archived = archived;
	}

	/**
	 * @return the ficheID
	 */
	public long getFicheID() {
		return ficheID;
	}

}