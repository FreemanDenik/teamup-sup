package ru.team.up.sup.core.service;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.team.up.sup.core.entity.User;
import ru.team.up.sup.core.entity.UserMessage;
import ru.team.up.sup.core.repositories.UserRepository;

import java.util.Set;


@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class SendMessageServiceImpl implements SendMessageService {
    private UserRepository userRepository;

    @Override
    @Transactional
    public void sendMessage(Set<User> users, UserMessage message) {
        if (users != null) {
            for (User user : users) {
                user.addUserMessage(message);
                userRepository.save(user);
            }
        }
    }

    @Override
    @Transactional
    public void sendMessage(User user, UserMessage message) {
        if (user != null) {
            user.addUserMessage(message);
            userRepository.save(user);
        }
    }
}
