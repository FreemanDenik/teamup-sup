package ru.team.up.sup.core.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.team.up.sup.core.entity.Parameter;
import ru.team.up.sup.core.exception.NoContentException;
import ru.team.up.sup.core.exception.UserNotFoundException;
import ru.team.up.sup.core.repositories.ParameterRepository;

import java.util.List;
import java.util.Optional;

/**
 *
 * Класс сервиса для управления параметрами ru.team.up.core.entity.Parameter
 */
@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ParameterServiceImpl implements ParameterService {
    private ParameterRepository parameterRepository;

    /**
     * @return Возвращает коллекцию Parameter.
     * Если коллекция пуста, генерирует исключение со статусом HttpStatus.NO_CONTENT
     */
    @Override
    @Transactional(readOnly = true)
    public List<Parameter> getAllParameters() throws NoContentException {
        log.debug("Старт метода List<Parameter> getAllParameters()");

        List<Parameter> parameters = parameterRepository.findAll();

        if (parameters.isEmpty()){
            throw new NoContentException();
        }
        log.debug("Получили список всех админов из БД {}", parameters);

        return parameters;
    }

    /**
     * @param id Уникальный ключ ID параметра
     * @return Находит в БД параметр по ID и возвращает его.
     * Если параметр с переданным ID не найден в базе, генерирует исключение со статусом HttpStatus.NOT_FOUND
     */
    @Override
    @Transactional(readOnly = true)
    public Parameter getOneParameter(Long id) throws UserNotFoundException {
        log.debug("Старт метода Parameter getOneParameter(Long id) с параметром {}", id);

        Parameter parameter = Optional.of(parameterRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id))).get();
        log.debug("Получили параметр из БД {}", parameter);

        return parameter;
    }

    /**
     * @param parameter Объект класса ru.team.up.core.entity.Parameter
     * @return Возвращает сохраненный в БД объект parameter
     */
    @Override
    @Transactional
    public Parameter saveParameter(Parameter parameter) {
        log.debug("Старт метода Parameter saveParameter(Parameter parameter) с параметром {}", parameter);

        Parameter save = parameterRepository.save(parameter);
        log.debug("Сохранили параметр в БД {}", save);

        return save;
    }

    /**
     * @param id Объект класса ru.team.up.core.entity.Parameter
     *           Метод удаляет параметр из БД
     */
    @Override
    @Transactional
    public void deleteParameter(Long id) throws UserNotFoundException {
        log.debug("Старт метода void deleteParameter(Parameter parameter) с параметром {}", id);

        log.debug("Проверка существования параметра в БД с id {}", id);
        Optional.of(parameterRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id)));

        parameterRepository.deleteById(id);
        log.debug("Удалили параметр из БД {}", id);
    }
}
