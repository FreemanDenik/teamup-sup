package ru.team.up.sup.core.initialization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ru.team.up.sup.core.entity.Parameter;
import ru.team.up.sup.core.repositories.AdminRepository;
import ru.team.up.sup.core.repositories.ParameterRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@Transactional
public class ParameterDefaultCreator {

    private ParameterRepository parameterRepository;
    private AdminRepository adminRepository;

    @Autowired
    public ParameterDefaultCreator(ParameterRepository parameterRepository, AdminRepository adminRepository) {
        this.parameterRepository = parameterRepository;
        this.adminRepository = adminRepository;
    }

    @Bean("ParameterDefaultCreator")
    public void parameterDefaultCreator() {
        parameterRepository.save(Parameter.builder()
                .id(1L)
                .parameterName("testName")
                .parameterValue("testValue")
                .systemName("testSystemName")
                .creationDate(LocalDate.now())
                .updateDate(LocalDateTime.now())
                .userWhoLastChangeParameters(adminRepository.getOne(1L)
                ).build()
        );

    }

}
