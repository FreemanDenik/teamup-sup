package ru.team.up.sup.core.service;


import ru.team.up.dto.SupParameterDto;
import ru.team.up.sup.core.entity.Parameter;

import java.util.List;

/**
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
     * Отправка нескольких параметров в kafka
     *
     * @param list лист с объектами для кофигурации работы модулей приложения
     */
    void sendList(List<Parameter> list);

    // Откуда сервис должен получать параметры систем если он является единственным управляющим?
    // Если необходимо реализовать механизм обратной связи, то нужно запросить текущие параметры модулей через кафку.
    /**
     * Получение списка параметров ВСЕХ систем
     *
     * @return List<SupParameterDto> - Коллекция с параметрами для кофигурации работы модулей приложения
     */
    List<SupParameterDto<?>> getListParameters();
}