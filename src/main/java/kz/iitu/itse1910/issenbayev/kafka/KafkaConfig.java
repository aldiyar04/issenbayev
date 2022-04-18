package kz.iitu.itse1910.issenbayev.kafka;

import kz.iitu.itse1910.issenbayev.kafka.eventdto.ProjectAssigneesUpdatedDto;
import kz.iitu.itse1910.issenbayev.kafka.eventdto.TicketResolvedDto;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {
    private static final String BOOTSTRAP_SERVER_ADDRESS = "localhost:9092";

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER_ADDRESS);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic resolvedTicketsTopic() {
        return TopicBuilder.name(TopicNames.RESOLVED_TICKETS)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic projectAssigneesUpdatesTopic() {
        return TopicBuilder.name(TopicNames.PROJECTS_ASSIGNEES_UPDATES)
                .partitions(1)
                .replicas(1)
                .build();
    }

    public static class TopicNames {
        public static final String RESOLVED_TICKETS = "resolved-tickets";
        public static final String PROJECTS_ASSIGNEES_UPDATES = "project-assignees-updates";
    }

    @Configuration
    public static class KafkaProducerConfig {
        private static Map<String, Object> kafkaConfigs() {
            Map<String, Object> configs = new HashMap<>();
            configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER_ADDRESS);
            configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
            return configs;
        }

        @Bean
        public ProducerFactory<String, TicketResolvedDto> ticketResolvedProducerFactory() {
            return new DefaultKafkaProducerFactory<>(kafkaConfigs());
        }

        @Bean
        public ProducerFactory<String, ProjectAssigneesUpdatedDto> projectAssigneesUpdatedProducerFactory() {
            return new DefaultKafkaProducerFactory<>(kafkaConfigs());
        }

        @Bean
        public KafkaTemplate<String, TicketResolvedDto> ticketResolvedKafkaTemplate() {
            return new KafkaTemplate<>(ticketResolvedProducerFactory());
        }

        @Bean
        public KafkaTemplate<String, ProjectAssigneesUpdatedDto> projectAssigneesUpdatedKafkaTemplate() {
            return new KafkaTemplate<>(projectAssigneesUpdatedProducerFactory());
        }
    }
}
