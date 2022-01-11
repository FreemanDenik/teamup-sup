package ru.team.up.sup.input.controller.privateController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.team.up.sup.core.entity.Parameter;
import ru.team.up.sup.core.service.ParameterService;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * @link localhost:8080/swagger-ui.html
 * Документация API
 */

@Slf4j
@Tag(name = "Admin Parameter Private Controller",description = "Admin Parameter API")
@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/private/account/admin/parameters")
public class AdminParameterController {
    private ParameterService parameterService;

    /**
     * @return Результат работы метода parameterService.getAllParameters() в виде коллекции параметров
     * в теле ResponseEntity
     */
    @Operation(summary ="Получение списка всех параметров")
    @GetMapping
    public ResponseEntity<List<Parameter>> getAllParameters() {
        log.debug("Старт метода ResponseEntity<List<Parameter>> getAllParameters()");

        ResponseEntity<List<Parameter>> responseEntity = ResponseEntity.ok(parameterService.getAllParameters());
        log.debug("Получили ответ {}", responseEntity);

        return responseEntity;
    }

    /**
     * @param id Значение ID параметра
     * @return Результат работы метода parameterService.getOneParameter(id) в виде объекта Parameter
     * в теле ResponseEntity
     */
    @Operation(summary ="Получение параметра по id")
    @GetMapping("/{id}")
    public ResponseEntity<Parameter> getOneParameter(@PathVariable Long id) {
        log.debug("Старт метода ResponseEntity<Parameter> getOneParameter(@PathVariable Long id) с параметром {}", id);

        ResponseEntity<Parameter> responseEntity = ResponseEntity.ok(parameterService.getParameterById(id));
        log.debug("Получили ответ {}", responseEntity);

        return responseEntity;
    }

    /**
     * @param parameter Создаваемый объект класса Parameter
     * @return Результат работы метода parameterService.saveParameter(parameter) в виде объекта Parameter
     * в теле ResponseEntity
     */
    @Operation(summary ="Создание нового параметра")
    @PostMapping
    public ResponseEntity<Parameter> createParameter(@RequestParam String parameter, @RequestBody @NotNull Parameter parameterCreate) {
        log.debug("Старт метода ResponseEntity<Parameter> createParameter(@RequestBody @NotNull Parameter parameter) с параметром {}", parameterCreate);

        ResponseEntity<Parameter> responseEntity = new ResponseEntity<>(parameterService.saveParameter(parameterCreate), HttpStatus.CREATED);
        log.debug("Получили ответ {}", responseEntity);

        return responseEntity;
    }

    /**
     * @param parameter Обновляемый объект класса Parameter
     * @return Результат работы метода parameterService.saveParameter(parameter) в виде объекта Parameter
     * в теле ResponseEntity
     */
    @Operation(summary ="Обновление данных параметра")
    @PatchMapping
    public ResponseEntity<Parameter> updateParameter(@RequestBody @NotNull Parameter parameter) {
        log.debug("Старт метода ResponseEntity<Parameter> updateParameter(@RequestBody @NotNull Parameter parameter) с параметром {}", parameter);

        log.debug("Проверка наличия обновляемого параметра в БД");
        parameterService.getParameterById(parameter.getId());

        ResponseEntity<Parameter> responseEntity = ResponseEntity.ok(parameterService.saveParameter(parameter));
        log.debug("Получили ответ {}", responseEntity);

        return responseEntity;
    }

    /**
     * @param id Удаляемый объект класса Parameter
     * @return Объект ResponseEntity со статусом OK
     */
    @Operation(summary ="Удаление параметра по id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Parameter> deleteParameter(@PathVariable Long id) {
        log.debug("Старт метода ResponseEntity<Parameter> updateParameter(@RequestBody @NotNull Parameter parameter) с параметром {}", id);

        parameterService.deleteParameter(id);

        ResponseEntity<Parameter> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        log.debug("Получили ответ {}", responseEntity);

        return responseEntity;
    }
}
