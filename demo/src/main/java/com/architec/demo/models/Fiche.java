package com.architec.demo.models;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Fiche {
	@Id
	@GeneratedValue
	private int ficheID;

	@Column
	private int binnenland;

	@Column
	private String Title;

	@Column
	private String Description;

	@Column
	private int amount_hours;

	@Column
	private int amount_students;

	@Column
	private Date start_date;

	@Column
	private Date end_date;

	@Column
	private String given_by;

	public Fiche() {
	}

	public Fiche(String title, int binnenland, String description, int amount_hours, int amount_students,
			Date start_date, Date end_date, String given_by) {

	}

	public int getFicheID() {
		return ficheID;
	}

	public void setFicheID(int ficheID) {
		this.ficheID = ficheID;
	}

	public int getBinnenland() {
		return binnenland;
	}

	public void setBinnenland(int binnenland) {
		this.binnenland = binnenland;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
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

}