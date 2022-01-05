package ru.team.up.sup.core.service;


import ru.team.up.sup.core.entity.User;
import ru.team.up.sup.core.entity.UserMessage;

import java.util.Set;

/**
 * @author Stanislav Ivashchenko
 * Сервис для отправки сообщений пользователям
 */
public interface SendMessageService {

    void sendMessage(User user, UserMessage message);

    void sendMessage(Set<User> users, UserMessage message);
}
