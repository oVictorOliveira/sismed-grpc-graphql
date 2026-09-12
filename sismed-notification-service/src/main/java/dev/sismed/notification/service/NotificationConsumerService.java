package dev.sismed.notification.service;

import dev.sismed.notification.dto.SchedulingDTO;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumerService {
    private final Logger log = LoggerFactory.getLogger(NotificationConsumerService.class);

    private final ReactiveKafkaConsumerTemplate<String, SchedulingDTO> consumerTemplate;

    public NotificationConsumerService(ReactiveKafkaConsumerTemplate<String, SchedulingDTO> consumerTemplate) {
        this.consumerTemplate = consumerTemplate;
    }

    @PostConstruct
    public void processSchedulingNotification() {
        consumerTemplate.receive()
                .limitRate(10000)
                .doOnNext(msg -> log.info("Schedule Notification Received: {}", msg.value()))
                .doOnNext(message -> message.receiverOffset().acknowledge())
                .doOnError(error -> log.error("Notification error: {}", error.getMessage()))
                .subscribe();
    }
}
