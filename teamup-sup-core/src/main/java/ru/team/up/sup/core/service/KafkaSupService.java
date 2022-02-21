package ru.team.up.sup.core.service;


import ru.team.up.dto.SupParameterDto;
import ru.team.up.sup.core.entity.Parameter;

import java.util.List;

/**
 * @author Stepan Glushchenko
 * Интерфейс сервиса для отправки парамтеров в kafka
 */

public interface KafkaSupService {
    /**
     * Отправка сообщения в kafka
     *
     * @param parameter объект для кофигурации работы модулей приложения
     */
    void send(Parameter parameter);

    /**
     * Отправляет в kafka сообщение состоящее из ParameterDTO с  полем isDeleted - true
     *
     * @param parameter объект для кофигурации работы модулей приложения
     */
    void delete(Parameter parameter);

    /**
     * Получение списка параметров ВСЕХ систем
     *
     * @return List<SupParameterDto> - Коллекция с параметрами для кофигурации работы модулей приложения
     */
    List<SupParameterDto> getListParameters();
}