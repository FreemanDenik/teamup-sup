package ru.team.up.sup.core.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.team.up.dto.SupParameterDto;
import ru.team.up.sup.core.entity.Parameter;

/**
 * @author Stepan Glushchenko
 * Сервис для отправки сообщений(парамтеров) в kafka
 */

@Slf4j
@Service
public class KafkaProducerSupServiceImpl implements KafkaProducerSupService {
    /**
     * Имя топика kafka
     */
    @Value(value = "${kafka.topic.name}")
    private String TOPIC;

    /**
     * Шаблон kafka для отправки сообщений
     */
    private KafkaTemplate<String, SupParameterDto<?>> kafkaTemplate;

    @Autowired
    public KafkaProducerSupServiceImpl(KafkaTemplate<String, SupParameterDto<?>> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Отправка парамтера системы через kafka с предварительной конвертацией в DTO объект
     * @param parameter объект для кофигурации работы модулей приложения
     */
    @Override
    public void send(Parameter<?> parameter) {
        if (parameter == null) {
            log.debug("The parameter value is null.");
        } else {
            log.debug("Start sending message: {}", parameter);
            kafkaTemplate.send(TOPIC, SupParameterDto.builder()
                    .parameterName(parameter.getParameterName())
                    .systemName(parameter.getSystemName())
                    .parameterValue(parameter.getParameterValue())
                    .build());
            log.debug("Finished sending message: {}", parameter);
        }
    }
}