package ru.team.up.sup.core.config;


import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.team.up.dto.SupParameterDto;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Stepan Glushchenko
 * Конфигурация producer kafka
 */

@Configuration
@PropertySource("classpath:application.properties")
public class KafkaProducerSupConfig {
    /**
     * Адрес bootstrap сервера kafka
     */
    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    /**
     * Конфигурация фабрики производителей
     *
     * @return возвращает объект класса org.springframework.kafka.core.DefaultKafkaProducerFactory
     */
    @Bean
    public ProducerFactory<String, SupParameterDto<?>> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    /**
     * @return возвращает объект org.springframework.kafka.core.KafkaTemplate
     */
    @Bean
    @Qualifier("kafkaTemplate")
    public KafkaTemplate<String, SupParameterDto<?>> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}