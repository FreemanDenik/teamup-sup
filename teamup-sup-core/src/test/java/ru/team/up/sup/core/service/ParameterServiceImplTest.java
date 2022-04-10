package ru.team.up.sup.core.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.team.up.dto.AppModuleNameDto;
import ru.team.up.dto.ListSupParameterDto;
import ru.team.up.dto.SupParameterDto;
import ru.team.up.dto.SupParameterTypeDto;
import ru.team.up.sup.core.entity.Parameter;
import ru.team.up.sup.core.entity.User;
import ru.team.up.sup.core.repositories.ParameterRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ParameterServiceImplTest {

    private ParameterService underTest;
    @Mock
    private ParameterRepository parameterRepository;
    @Mock
    private KafkaSupService kafkaSupService;


    private final User testUser = User.builder()
            .id(1L)
            .name("TestName")
            .lastName("TestLastName")
            .email("test@mail.com")
            .password("testPass")
            .lastAccountActivity(LocalDateTime.now())
            .build();

    @BeforeEach
    void setUp() {
        underTest = new ParameterServiceImpl(parameterRepository, kafkaSupService);
    }

    @Test
    void testCompareParameterInBdInUse() {
        // Given
        Parameter bdParam = Parameter.builder()
                .id(1L)
                .parameterName("TestParam3")
                .parameterType(SupParameterTypeDto.STRING)
                .systemName(AppModuleNameDto.TEAMUP_CORE)
                .parameterValue("Hello world!")
                .creationDate(LocalDate.now())
                .inUse(false)
                .lastUsedDate(null)
                .updateDate(LocalDateTime.now())
                .userWhoLastChangeParameters(testUser)
                .build();
        Parameter bdParamResult = Parameter.builder()
                .id(1L)
                .parameterName("TestParam3")
                .parameterType(SupParameterTypeDto.STRING)
                .systemName(AppModuleNameDto.TEAMUP_CORE)
                .parameterValue("Hello world!")
                .creationDate(LocalDate.now())
                .inUse(true)
                .lastUsedDate(LocalDate.now())
                .updateDate(LocalDateTime.now())
                .userWhoLastChangeParameters(testUser)
                .build();
        SupParameterDto defaultParam = SupParameterDto.builder()
                .parameterName("TestParam3")
                .parameterValue("Hello world!")
                .parameterType(SupParameterTypeDto.STRING)
                .systemName(AppModuleNameDto.TEAMUP_CORE)
                .updateTime(LocalDateTime.now())
                .build();
        ListSupParameterDto listSupParameterDto = new ListSupParameterDto();
        listSupParameterDto.addParameter(defaultParam);
        doReturn(List.of(bdParam)).when(parameterRepository)
                .getParametersBySystemName(listSupParameterDto.getModuleName());

        // When
        underTest.compareWithDefaultAndUpdate(listSupParameterDto);


        // Then
        ArgumentCaptor<Parameter> parameterArgumentCaptor =
                ArgumentCaptor.forClass(Parameter.class);
        verify(parameterRepository).save(parameterArgumentCaptor.capture());
        assertThat(parameterArgumentCaptor.getValue()).isEqualTo(bdParamResult);
    }

    @Test
    void testCompareParameterInBdNotInUse() {
        // Given
        Parameter bdParam = Parameter.builder()
                .id(1L)
                .parameterName("TestParam1")
                .parameterType(SupParameterTypeDto.STRING)
                .systemName(AppModuleNameDto.TEAMUP_CORE)
                .parameterValue("Hello world!")
                .creationDate(LocalDate.now())
                .inUse(true)
                .lastUsedDate(null)
                .updateDate(LocalDateTime.now())
                .userWhoLastChangeParameters(testUser)
                .build();
        Parameter bdParamResult = Parameter.builder()
                .id(1L)
                .parameterName("TestParam1")
                .parameterType(SupParameterTypeDto.STRING)
                .systemName(AppModuleNameDto.TEAMUP_CORE)
                .parameterValue("Hello world!")
                .creationDate(LocalDate.now())
                .inUse(false)
                .lastUsedDate(null)
                .updateDate(bdParam.getUpdateDate())
                .userWhoLastChangeParameters(testUser)
                .build();
        SupParameterDto defaultParam = SupParameterDto.builder()
                .parameterName("TestParam3")
                .parameterValue("Hello world!")
                .parameterType(SupParameterTypeDto.STRING)
                .systemName(AppModuleNameDto.TEAMUP_CORE)
                .updateTime(LocalDateTime.now())
                .build();
        Parameter newParam = Parameter.builder()
                .parameterName(defaultParam.getParameterName())
                .parameterType(defaultParam.getParameterType())
                .systemName(defaultParam.getSystemName())
                .parameterValue(defaultParam.getParameterValue().toString())
                .creationDate(LocalDate.now())
                .inUse(true)
                .lastUsedDate(LocalDate.now())
                .build();

        ListSupParameterDto listSupParameterDto = new ListSupParameterDto();
        listSupParameterDto.addParameter(defaultParam);
        doReturn(List.of(bdParam)).when(parameterRepository)
                .getParametersBySystemName(listSupParameterDto.getModuleName());

        // When
        underTest.compareWithDefaultAndUpdate(listSupParameterDto);

        // Then
        ArgumentCaptor<Parameter> parameterArgumentCaptor = ArgumentCaptor.forClass(Parameter.class);
        verify(parameterRepository, times(2)).save(parameterArgumentCaptor.capture());
        List<Parameter> results = parameterArgumentCaptor.getAllValues();
        assertThat(results.get(0)).isEqualTo(bdParamResult);
        assertThat(results.get(1)).isEqualTo(newParam);
    }

    @Test
    void testCompareParameterNotInBd() {
        // Given
        AppModuleNameDto testedSystem = AppModuleNameDto.TEAMUP_CORE;
        SupParameterDto defaultParam = SupParameterDto.builder()
                .parameterName("TestParam25")
                .parameterValue(123)
                .parameterType(SupParameterTypeDto.INTEGER)
                .systemName(AppModuleNameDto.TEAMUP_CORE)
                .updateTime(LocalDateTime.now())
                .build();
        Parameter result = Parameter.builder()
                .parameterName(defaultParam.getParameterName())
                .parameterType(defaultParam.getParameterType())
                .systemName(defaultParam.getSystemName())
                .parameterValue(defaultParam.getParameterValue().toString())
                .creationDate(LocalDate.now())
                .inUse(true)
                .lastUsedDate(LocalDate.now())
                .build();

        ListSupParameterDto listSupParameterDto = new ListSupParameterDto();
        listSupParameterDto.addParameter(defaultParam);
        doReturn(List.of()).when(parameterRepository)
                .getParametersBySystemName(testedSystem);

        // When
        underTest.compareWithDefaultAndUpdate(listSupParameterDto);

        // Then
        ArgumentCaptor<Parameter> parameterArgumentCaptor =
                ArgumentCaptor.forClass(Parameter.class);
        verify(parameterRepository).save(parameterArgumentCaptor.capture());
        assertThat(parameterArgumentCaptor.getValue()).isEqualTo(result);
    }

    @Test
    void compareWillThrowIfDtoIsNull() {
        assertThatThrownBy(() -> underTest.compareWithDefaultAndUpdate(null))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Получен пустой лист параметров по умолчанию");

    }

    @Test
    void compareWillThrowIfModuleIsNull() {
        // Given
        final ListSupParameterDto list = new ListSupParameterDto();
        list.setModuleName(null);
        list.setList(List.of(SupParameterDto.builder().build()));

        // When Then
        assertThatThrownBy(() -> underTest.compareWithDefaultAndUpdate(list))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Получен пустой лист параметров по умолчанию");

    }

    @Test
    void compareWillThrowIfListIsNull() {
        // Given
        final ListSupParameterDto list = new ListSupParameterDto();
        list.setModuleName(AppModuleNameDto.TEAMUP_CORE);
        list.setList(null);

        // When Then
        assertThatThrownBy(() -> underTest.compareWithDefaultAndUpdate(list))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Получен пустой лист параметров по умолчанию");

    }

    @Test
    void compareWillThrowIfListIsEmpty() {
        // Given
        final ListSupParameterDto list = new ListSupParameterDto();
        list.setModuleName(AppModuleNameDto.TEAMUP_CORE);
        list.setList(List.of());

        // When Then
        assertThatThrownBy(() -> underTest.compareWithDefaultAndUpdate(list))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Получен пустой лист параметров по умолчанию");

    }

    @Test
    void canPurgeIfUsedDateIsNull() {
        // Given
        Parameter testParam1 = Parameter.builder()
                .id(1L)
                .parameterName("TestParam2")
                .parameterType(SupParameterTypeDto.STRING)
                .systemName(AppModuleNameDto.TEAMUP_CORE)
                .parameterValue("Hello world!")
                .creationDate(LocalDate.now())
                .inUse(false)
                .lastUsedDate(null)
                .updateDate(LocalDateTime.now())
                .build();
        doReturn(List.of(testParam1)).when(parameterRepository).findAll();
        // When
        underTest.purge();

        // Then
        ArgumentCaptor<Long> idArgumentCaptor =
                ArgumentCaptor.forClass(Long.class);
        verify(parameterRepository).deleteById(idArgumentCaptor.capture());
        assertThat(idArgumentCaptor.getValue()).isEqualTo(testParam1.getId());
    }

    @Test
    void canPurgeIfUsedDateIsGreaterThanThreshold() {
        // Given
        Parameter testParam1 = Parameter.builder()
                .id(1L)
                .parameterName("TestParam2")
                .parameterType(SupParameterTypeDto.STRING)
                .systemName(AppModuleNameDto.TEAMUP_CORE)
                .parameterValue("Hello world!")
                .creationDate(LocalDate.now())
                .inUse(false)
                .lastUsedDate(LocalDate.now().minusDays(8L))
                .updateDate(LocalDateTime.now())
                .build();
        doReturn(List.of(testParam1)).when(parameterRepository).findAll();
        // When
        underTest.purge();

        // Then
        ArgumentCaptor<Long> idArgumentCaptor =
                ArgumentCaptor.forClass(Long.class);
        verify(parameterRepository).deleteById(idArgumentCaptor.capture());
        assertThat(idArgumentCaptor.getValue()).isEqualTo(testParam1.getId());
    }

    @Test
    void dontPurgeIfUsedDateIsLowerThanThreshold() {
        // Given
        Parameter testParam1 = Parameter.builder()
                .id(1L)
                .parameterName("TestParam2")
                .parameterType(SupParameterTypeDto.STRING)
                .systemName(AppModuleNameDto.TEAMUP_CORE)
                .parameterValue("Hello world!")
                .creationDate(LocalDate.now())
                .inUse(false)
                .lastUsedDate(LocalDate.now().minusDays(1L))
                .updateDate(LocalDateTime.now())
                .build();
        doReturn(List.of(testParam1)).when(parameterRepository).findAll();
        // When
        underTest.purge();

        // Then
        verify(parameterRepository, never()).deleteById(testParam1.getId());
    }


    @Test
    void dontPurgeIfInUse() {
        // Given
        Parameter testParam1 = Parameter.builder()
                .id(1L)
                .parameterName("TestParam2")
                .parameterType(SupParameterTypeDto.STRING)
                .systemName(AppModuleNameDto.TEAMUP_CORE)
                .parameterValue("Hello world!")
                .creationDate(LocalDate.now())
                .inUse(true)
                .lastUsedDate(LocalDate.now().minusDays(100L))
                .updateDate(LocalDateTime.now())
                .build();
        doReturn(List.of(testParam1)).when(parameterRepository).findAll();
        // When
        underTest.purge();

        // Then
        verify(parameterRepository, never()).deleteById(testParam1.getId());
    }


}