package com.ecc.company.model;

import java.io.IOException;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

import lombok.Data;

@Data
public class SseEvent {
	
	private SseEmitter emitter;
	private SseEventBuilder event;
	private String eventId;
	private EventTypeEnum eventType;
	private Object eventData;
	
	public SseEvent(SseEmitter emitter) {
		this.emitter = emitter;
	}
	
	public void sendEvent(String eventId, EventTypeEnum eventType, Object eventData) {
		
		event = SseEmitter.event().id(eventId).name(eventType.toString()).data(eventData);
		
		try {
			emitter.send(event);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
	}
	
}
