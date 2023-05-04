package com.example.demokafkacompaction.config;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfigure {

	public static final String TOPIC_NAME = "compact-topic";

	@Bean
	public NewTopic compactTopic() {

		// @formatter:off
        return TopicBuilder
            .name(TOPIC_NAME)
            .compact()
            .config(TopicConfig.MIN_CLEANABLE_DIRTY_RATIO_CONFIG, "0.001")
			.config(TopicConfig.SEGMENT_MS_CONFIG, "5000")
            .build();
        // @formatter:on
	}

	@Bean
	public ConcurrentMap<String, Object> compactDB() {
		return new ConcurrentHashMap<>();
	}

}
