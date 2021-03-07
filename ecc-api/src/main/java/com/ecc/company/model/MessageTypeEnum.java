package com.ecc.company.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MessageTypeEnum {
	
	PRIMARY("bg"),
	SECONDARY("bg"),
	SUCCESS("bg"),
	DANGER("bg"),
	WARNING("bg"),
	INFO("bg");
	
	private String displayName;
	
	@Override
	public String toString() { 
		return displayName;
	}
	
}
