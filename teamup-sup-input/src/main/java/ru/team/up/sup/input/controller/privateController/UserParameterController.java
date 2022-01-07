package ru.team.up.sup.input.controller.privateController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.team.up.sup.core.entity.Parameter;
import ru.team.up.sup.core.entity.User;
import ru.team.up.sup.core.service.ParameterService;
import ru.team.up.sup.core.service.UserService;

import javax.persistence.PersistenceException;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * @link localhost:8080/swagger-ui.html
 * Документация API
 */

@Slf4j
@RestController
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Tag(name = "User Parameter Private Controller", description = "User Parameter API")
@RequestMapping(value = "/private/account/user/parameters")
public class UserParameterController {
    private ParameterService parameterService;

    /**
     * @return Результат работы метода parameterService.getAllParameters() в виде коллекции параметров
     * в теле ResponseEntity
     */
    @GetMapping
    @Operation(summary ="Получение списка всех параметров")
    public ResponseEntity<List<Parameter>> getAllUsers() {
        log.debug("Старт метода ResponseEntity<List<Parameter>> getAllParameters()");

        ResponseEntity<List<Parameter>> responseEntity;
        try {
            responseEntity = ResponseEntity.ok(parameterService.getAllParameters());
        } catch (PersistenceException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        log.debug("Получили ответ {}", responseEntity);

        return responseEntity;
    }

    /**
     * @param id Значение ID параметра
     * @return Результат работы метода parameterService.getOneParameter(id) в виде объекта Parameter
     * в теле ResponseEntity
     */
    @GetMapping("/{id}")
    @Operation(summary ="Получение параметра по id")
    public ResponseEntity<Parameter> getOneParameter(@PathVariable Long id) {
        log.debug("Старт метода ResponseEntity<Parameter> getOneParameter(@PathVariable Long id) с параметром {}", id);

        ResponseEntity<Parameter> responseEntity;
        try {
            responseEntity = ResponseEntity.ok(parameterService.getOneParameter(id));
        } catch (PersistenceException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        log.debug("Получили ответ {}", responseEntity);

        return responseEntity;
    }
}
