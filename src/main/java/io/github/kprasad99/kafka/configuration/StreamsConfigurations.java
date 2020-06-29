package io.github.kprasad99.kafka.configuration;

import java.util.function.Consumer;

import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.kprasad99.kafka.model.EventProto;
import io.github.kprasad99.kafka.streams.DuplicateFilter;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class StreamsConfigurations {

	@Bean
	public StoreBuilder<KeyValueStore<String, Integer>> myStore() {
		return Stores.keyValueStoreBuilder(
				Stores.persistentKeyValueStore("event-distinct-store"), AppSerdes.String(),
				AppSerdes.Integer());
	}
	
	@Bean
	public Consumer<KStream<String, EventProto.Event>> distinct(){
		return (streams) -> streams.transformValues(() -> new DuplicateFilter(), "event-distinct-store")
				.filter((k, v) -> v != null).peek((k, v) -> log.info("received event with id {}, with value {}", k, v));
	}
	
}
