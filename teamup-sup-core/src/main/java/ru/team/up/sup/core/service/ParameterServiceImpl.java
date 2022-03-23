package ru.team.up.sup.core.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.team.up.dto.AppModuleNameDto;
import ru.team.up.sup.core.entity.Parameter;
import ru.team.up.sup.core.exception.NoContentException;
import ru.team.up.sup.core.repositories.ParameterRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ParameterServiceImpl implements ParameterService {

    private ParameterRepository parameterRepository;
    private KafkaSupServiceImpl kafkaSupService;

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
        log.debug("Получили список всех параметров по systemName из БД {}", systemName);
        return parameters;
    }

    @Override
    @Transactional(readOnly = true)
    public Parameter getParameterById(Long id) throws NoContentException {
        log.debug("Старт метода Parameter getParameterById(Long id) с параметром {}", id);
        Parameter parameter = Optional.of(parameterRepository.findById(id)
                .orElseThrow(() -> new NoContentException())).get();
        log.debug("Получили параметр из БД {}", parameter.getId());
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
        log.debug("Получили из БД параметр c айди {}", parameter.getId());
        return parameter;
    }

    @Override
    @Transactional
    public Parameter saveParameter(Parameter parameter) {
        log.debug("Старт метода Parameter saveParameter(Parameter parameter) с параметром {}", parameter);
        // надо проверить нужно ли здесь менять время или оно уже устанавливается само
//        parameter.setCreationDate(LocalDate.now());
//        parameter.setUpdateDate(LocalDateTime.now());
        Parameter saved = parameterRepository.save(parameter);
        kafkaSupService.send(parameter);
        log.debug("Сохранили параметр в БД {}", saved);
        return saved;
    }

    @Override
    @Transactional
    public void deleteParameter(Long id) throws NoContentException {
        log.debug("Старт метода void deleteParameter(Parameter parameter) с {}", id);
        log.debug("Проверка существования параметра в БД с id {}", id);
        Parameter parameter = parameterRepository.findById(id).orElseThrow(() -> new NoContentException());
        parameterRepository.deleteById(id);
        kafkaSupService.delete(parameter);
        log.debug("Удалили параметр из БД {}", id);
    }

    @Override
    @Transactional
    public Parameter editParameter(Parameter parameter) {
        log.debug("Старт метода Parameter editParameter(Parameter parameter) с параметром {}", parameter);
        parameter.setCreationDate(parameterRepository.findById(parameter.getId())
                .orElseThrow(() -> new NoContentException())
                .getCreationDate());
        parameter.setUpdateDate(LocalDateTime.now());
        Parameter saved = parameterRepository.save(parameter);
        log.debug("Изменили параметр в БД {}", saved);
        kafkaSupService.send(saved);
        return saved;
    }
}
