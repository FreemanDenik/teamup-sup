package ru.team.up.sup.core.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.team.up.dto.ListSupParameterDto;
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
@Component
public class KafkaSupServiceImpl implements KafkaSupService {
    /**
     * Имя топика kafka
     */
    @Value(value = "${kafka.topic.name}")
    private String TOPIC;

    /**
     * Шаблон kafka для отправки сообщений
     */
    private final KafkaTemplate<String, ListSupParameterDto> listSupParameterDtoKafkaTemplate;
    private final SupRepository supRepository;

    @Autowired
    public KafkaSupServiceImpl(KafkaTemplate<String, ListSupParameterDto> listSupParameterDtoKafkaTemplate,
                               SupRepository supRepository) {
        this.listSupParameterDtoKafkaTemplate = listSupParameterDtoKafkaTemplate;
        this.supRepository = supRepository;
    }

    public SupParameterDto<?> convertToDto(Parameter parameter) {
        return SupParameterDto.builder()
                .parameterName(parameter.getParameterName())
                .systemName(parameter.getSystemName())
                .parameterValue(parameter.getParameterValue())
                .updateTime(parameter.getUpdateDate())
                .isDeleted(false)
                .build();
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
            ListSupParameterDto listToSend = new ListSupParameterDto();
            listToSend.addParameter(convertToDto(parameter));
            listSupParameterDtoKafkaTemplate.send(TOPIC, listToSend);
            log.debug("Finished sending message: {}", parameter);
        }
    }

    @Override
    public void sendList(List<Parameter> list) {
        if (list.isEmpty()) {
            log.debug("The parameter list is empty.");
        } else {
            log.debug("Start sending list");
            ListSupParameterDto listToSend = new ListSupParameterDto();
            for (Parameter param : list) {
                listToSend.addParameter(convertToDto(param));
            }
            listSupParameterDtoKafkaTemplate.send(TOPIC, listToSend);
            log.debug("Finished sending list");
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
            log.debug("Start sending delete message for: {}", parameter);
            SupParameterDto<?> dto = convertToDto(parameter);
            dto.setUpdateTime(LocalDateTime.now());
            dto.setDeleted(true);
            ListSupParameterDto listToSend = new ListSupParameterDto();
            listToSend.addParameter(dto);
            listSupParameterDtoKafkaTemplate.send(TOPIC, listToSend);
            log.debug("Finished sending delete message for: {}", parameter);
        }
    }

    @Override
    public List<SupParameterDto<?>> getListParameters() {
        return supRepository.findAll();
    }
}