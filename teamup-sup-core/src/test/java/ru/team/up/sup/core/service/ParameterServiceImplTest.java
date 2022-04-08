package ru.team.up.sup.core.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.team.up.dto.AppModuleNameDto;
import ru.team.up.dto.SupParameterDto;
import ru.team.up.dto.SupParameterTypeDto;
import ru.team.up.sup.core.entity.Parameter;
import ru.team.up.sup.core.entity.User;
import ru.team.up.sup.core.repositories.ParameterRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ParameterServiceImplTest {

    private ParameterService underTest;
    @Mock
    private ParameterRepository parameterRepository;
    @Mock
    private DefaultParameterGetter defaultParameterGetter;
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
        underTest = new ParameterServiceImpl(parameterRepository, kafkaSupService, defaultParameterGetter);
    }

    @Test
    void testCompareParameterInBdInUse() {
        // Given
        AppModuleNameDto testedSystem = AppModuleNameDto.TEAMUP_CORE;
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
        SupParameterDto defaultParam = SupParameterDto.builder()
                .parameterName("TestParam3")
                .parameterValue("Hello world!")
                .parameterType(SupParameterTypeDto.STRING)
                .systemName(AppModuleNameDto.TEAMUP_CORE)
                .updateTime(LocalDateTime.now())
                .build();

        Map<String,SupParameterDto> defaultParamMap = new HashMap<>();
        defaultParamMap.put(defaultParam.getParameterName(), defaultParam);
        doReturn(defaultParamMap).when(defaultParameterGetter).getParameters(testedSystem);
        doReturn(List.of(bdParam)).when(parameterRepository)
                .getParametersBySystemName(testedSystem);

        // When
        underTest.compareWithDefaultAndUpdate(testedSystem);
        bdParam.setInUse(true);
        bdParam.setLastUsedDate(LocalDate.now());

        // Then
        ArgumentCaptor<Parameter> parameterArgumentCaptor =
                ArgumentCaptor.forClass(Parameter.class);
        verify(parameterRepository).save(parameterArgumentCaptor.capture());
        assertThat(parameterArgumentCaptor.getValue()).isEqualTo(bdParam);
    }

    @Test
    void testCompareParameterInBdNotInUse() {
        // Given
        AppModuleNameDto testedSystem = AppModuleNameDto.TEAMUP_CORE;
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

        Map<String,SupParameterDto> defaultParamMap = new HashMap<>();
        doReturn(defaultParamMap).when(defaultParameterGetter).getParameters(testedSystem);
        doReturn(List.of(bdParam)).when(parameterRepository)
                .getParametersBySystemName(testedSystem);

        // When
        underTest.compareWithDefaultAndUpdate(testedSystem);
        bdParam.setInUse(false);

        // Then
        ArgumentCaptor<Parameter> parameterArgumentCaptor =
                ArgumentCaptor.forClass(Parameter.class);
        verify(parameterRepository).save(parameterArgumentCaptor.capture());
        assertThat(parameterArgumentCaptor.getValue()).isEqualTo(bdParam);
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

        Map<String,SupParameterDto> defaultParamMap = new HashMap<>();
        defaultParamMap.put(defaultParam.getParameterName(),defaultParam);
        doReturn(defaultParamMap).when(defaultParameterGetter).getParameters(testedSystem);
        doReturn(List.of()).when(parameterRepository)
                .getParametersBySystemName(testedSystem);

        // When
        underTest.compareWithDefaultAndUpdate(testedSystem);

        // Then
        ArgumentCaptor<Parameter> parameterArgumentCaptor =
                ArgumentCaptor.forClass(Parameter.class);
        verify(parameterRepository).save(parameterArgumentCaptor.capture());
        assertThat(parameterArgumentCaptor.getValue()).isEqualTo(result);
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
        doReturn(List.of(testParam1)).when(parameterRepository).findByInUseAndSystemName(false, testParam1.getSystemName());
        // When
        underTest.purge(testParam1.getSystemName());

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
        doReturn(List.of(testParam1)).when(parameterRepository).findByInUseAndSystemName(false, testParam1.getSystemName());
        // When
        underTest.purge(testParam1.getSystemName());

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
        doReturn(List.of(testParam1)).when(parameterRepository).findByInUseAndSystemName(false, testParam1.getSystemName());
        // When
        underTest.purge(testParam1.getSystemName());

        // Then
        verify(parameterRepository, never()).deleteById(testParam1.getId());
    }

}