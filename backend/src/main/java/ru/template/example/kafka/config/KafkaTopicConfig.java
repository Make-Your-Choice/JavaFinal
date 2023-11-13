package ru.template.example.kafka.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    /*@Bean
    public NewTopic topic1() {
        return new NewTopic("documents", 1, (short) 1);
    }*/

    @Bean
    public NewTopic topic1() {
        return new NewTopic("docs", 1, (short) 1);
    }

    @Bean
    public NewTopic topic_in() {
        return new NewTopic("docs_in", 1, (short) 1);
    }

    @Bean
    public NewTopic topic_out() {
        return new NewTopic("docs_out", 1, (short) 1);
    }
}