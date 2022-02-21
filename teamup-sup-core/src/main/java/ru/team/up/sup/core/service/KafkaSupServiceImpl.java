package ru.team.up.sup.core.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.team.up.dto.SupParameterDto;
import ru.team.up.sup.core.entity.Parameter;
import ru.team.up.sup.core.repositories.SupRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Stepan Glushchenko
 * Сервис для отправки сообщений(парамтеров) в kafka
 */

@Slf4j
@Service
public class KafkaSupServiceImpl implements KafkaSupService {
    /**
     * Имя топика kafka
     */
    @Value(value = "${kafka.topic.name}")
    private String TOPIC;

    /**
     * Шаблон kafka для отправки сообщений
     */
    private KafkaTemplate<String, SupParameterDto<?>> kafkaTemplate;
    private SupRepository supRepository;

    public KafkaSupServiceImpl(KafkaTemplate<String, SupParameterDto<?>> kafkaTemplate, SupRepository supRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.supRepository = supRepository;
    }

    /**
     * Отправка парамера системы через kafka с предварительной конвертацией в DTO объект
     *
     * @param parameter объект для конфигурации работы модулей приложения
     */
    @Override
    public void send(Parameter parameter) {
        if (parameter == null) {
            log.debug("The parameter value is null.");
        } else {
            log.debug("Start sending message: {}", parameter);
            kafkaTemplate.send(TOPIC, SupParameterDto.builder()
                    .parameterName(parameter.getParameterName())
                    .systemName(parameter.getSystemName())
                    .parameterValue(parameter.getParameterValue())
                    .updateTime(parameter.getUpdateDate())
                    .isDeleted(false)
                    .build());
            log.debug("Finished sending message: {}", parameter);
        }
    }

    /**
     * Удаление парамера системы через kafka с предварительной конвертацией в DTO объект
     * и его отправка с параметром isDeleted = true
     *
     * @param parameter -
     */
    @Override
    public void delete(Parameter parameter) {
        if (parameter == null) {
            log.debug("The parameter value is null.");
        } else {
            log.debug("Start sending delete message: {}", parameter);
            kafkaTemplate.send(TOPIC, SupParameterDto.builder()
                    .parameterName(parameter.getParameterName())
                    .systemName(parameter.getSystemName())
                    .parameterValue(parameter.getParameterValue())
                    .updateTime(LocalDateTime.now())
                    .isDeleted(true)
                    .build());
            log.debug("Finished sending message: {}", parameter);
        }
    }

    @Override
    public List<SupParameterDto> getListParameters() {
        return supRepository.findAll();
    }
}