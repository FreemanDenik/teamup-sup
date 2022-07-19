package ru.team.up.sup.input;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import ru.team.up.dto.AppModuleNameDto;
import ru.team.up.dto.SupParameterType;
import ru.team.up.sup.core.entity.Parameter;
import ru.team.up.sup.core.entity.User;
import ru.team.up.sup.core.utils.ParameterToDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@ComponentScan({"ru.team.up.sup.input", "ru.team.up.sup.core"})
public class TeamupInputApplication {
    public static void main(String[] args) {
        SpringApplication.run(TeamupInputApplication.class, args);
    }
}
