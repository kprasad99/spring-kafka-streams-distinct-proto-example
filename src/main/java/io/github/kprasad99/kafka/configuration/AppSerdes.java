package io.github.kprasad99.kafka.configuration;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

import com.google.protobuf.MessageLite;

import io.github.kprasad99.kafka.model.EventProto;
import io.github.kprasad99.kafka.serde.KafkaProtobufDeserializer;
import io.github.kprasad99.kafka.serde.KafkaProtobufSerializer;

public class AppSerdes extends Serdes {

	public static class ProtobufSerde<T extends MessageLite> extends Serdes.WrapperSerde<T> {
		public ProtobufSerde() {
			super(new KafkaProtobufSerializer<T>(), new KafkaProtobufDeserializer<T>());
		}

		public ProtobufSerde(Class<T> clazz) {
			super(new KafkaProtobufSerializer<T>(), new KafkaProtobufDeserializer<T>(clazz));
		}
	}

	public static Serde<EventProto.Event> event() {
		var eventSerde = new ProtobufSerde<EventProto.Event>(EventProto.Event.class);
		return eventSerde;
	}
}
