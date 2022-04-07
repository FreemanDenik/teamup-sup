package ru.team.up.sup.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ParameterServiceImpl implements ParameterService {

    private ParameterRepository parameterRepository;
    private KafkaSupService kafkaSupService;

//    @PostConstruct
//    private void init(){
//        compareAndUpdateWithFile();
//    }

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
        kafkaSupService.delete(parameter);
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
    public List<SupParameterDto<?>> readParametersFromFile() {
        ObjectMapper mapper = new ObjectMapper();
        List<SupParameterDto<?>> list = List.of();
        try {
            list = Arrays.asList(mapper.readValue(new File("./Parameters.json"),
                    SupParameterDto[].class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void compareAndUpdateWithFile() {
        List<SupParameterDto<?>> list = readParametersFromFile();
        for (SupParameterDto fileParam : list) {
            Parameter bdParam = parameterRepository.getParameterByParameterName(fileParam.getParameterName());

            if (bdParam == null) {
                parameterRepository.save(Parameter.builder()
                        .parameterName(fileParam.getParameterName())
                        .parameterType(fileParam.getParameterType())
                        .systemName(fileParam.getSystemName())
                        .parameterValue(fileParam.getParameterValue().toString())
                        .creationDate(LocalDate.now())
                        .updateDate(LocalDateTime.now())
                        .build());
            }
        }
    }

}
