package ru.team.up.sup.core.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.team.up.dto.AppModuleNameDto;
import ru.team.up.dto.SupParameterDto;
import ru.team.up.sup.core.entity.Parameter;
import ru.team.up.sup.core.exception.NoContentException;
import ru.team.up.sup.core.repositories.ParameterRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class ParameterServiceImpl implements ParameterService {

    private Long daysToSaveParam = 7L;
    private final ParameterRepository parameterRepository;
    private final KafkaSupService kafkaSupService;
    private final DefaultParameterGetter defaultParameterGetter;

    @Autowired
    public ParameterServiceImpl(ParameterRepository parameterRepository,
                                KafkaSupService kafkaSupService,
                                DefaultParameterGetter defaultParameterGetter) {
        this.parameterRepository = parameterRepository;
        this.kafkaSupService = kafkaSupService;
        this.defaultParameterGetter = defaultParameterGetter;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Parameter> getAllParameters() throws NoContentException {
        log.debug("Старт метода List<Parameter> getAllParameters()");
        List<Parameter> parameters = parameterRepository.findAll();
        if (parameters.isEmpty()) {
            throw new NoContentException();
        }
        log.debug("Получили список всех параметров из БД");
        return parameters;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Parameter> getParametersBySystemName(AppModuleNameDto systemName) throws NoContentException {
        log.debug("Старт метода List<Parameter> getParametersBySystemName(String systemName)");
        List<Parameter> parameters = parameterRepository.getParametersBySystemName(systemName);
        if (parameters.isEmpty()) {
            throw new NoContentException();
        }
        log.debug("Получили список всех параметров по systemName {} из БД", systemName);
        return parameters;
    }

    @Override
    @Transactional(readOnly = true)
    public Parameter getParameterById(Long id) throws NoContentException {
        log.debug("Старт метода Parameter getParameterById(Long id) с id = {}", id);
        Parameter parameter = Optional.of(parameterRepository.findById(id)
                .orElseThrow(() -> new NoContentException())).get();
        log.debug("Получили параметр {} из БД", parameter);
        return parameter;
    }

    @Override
    @Transactional(readOnly = true)
    public Parameter getParameterByParameterName(String parameterName) throws NoContentException {
        log.debug("Старт метода Parameter getParameterByParameterName(String parameterName) с параметром {}", parameterName);
        Parameter parameter = parameterRepository.getParameterByParameterName(parameterName);
        if (parameter == null) {
            throw new NoContentException();
        }
        log.debug("Получили из БД параметр {}", parameter);
        return parameter;
    }

    @Override
    public Parameter saveParameter(Parameter parameter) {
        log.debug("Старт метода Parameter saveParameter(Parameter parameter) с параметром {}", parameter);
        parameter.setCreationDate(LocalDate.now());
        parameter.setUpdateDate(LocalDateTime.now());
        Parameter savedParam = parameterRepository.save(parameter);
        kafkaSupService.send(parameter);
        log.debug("Сохранили параметр {} в БД", savedParam);
        return savedParam;
    }

    @Override
    public void deleteParameter(Long id) throws NoContentException {
        log.debug("Старт метода void deleteParameter(Parameter parameter) с id = {}", id);
        log.debug("Проверка существования параметра в БД с id = {}", id);
        Parameter parameter = parameterRepository.findById(id).orElseThrow(() -> new NoContentException());
        parameterRepository.deleteById(id);
        log.debug("Удалили параметр с id = {} из БД", id);
    }

    @Override
    public Parameter editParameter(Parameter parameter) {
        log.debug("Старт метода Parameter editParameter(Parameter parameter) с параметром {}", parameter);
        parameter.setCreationDate(parameterRepository.findById(parameter.getId())
                .orElseThrow(() -> new NoContentException())
                .getCreationDate());
        parameter.setUpdateDate(LocalDateTime.now());
        Parameter editedParam = parameterRepository.save(parameter);
        log.debug("Изменили параметр в БД {}", editedParam);
        kafkaSupService.send(editedParam);
        return editedParam;
    }

    @Override
    public void compareWithDefaultAndUpdate(AppModuleNameDto systemName) {
        List<Parameter> bdParams = parameterRepository.getParametersBySystemName(systemName);
        Map<String, SupParameterDto> defaultParamMap = defaultParameterGetter.getParameters(systemName);
        for (Parameter bdParam : bdParams) {
            if (!defaultParamMap.containsKey(bdParam.getParameterName())) {
                log.debug("Параметр {} не используется в модуле {}", bdParam.getParameterName(), systemName);
                bdParam.setInUse(false);
                parameterRepository.save(bdParam);
            } else {
                bdParam.setInUse(true);
                bdParam.setLastUsedDate(LocalDate.now());
                parameterRepository.save(bdParam);
                defaultParamMap.remove(bdParam.getParameterName());
                log.debug("Параметр {} используется в модуле {}. Дата последнего использования изменена на {}.",
                        bdParam.getParameterName(),
                        systemName,
                        bdParam.getLastUsedDate());
            }
        }
        if (!defaultParamMap.isEmpty()) {
            for (SupParameterDto defaultParam : defaultParamMap.values()) {
                log.debug("Параметр {} используется в модуле {}, но его нет в БД.",
                        defaultParam.getParameterName(),
                        systemName);
                parameterRepository.save(Parameter.builder()
                        .parameterName(defaultParam.getParameterName())
                        .parameterType(defaultParam.getParameterType())
                        .systemName(defaultParam.getSystemName())
                        .parameterValue(defaultParam.getParameterValue().toString())
                        .creationDate(LocalDate.now())
                        .inUse(true)
                        .lastUsedDate(LocalDate.now())
                        .build());
                log.debug("Параметр {} добавлен в БД.", defaultParam.getParameterName());
            }
        }
    }

    @Override
    public void purge(AppModuleNameDto system) {
        List<Parameter> listToCheck = parameterRepository.findByInUseAndSystemName(false, system);
        listToCheck.stream()
                .filter(p -> p.getLastUsedDate() == null ||
                        LocalDate.now().isAfter(p.getLastUsedDate().plusDays(daysToSaveParam)))
                .forEach(p -> {
                    parameterRepository.deleteById(p.getId());
                    log.debug("Удалён параметр {}, т.к. он не использовался более {} дней",
                            p.getParameterName(),
                            daysToSaveParam);
                });
    }
}
