package ru.team.up.sup.core.utils;

import org.junit.jupiter.api.Test;
import ru.team.up.dto.AppModuleNameDto;
import ru.team.up.dto.ListSupParameterDto;
import ru.team.up.dto.SupParameterDto;
import ru.team.up.dto.SupParameterType;
import ru.team.up.sup.core.entity.Parameter;
import ru.team.up.sup.core.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ParameterToDtoTest {

    private final User testUser = User.builder()
            .id(1L)
            .name("TestName")
            .lastName("TestLastName")
            .email("test@mail.com")
            .password("testPass")
            .lastAccountActivity(LocalDateTime.now())
            .build();
    private final Parameter testParam1 = Parameter.builder()
            .id(1L)
            .parameterName("TestParam1")
            .parameterType(SupParameterType.BOOLEAN)
            .systemName(AppModuleNameDto.TEAMUP_CORE)
            .isList(false)
            .parameterValue(Collections.singletonList("false"))
            .creationDate(LocalDate.now())
            .updateDate(LocalDateTime.now())
            .userWhoLastChangeParameters(testUser)
            .build();
    private final Parameter testParam2 = Parameter.builder()
            .id(2L)
            .parameterName("TestParam2")
            .parameterType(SupParameterType.INTEGER)
            .systemName(AppModuleNameDto.TEAMUP_KAFKA)
            .isList(false)
            .parameterValue(Collections.singletonList("123"))
            .creationDate(LocalDate.now())
            .updateDate(LocalDateTime.now())
            .userWhoLastChangeParameters(testUser)
            .build();
    private final Parameter testParam3 = Parameter.builder()
            .id(3L)
            .parameterName("TestParam3")
            .parameterType(SupParameterType.INTEGER)
            .systemName(AppModuleNameDto.TEAMUP_APP)
            .isList(false)
            .parameterValue(Collections.singletonList("123"))
            .creationDate(LocalDate.now())
            .updateDate(LocalDateTime.now())
            .userWhoLastChangeParameters(testUser)
            .build();


    @Test
    void convertTest() {

        List<String> stringList1 = new ArrayList<>(Arrays.asList("1", "2", "3"));
        List<String> stringList2 = new ArrayList<>(Arrays.asList("true", "true", "false"));
        List<String> stringList3 = new ArrayList<>(Arrays.asList("1", "2", "3"));
        List<String> stringList4 = new ArrayList<>(Arrays.asList("a", "b", "c"));
        List<String> stringList5 = new ArrayList<>(List.of("2"));
        List<String> stringList6 = new ArrayList<>(List.of("true"));
        List<String> stringList7 = new ArrayList<>(List.of("1"));
        List<String> stringList8 = new ArrayList<>(List.of("a"));


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
                .isList(true)
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
                .parameterValue(stringList2)
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
                .parameterValue(stringList3)
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
                .parameterValue(stringList4)
                .creationDate(LocalDate.now())
                .inUse(false)
                .lastUsedDate(null)
                .updateDate(LocalDateTime.now())
                .userWhoLastChangeParameters(testUser)
                .build();

        Parameter parameter5 = Parameter.builder()
                .id(5L)
                .parameterName("TestParam5")
                .parameterType(SupParameterType.DOUBLE)
                .systemName(AppModuleNameDto.TEAMUP_CORE)
                .isList(false)
                .parameterValue(stringList5)
                .creationDate(LocalDate.now())
                .inUse(false)
                .lastUsedDate(null)
                .updateDate(LocalDateTime.now())
                .userWhoLastChangeParameters(testUser)
                .build();

        Parameter parameter6 = Parameter.builder()
                .id(6L)
                .parameterName("TestParam6")
                .parameterType(SupParameterType.BOOLEAN)
                .systemName(AppModuleNameDto.TEAMUP_CORE)
                .isList(false)
                .parameterValue(stringList6)
                .creationDate(LocalDate.now())
                .inUse(false)
                .lastUsedDate(null)
                .updateDate(LocalDateTime.now())
                .userWhoLastChangeParameters(testUser)
                .build();

        Parameter parameter7 = Parameter.builder()
                .id(8L)
                .parameterName("TestParam7")
                .parameterType(SupParameterType.INTEGER)
                .systemName(AppModuleNameDto.TEAMUP_CORE)
                .isList(false)
                .parameterValue(stringList7)
                .creationDate(LocalDate.now())
                .inUse(false)
                .lastUsedDate(null)
                .updateDate(LocalDateTime.now())
                .userWhoLastChangeParameters(testUser)
                .build();

        Parameter parameter8 = Parameter.builder()
                .id(8L)
                .parameterName("TestParam8")
                .parameterType(SupParameterType.STRING)
                .systemName(AppModuleNameDto.TEAMUP_CORE)
                .isList(false)
                .parameterValue(stringList8)
                .creationDate(LocalDate.now())
                .inUse(false)
                .lastUsedDate(null)
                .updateDate(LocalDateTime.now())
                .userWhoLastChangeParameters(testUser)
                .build();

        //Double List
        SupParameterDto<?> testParam1 = ParameterToDto.convert(parameter1);
        //Boolean List
        SupParameterDto<?> testParam2 = ParameterToDto.convert(parameter2);
        //Integer List
        SupParameterDto<?> testParam3 = ParameterToDto.convert(parameter3);
        //String List
        SupParameterDto<?> testParam4 = ParameterToDto.convert(parameter4);
        //Double
        SupParameterDto<?> testParam5 = ParameterToDto.convert(parameter5);
        //Boolean
        SupParameterDto<?> testParam6 = ParameterToDto.convert(parameter6);
        //Integer
        SupParameterDto<?> testParam7 = ParameterToDto.convert(parameter7);
        //String
        SupParameterDto<?> testParam8 = ParameterToDto.convert(parameter8);



        assertThat(testParam1.getParameterValue()).isEqualTo(new ArrayList<>(Arrays.asList(1.0, 2.0, 3.0)));
        assertThat(testParam2.getParameterValue()).isEqualTo(new ArrayList<>(Arrays.asList(true, true, false)));
        assertThat(testParam3.getParameterValue()).isEqualTo(new ArrayList<>(Arrays.asList(1, 2, 3)));
        assertThat(testParam4.getParameterValue()).isEqualTo(new ArrayList<>(Arrays.asList("a", "b", "c")));
        assertThat(testParam5.getParameterValue()).isEqualTo(2.0);
        assertThat(testParam6.getParameterValue()).isEqualTo(true);
        assertThat(testParam7.getParameterValue()).isEqualTo(1);
        assertThat(testParam8.getParameterValue()).isEqualTo("a");



    }

    @Test
    void canParseList() {
        // Given
        List<Parameter> inputList = List.of(testParam1, testParam2, testParam3);
        Set<AppModuleNameDto> moduleNames = new HashSet<>();
        inputList.stream().forEach(p -> moduleNames.add(p.getSystemName()));
        Set<String> parameterNames = new HashSet<>();
        inputList.stream().forEach(p -> parameterNames.add(p.getParameterName()));
        // When
        List<ListSupParameterDto> resultList = ParameterToDto.parseParameterListToListsDto(inputList);
        // Then
        assertThat(resultList.size()).isEqualTo(moduleNames.size());
        for (ListSupParameterDto list : resultList) {
            list.getList().stream().forEach(paramDto -> parameterNames.remove(paramDto.getParameterName()));
        }
        assertThat(parameterNames.isEmpty());
    }

    @Test
    void willThrownWhenInputListIsNullOrEmpty() {
        assertThatThrownBy(() -> ParameterToDto.parseParameterListToListsDto(null))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("На вход метода parseParameterListToListsDto пришел null или пустой лист");
        assertThatThrownBy(() -> ParameterToDto.parseParameterListToListsDto(List.of()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("На вход метода parseParameterListToListsDto пришел null или пустой лист");
    }

    @Test
    void willThrownWhenResultListIsEmpty() {
        assertThatThrownBy(() -> ParameterToDto.parseParameterListToListsDto(List.of(Parameter.builder().build())))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Ни один параметр не добавлен в результирующий лист");
    }
}