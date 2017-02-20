package com.jbossdev.beans;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.persistence.Id;
import javax.validation.constraints.AssertTrue;
import java.io.Serializable;

@Named
@SessionScoped
public class Configuration implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(Configuration.class.getName());
	
	@Id
	private String name;
	
	private int age;
	
	private double income;
	
	private int points;
	
	private Map<String, Integer> pointsOption;
	
	@PostConstruct
	public void init() {
		pointsOption = new HashMap<>(5);
		pointsOption.put("5", 5);
		pointsOption.put("10", 10);
		pointsOption.put("15", 15);
	}
	
	@AssertTrue(message="Validation was incorrect! Please verify input data.")
	public boolean crossValidation() {
		logger.info("On cross validation");
		if (name != null && name.equals("Abraham") && age == 33) {
			return false;
		}
		return true;
	}
	
	public void saveConfiguration() {
		logger.info("Saving configuration");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public double getIncome() {
		return income;
	}

	public void setIncome(double income) {
		this.income = income;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public Map<String, Integer> getPointsOption() {
		return pointsOption;
	}
	
	
}
