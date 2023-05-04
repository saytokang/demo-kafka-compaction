package com.example.demokafkacompaction.web;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demokafkacompaction.config.KafkaConfigure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/kafka")
@RequiredArgsConstructor
public class KafkaController {

	private final ConcurrentMap<String, Object> compactDB;

	private final KafkaTemplate<String, String> kafkaTemplate;

	@PutMapping("/send/{key}/{value}")
	public ResponseEntity<?> send(@PathVariable String key, @PathVariable String value)
			throws InterruptedException, ExecutionException, TimeoutException {
		var future = kafkaTemplate.send(KafkaConfigure.TOPIC_NAME, key, value);
		// .whenComplete((rs, ex) -> {
		// if (ex != null)
		// log.error("{}", ex.getMessage());
		// else
		// log.warn("offset: {}", rs.getRecordMetadata().offset());
		// });
		var rs = future.get(5, TimeUnit.SECONDS);
		log.warn("offset : {}", rs.getRecordMetadata().offset());

		return ResponseEntity.ok().body("offset: " + rs.getRecordMetadata().offset());
	}

	@DeleteMapping("/db/{key}")
	public ResponseEntity<?> afterDelete(@PathVariable String key)
			throws InterruptedException, ExecutionException, TimeoutException {
		var future = kafkaTemplate.send(KafkaConfigure.TOPIC_NAME, key, null);
		var rs = future.get(5, TimeUnit.SECONDS);
		log.warn("offset : {}", rs.getRecordMetadata().offset());

		return ResponseEntity.ok().body("offset: " + rs.getRecordMetadata().offset());
	}

	@GetMapping("/db")
	public ResponseEntity<?> status() {
		return ResponseEntity.ok().body(compactDB);
	}

}
