package ru.team.up.sup.core.initialization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import ru.team.up.sup.core.entity.Role;
import ru.team.up.sup.core.entity.User;
import ru.team.up.sup.core.repositories.UserRepository;

import javax.transaction.Transactional;

@Component
@Transactional
public class UsersDefaultCreator {

    private final UserRepository userRepository;

    @Autowired
    public UsersDefaultCreator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Bean("UsersDefaultCreator")
    public void usersDefaultCreator() {
        userRepository.save(User.builder()
                .id(1L)
                .name("User")
                .lastName("DefaultUser")
                .email("user@gmail.com")
                .password(BCrypt.hashpw("user", BCrypt.gensalt(10)))
                .role(Role.ROLE_USER)
                .build());

        userRepository.save(User.builder()
                 .id(2L)
                .name("Admin")
                .lastName("DefaultAdmin")
                .email("admin@gmail.com")
                .password(BCrypt.hashpw("admin", BCrypt.gensalt(10)))
                .role(Role.ROLE_ADMIN)
                .build());
    }
}