package dev.sismed.scheduling.service;

import dev.sismed.scheduling.dto.SchedulingDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationProducerService {
    @Value("${sismed.scheduling-broker.topic}")
    private String notificationBrokerTopic;

    private final Logger log = LoggerFactory.getLogger(NotificationProducerService.class);

    private final ReactiveKafkaProducerTemplate<String, SchedulingDTO> producerTemplate;

    public NotificationProducerService(ReactiveKafkaProducerTemplate<String, SchedulingDTO> producerTemplate) {
        this.producerTemplate = producerTemplate;
    }

    public void sendSchedulingNotification(SchedulingDTO schedulingDTO) {
        producerTemplate.send(notificationBrokerTopic, schedulingDTO)
                .doOnSuccess(senderResult -> log.info("Notification sent for scheduling: {}", schedulingDTO))
                .subscribe();
    }
}
