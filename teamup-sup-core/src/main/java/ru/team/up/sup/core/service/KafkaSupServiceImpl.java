package ru.team.up.sup.core.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.team.up.dto.AppModuleNameDto;
import ru.team.up.dto.ListSupParameterDto;
import ru.team.up.dto.SupParameterDto;
import ru.team.up.sup.core.entity.Parameter;
import ru.team.up.sup.core.utils.ParameterToDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервис для отправки параметров в kafka
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
     * Шаблон kafka для отправки параметров
     */
    private final KafkaTemplate<AppModuleNameDto, ListSupParameterDto> listSupParameterDtoKafkaTemplate;


    @Autowired
    public KafkaSupServiceImpl(KafkaTemplate<AppModuleNameDto, ListSupParameterDto> listSupParameterDtoKafkaTemplate) {
        this.listSupParameterDtoKafkaTemplate = listSupParameterDtoKafkaTemplate;
    }

    /**
     * Отправка одного параметра в kafka с предварительной конвертацией в ListDTO объект
     *
     * @param parameter объект для конфигурации работы модулей приложения
     */
    @Override
    public void send(Parameter parameter) {
        log.debug("Начало отправки параметра: {}", parameter);
        if (parameter == null) {
            log.debug("В метод send вместо параметра пришел null");
            throw new RuntimeException("В метод send вместо параметра пришел null");
        }
        ListSupParameterDto listToSend = new ListSupParameterDto();
        listToSend.addParameter(ParameterToDto.convert(parameter));
        listSupParameterDtoKafkaTemplate.send(TOPIC, listToSend.getModuleName(), listToSend);
        log.debug("Завершение отправки параметра: {}", parameter);
    }

    /**
     * Отправка листа параметров в kafka с предварительной конвертацией в ListDTO объект
     *
     * @param list объект для конфигурации работы модулей приложения
     */
    @Override
    public void send(List<Parameter> list) {
        log.debug("Начало отправки листа параметров");
        if (list == null || list.isEmpty()) {
            log.debug("В метод send пришел пустой лист или null.");
            throw new RuntimeException("В метод send пришел пустой лист или null.");
        }
        for (ListSupParameterDto listToSend : ParameterToDto.parseParameterListToListsDto(list)) {
            listSupParameterDtoKafkaTemplate.send(TOPIC, listToSend.getModuleName(), listToSend);
            log.debug("Отправлены параметры в модуль {}", listToSend.getModuleName());
        }
//        ListSupParameterDto listToSend = new ListSupParameterDto();
//        for (Parameter param : list) {
//            listToSend.addParameter(ParameterToDto.convert(param));
//        }
//        listSupParameterDtoKafkaTemplate.send(TOPIC, listToSend.getModuleName(), listToSend);
        log.debug("Завершение отправки листа параметров");
    }

    /**
     * Удаление парамера системы через kafka с предварительной конвертацией в DTO объект
     * и его отправка с параметром isDeleted = true
     *
     * @param parameter -
     */
    @Override
    public void delete(Parameter parameter) {
        log.debug("Начало отправки удаленного параметра: {}", parameter);
        if (parameter == null) {
            log.debug("В метод delete вместо параметра пришел null.");
            throw new RuntimeException("В метод delete вместо параметра пришел null.");
        } else {
            SupParameterDto<?> dto = ParameterToDto.convert(parameter);
            dto.setUpdateTime(LocalDateTime.now());
            dto.setDeleted(true);
            ListSupParameterDto listToSend = new ListSupParameterDto();
            listToSend.addParameter(dto);
            listSupParameterDtoKafkaTemplate.send(TOPIC, listToSend.getModuleName(), listToSend);
            log.debug("Завершение отправки удаленного параметра: {}", parameter);
        }
    }
}