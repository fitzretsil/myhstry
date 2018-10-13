package org.myhstry.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Marriage {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	private Person husband;
	
	@ManyToOne
	private Person wife;
	
	public Marriage() {
		
	}

	public Marriage(Person husband, Person wife) {
		setHusband(husband);
		setWife(wife);
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the husband
	 */
	public Person getHusband() {
		return husband;
	}

	/**
	 * @param husband the husband to set
	 */
	public void setHusband(Person husband) {
		this.husband = husband;
	}

	/**
	 * @return the wife
	 */
	public Person getWife() {
		return wife;
	}

	/**
	 * @param wife the wife to set
	 */
	public void setWife(Person wife) {
		this.wife = wife;
	}
}
