package com.ecc.company.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Message {
	
	private String messageType;
	private String header;
	private String message;
	
}
