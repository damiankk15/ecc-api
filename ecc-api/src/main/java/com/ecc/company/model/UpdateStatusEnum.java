package com.ecc.company.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UpdateStatusEnum {
	
	STARTING_WEBDRIVER("Starting webdriver"),
	COUNTING_NUMBER_OF_COMPANIES("Counting number of companies"),
	UPDATING_COMPANIES("Updating companies"),
	COMPLETED("Completed"),
	ERROR("Error");
	
	private String displayName;
	
	@Override
	public String toString() {
		return displayName;
	}
	
}
