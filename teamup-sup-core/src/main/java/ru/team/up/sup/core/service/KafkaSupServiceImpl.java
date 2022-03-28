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
import ru.team.up.sup.core.utils.ParameterToDto;

import java.time.LocalDateTime;
import java.util.List;

/**
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

    /**
     * Отправка парамера системы через kafka с предварительной конвертацией в ListDTO объект
     *
     * @param parameter объект для конфигурации работы модулей приложения
     */
    @Override
    public void send(Parameter parameter) {
        if (parameter == null) {
            log.debug("В метод send вместо параметра пришел null");
        } else {
            log.debug("Начало отправки параметра: {}", parameter);
            ListSupParameterDto listToSend = new ListSupParameterDto();
            listToSend.addParameter(ParameterToDto.convert(parameter));
            listSupParameterDtoKafkaTemplate.send(TOPIC, listToSend);
            log.debug("Завершение отправки параметра: {}", parameter);
        }
    }

    @Override
    public void sendList(List<Parameter> list) {
        if (list.isEmpty()) {
            log.debug("В метод sendList пришел пустой лист.");
        } else {
            log.debug("Начало отправки листа параметров");
            ListSupParameterDto listToSend = new ListSupParameterDto();
            for (Parameter param : list) {
                listToSend.addParameter(ParameterToDto.convert(param));
            }
            listSupParameterDtoKafkaTemplate.send(TOPIC, listToSend);
            log.debug("Завершение отправки листа параметров");
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
            log.debug("В метод delete вместо параметра пришел null");
        } else {
            log.debug("Начало отправки удаленного параметра: {}", parameter);
            SupParameterDto<?> dto = ParameterToDto.convert(parameter);
            dto.setUpdateTime(LocalDateTime.now());
            dto.setDeleted(true);
            ListSupParameterDto listToSend = new ListSupParameterDto();
            listToSend.addParameter(dto);
            listSupParameterDtoKafkaTemplate.send(TOPIC, listToSend);
            log.debug("Завершение отправки удаленного параметра: {}", parameter);
        }
    }

    @Override
    public List<SupParameterDto<?>> getListParameters() {
        return supRepository.findAll();
    }
}