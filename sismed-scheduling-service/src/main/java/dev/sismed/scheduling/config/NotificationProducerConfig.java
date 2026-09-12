package dev.sismed.scheduling.config;

import dev.sismed.scheduling.dto.SchedulingDTO;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;
import reactor.kafka.sender.SenderOptions;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class NotificationProducerConfig {
    @Value("${sismed.scheduling-broker.url}")
    private String schedulingBrokerUrl;

    @Bean
    public SenderOptions<String, SchedulingDTO> senderOptions() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, schedulingBrokerUrl);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return SenderOptions.create(config);
    }

    @Bean
    public ReactiveKafkaProducerTemplate<String, SchedulingDTO> reactiveKafkaProducerTemplate() {
        return new ReactiveKafkaProducerTemplate<>(senderOptions());
    }
}
