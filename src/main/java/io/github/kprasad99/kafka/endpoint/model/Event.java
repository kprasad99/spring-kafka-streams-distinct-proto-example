package io.github.kprasad99.kafka.endpoint.model;

import lombok.Data;

@Data
public class Event {

	private String id;
	private String type;
	private String message;
	
}
