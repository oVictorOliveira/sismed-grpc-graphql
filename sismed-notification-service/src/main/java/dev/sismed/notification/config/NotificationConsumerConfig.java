package dev.sismed.notification.config;

import dev.sismed.notification.dto.SchedulingDTO;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class NotificationConsumerConfig {
    @Value("${sismed.scheduling-broker.url}")
    private String schedulingBrokerUrl;

    @Value("${sismed.scheduling-broker.group}")
    private String schedulingBrokerGroup;

    @Value("${sismed.scheduling-broker.topic}")
    private String schedulingBrokerTopic;

    @Bean
    public ReceiverOptions<String, SchedulingDTO> receiverOptions() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, schedulingBrokerUrl);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(ConsumerConfig.GROUP_ID_CONFIG, schedulingBrokerGroup);
        config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        config.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        config.put(JsonDeserializer.VALUE_DEFAULT_TYPE, SchedulingDTO.class);

        return ReceiverOptions.<String, SchedulingDTO>create(config)
                .subscription(Collections.singletonList(schedulingBrokerTopic));
    }

    @Bean
    public ReactiveKafkaConsumerTemplate<String, SchedulingDTO> reactiveKafkaConsumerTemplate() {
        return new ReactiveKafkaConsumerTemplate<>(receiverOptions());
    }

    @Bean
    public StringJsonMessageConverter jsonConverter() {
        return new StringJsonMessageConverter();
    }
}
