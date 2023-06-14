package com.example.demokafkacompaction.listener;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.ConsumerSeekAware;
import org.springframework.stereotype.Component;

import com.example.demokafkacompaction.config.KafkaConfigure;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CompactTopicListener implements ConsumerSeekAware {

	private final ConcurrentMap<String, Object> compactDB;

	private final ObjectMapper mapper;

	@KafkaListener(id = "k1", topics = KafkaConfigure.TOPIC_NAME)
	public void onMessage(ConsumerRecord<String, String> record) throws JsonProcessingException {
		if (record.value() == null) {
			compactDB.remove(record.key());
		}
		else {
			compactDB.put(record.key(), mapper.readTree(record.value()));
		}
		// log.info("[REV]-> {}", record.value());
	}

	@Override
	public void onPartitionsAssigned(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {
		// @formatter:off
        assignments.keySet()
            .stream()
            .filter(p -> KafkaConfigure.TOPIC_NAME.equals(p.topic()))
            .forEach(p -> callback.seekToBeginning(p.topic(), p.partition()));
        // @formatter:on
	}

}
