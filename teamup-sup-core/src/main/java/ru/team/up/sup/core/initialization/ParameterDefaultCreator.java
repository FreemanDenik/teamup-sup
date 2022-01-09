package ru.team.up.sup.core.initialization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ru.team.up.sup.core.entity.Parameter;
import ru.team.up.sup.core.repositories.ParameterRepository;
import ru.team.up.sup.core.repositories.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static ru.team.up.sup.core.entity.ParameterType.STRING;

@Component
@Transactional
public class ParameterDefaultCreator {

    private ParameterRepository parameterRepository;
    private UserRepository userRepository;

    @Autowired
    public ParameterDefaultCreator(ParameterRepository parameterRepository, UserRepository userRepository) {
        this.parameterRepository = parameterRepository;
        this.userRepository = userRepository;
    }

    @Bean("ParameterDefaultCreator")
    public void parameterDefaultCreator() {
        parameterRepository.save(Parameter.builder()
                .id(1L)
                .parameterName("testName")
                .parameterType(STRING)
                .parameterValue("testValue")
                .systemName("testSystemName")
                .creationDate(LocalDate.now())
                .updateDate(LocalDateTime.now()
//                .userWhoLastChangeParameters(userRepository.getOne(1L)
                ).build()
        );
    }
}
