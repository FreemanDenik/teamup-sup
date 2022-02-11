package ru.team.up.sup.core.service;


import ru.team.up.sup.core.entity.Parameter;

/**
 * @author Stepan Glushchenko
 * Интерфейс сервиса для отправки парамтеров в kafka
 */

public interface KafkaProducerSupService {
     /**
      * Отправка сообщения в kafka
      * @param parameter объект для кофигурации работы модулей приложения
      */
     void send(Parameter parameter);
}