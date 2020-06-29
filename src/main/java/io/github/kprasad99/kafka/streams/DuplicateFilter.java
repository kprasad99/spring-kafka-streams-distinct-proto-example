package io.github.kprasad99.kafka.streams;

import org.apache.kafka.streams.kstream.ValueTransformerWithKey;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;

import io.github.kprasad99.kafka.model.EventProto;

public class DuplicateFilter implements ValueTransformerWithKey<String, EventProto.Event, EventProto.Event> {

	private KeyValueStore<String, Integer> store;
	
	@SuppressWarnings("unchecked")
	@Override
	public void init(ProcessorContext context) {
		store = (KeyValueStore<String, Integer>) context.getStateStore("event-distinct-store");
	}

	@Override
	public EventProto.Event transform(String readOnlyKey, EventProto.Event value) {
		if(readOnlyKey == null) {
			return value;
		}
		if(store.get(readOnlyKey) == null) {
			store.put(readOnlyKey, value.hashCode());
			return value;
		}
		return null;
	}

	@Override
	public void close() {
		
	}

}
