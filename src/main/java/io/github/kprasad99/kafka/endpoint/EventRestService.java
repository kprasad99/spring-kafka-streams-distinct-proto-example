package io.github.kprasad99.kafka.endpoint;

import java.util.function.Supplier;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.github.kprasad99.kafka.endpoint.model.Event;
import io.github.kprasad99.kafka.model.EventProto;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class EventRestService {

	private final EmitterProcessor<Message<EventProto.Event>> processor = EmitterProcessor.create();

	@Autowired
	private ModelMapper mapper;

	@Bean
	public Supplier<Flux<Message<EventProto.Event>>> sender() {
		return () -> processor;
	}

	@PostMapping("/api/notify")
	public Mono<Void> event(@RequestBody Event event) {
		log.info("Sending message {}", event.getId());
		Message<EventProto.Event> message = MessageBuilder.withPayload(toProto(event)).setHeader("key", event.getId()).build();
		processor.onNext(message);
		return Mono.<Void>empty();
	}

	public EventProto.Event toProto(Event event) {
		return mapper.map(event, EventProto.Event.Builder.class).build();
	}
}
