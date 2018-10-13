package org.myhstry.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Formula;

@Entity
public class Person implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4605704943826014982L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String firstname;
	private String middlenames;
	private String surname;
	private String prefix;
	private String suffix;

	private String gender;

	private Date birth;
	private Date death;
	
	@ManyToOne
	private Person father;
	
	@ManyToOne
	private Person mother;
	
	@Formula("CONCAT(firstname, ' ', middlenames, ' ', surname)")
	private String fullname;

	public Person() {
		
	}
	
	public Person(String firstname2, String middlenames2, String surname2) {
		setFirstname(firstname2);
		setMiddlenames(middlenames2);
		setSurname(surname2);
	}

	public Person(String firstname2, String middlenames2, String surname2, String gender2, String birth2,
			String death2, Person mother2, Person father2) throws ParseException {
		setFirstname(firstname2);
		setMiddlenames(middlenames2);
		setSurname(surname2);
		setGender(gender2);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		if (!birth2.isEmpty())
			setBirth(formatter.parse(birth2));
		if (!death2.isEmpty())
			setDeath(formatter.parse(death2));
		setMother(mother2);
		setFather(father2);
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
	 * @return the firstname
	 */
	public String getFirstname() {
		return firstname;
	}

	/**
	 * @param firstname the firstname to set
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	/**
	 * @return the middlenames
	 */
	public String getMiddlenames() {
		return middlenames;
	}

	/**
	 * @param middlenames the middlenames to set
	 */
	public void setMiddlenames(String middlenames) {
		this.middlenames = middlenames;
	}

	/**
	 * @return the surname
	 */
	public String getSurname() {
		return surname;
	}

	/**
	 * @param surname the surname to set
	 */
	public void setSurname(String surname) {
		this.surname = surname;
	}

	/**
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * @param prefix the prefix to set
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * @return the suffix
	 */
	public String getSuffix() {
		return suffix;
	}

	/**
	 * @param suffix the suffix to set
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * @return the birth
	 */
	public Date getBirth() {
		return birth;
	}

	/**
	 * @param birth the birth to set
	 */
	public void setBirth(Date birth) {
		this.birth = birth;
	}

	/**
	 * @return the death
	 */
	public Date getDeath() {
		return death;
	}

	/**
	 * @param death the death to set
	 */
	public void setDeath(Date death) {
		this.death = death;
	}

	public void setBirth(String string) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		this.birth = formatter.parse(string);
	}

	public void setDeath(String string) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		this.death = formatter.parse(string);		
	}

	/**
	 * @return the father
	 */
	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "father_id", referencedColumnName = "id")
	public Person getFather() {
		return father;
	}

	/**
	 * @param father the father to set
	 */
	public void setFather(Person father) {
		this.father = father;
	}

	/**
	 * @return the mother
	 */
	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "mother_id", referencedColumnName = "id")
	public Person getMother() {
		return mother;
	}

	/**
	 * @param mother the mother to set
	 */
	public void setMother(Person mother) {
		this.mother = mother;
	}

	/**
	 * @return the fullname
	 */
	public String getFullname() {
		return fullname;
	}

}
