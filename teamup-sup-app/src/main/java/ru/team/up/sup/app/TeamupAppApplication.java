package ru.team.up.sup.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
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
@ComponentScan({"ru.team.up.sup"})
@EnableJpaRepositories("ru.team.up.sup.core.repositories")
@EntityScan("ru.team.up.sup.core")
@PropertySource({
        "classpath:db.properties",
        "classpath:auth.properties",
        "classpath:kafka-sup.properties"
})
@EnableWebMvc
public class TeamupAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(TeamupAppApplication.class, args);

        List<String> stringList1 = new ArrayList<>();
        List<String> stringList2 = new ArrayList<>();
        List<String> stringList3 = new ArrayList<>();
        List<String> stringList4 = new ArrayList<>();

        stringList1.addAll(Arrays.asList("1", "2", "3"));
        stringList2.addAll(Arrays.asList("true", "true", "false"));
        stringList3.addAll(Arrays.asList("1", "2", "3"));
        stringList4.addAll(Arrays.asList("a", "b", "c"));

        User testUser = User.builder()
                .id(1L)
                .name("TestName")
                .lastName("TestLastName")
                .email("test@mail.com")
                .password("testPass")
                .lastAccountActivity(LocalDateTime.now())
                .build();

        Parameter parameter1 = Parameter.builder()
                .id(1L)
                .parameterName("TestParam1")
                .parameterType(SupParameterType.DOUBLE)
                .systemName(AppModuleNameDto.TEAMUP_CORE)
                .isList(false)
                .parameterValue(stringList1)
                .creationDate(LocalDate.now())
                .inUse(false)
                .lastUsedDate(null)
                .updateDate(LocalDateTime.now())
                .userWhoLastChangeParameters(testUser)
                .build();

        Parameter parameter2 = Parameter.builder()
                .id(2L)
                .parameterName("TestParam2")
                .parameterType(SupParameterType.BOOLEAN)
                .systemName(AppModuleNameDto.TEAMUP_CORE)
                .isList(true)
                .parameterValue(stringList1)
                .creationDate(LocalDate.now())
                .inUse(false)
                .lastUsedDate(null)
                .updateDate(LocalDateTime.now())
                .userWhoLastChangeParameters(testUser)
                .build();

        Parameter parameter3 = Parameter.builder()
                .id(3L)
                .parameterName("TestParam3")
                .parameterType(SupParameterType.INTEGER)
                .systemName(AppModuleNameDto.TEAMUP_CORE)
                .isList(true)
                .parameterValue(stringList1)
                .creationDate(LocalDate.now())
                .inUse(false)
                .lastUsedDate(null)
                .updateDate(LocalDateTime.now())
                .userWhoLastChangeParameters(testUser)
                .build();

        Parameter parameter4 = Parameter.builder()
                .id(4L)
                .parameterName("TestParam4")
                .parameterType(SupParameterType.STRING)
                .systemName(AppModuleNameDto.TEAMUP_CORE)
                .isList(true)
                .parameterValue(stringList1)
                .creationDate(LocalDate.now())
                .inUse(false)
                .lastUsedDate(null)
                .updateDate(LocalDateTime.now())
                .userWhoLastChangeParameters(testUser)
                .build();

        System.out.println("FIRST: " + ParameterToDto.convert(parameter1));
        System.out.println("SECOND: " + ParameterToDto.convert(parameter2));
        System.out.println("THIRD: " + ParameterToDto.convert(parameter3));
        System.out.println("FOURTH: " + ParameterToDto.convert(parameter4));

    }

}
