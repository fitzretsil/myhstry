package org.myhstry.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Type;

@Entity
public class Event {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String type;
	
	private int day;
	private int month;
	private int year;
	
	@Column(name = "fl", columnDefinition="TINYINT(1)", nullable = false)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean fl;
	
	private static final String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
	
	public Event() {
		
	}
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the day
	 */
	public int getDay() {
		return day;
	}
	/**
	 * @param day the day to set
	 */
	public void setDay(int day) {
		this.day = day;
	}
	/**
	 * @return the month
	 */
	public int getMonth() {
		return month;
	}
	/**
	 * @param month the month to set
	 */
	public void setMonth(int month) {
		this.month = month;
	}
	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}
	/**
	 * @param year the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}
	
	public boolean isFl() {
		return fl;
	}

	public void setFl(boolean fl) {
		this.fl = fl;
	}

	public String toString() {
		if (getDay() + getMonth() + getYear() == 0) {
			return "Unknown";
		} else if (isFl()) {
			return "fl" + getYear();
		} else if (getDay() == 0 || getMonth() == 0) {
			return getYear() + "";
		} else if (getDay() == 0) {
			return months[getMonth()-1] + " " + getYear();
		}
		return getDay() + " " + months[getMonth()-1] + " " + getYear();
	}
}
