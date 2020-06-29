package io.github.kprasad99.kafka;

import org.junit.jupiter.api.Test;

import io.github.kprasad99.kafka.model.EventProto;
import io.github.kprasad99.kafka.serde.KafkaProtobufDeserializer;
import io.github.kprasad99.kafka.serde.KafkaProtobufSerde;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProtoSerdeTest {

	@Test
	public void protoTest() {
		EventProto.Event event = EventProto.Event.newBuilder().setId("1").setMessage("Hello").setType("info").build();
		log.info(event.getId());
		try (KafkaProtobufSerde<EventProto.Event> serde = new KafkaProtobufSerde<>()) {
			byte[] data = serde.serializer().serialize("topic", event);
			log.info("{}", data);
			KafkaProtobufDeserializer<EventProto.Event> proto = new KafkaProtobufDeserializer<EventProto.Event>(
					EventProto.Event.class);
			log.info("{}", proto.deserialize(null, data).getId());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}
