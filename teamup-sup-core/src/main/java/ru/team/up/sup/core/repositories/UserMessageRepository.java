package ru.team.up.sup.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.team.up.sup.core.entity.User;
import ru.team.up.sup.core.entity.UserMessage;
import ru.team.up.sup.core.entity.UserMessageType;

import java.util.List;

/**
 * @author Alexey Tkachenko
 */
public interface UserMessageRepository extends JpaRepository<UserMessage, Long> {
    UserMessage findAllByMessageOwner(User user);

    List<UserMessage> findAllByMessageType(UserMessageType userMessageType);
}
