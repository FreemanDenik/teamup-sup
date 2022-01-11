package ru.team.up.sup.input.controller.publicController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.team.up.sup.core.entity.Parameter;
import ru.team.up.sup.core.exception.NoContentException;
import ru.team.up.sup.core.service.ParameterService;

import java.util.List;

@Slf4j
@Tag(name = "Public Parameter Controller", description = "public parameter API")
@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/public/api")
public class ParameterRestControllerPublic {
    private ParameterService parameterService;

    /**
     *
     */
    @GetMapping
    @Operation(summary ="Получение списка всех параметров")
    public ResponseEntity<List<Parameter>> getAllParameters() {
        log.debug("пытаемся получить список всех параметров");

        ResponseEntity<List<Parameter>> responseEntity;
        try {
            responseEntity = ResponseEntity.ok(parameterService.getAllParameters());
        } catch (NoContentException e) {
            responseEntity = ResponseEntity.noContent().build();
        }
        log.debug("Получили ответ {}", responseEntity);

        return responseEntity;
    }

    /**
     *
     */
    @GetMapping("/{systemName}")
    @Operation(summary ="Получение списка параметров по имени системы")
    public ResponseEntity<List<Parameter>> getParametersBySystemName(@PathVariable("systemName") String systemName) {
        log.debug("пытаемся получить список параметров по имени системы {}", systemName);

        ResponseEntity<List<Parameter>> responseEntity;
        try {
            responseEntity = ResponseEntity.ok(parameterService.getParametersBySystemName(systemName));
        } catch (NoContentException e) {
            responseEntity = ResponseEntity.noContent().build();
        }
        log.debug("Получили ответ {}", responseEntity);

        return responseEntity;
    }

    /**
     *
     */
    @GetMapping("/byid/{id}")
    @Operation(summary ="Получение параметра по id")
    public ResponseEntity<Parameter> getParameterById(@PathVariable Long id) {
        log.debug("пытаемся получить параметр по айди {}", id);

        ResponseEntity<Parameter> responseEntity;
        try {
            responseEntity = ResponseEntity.ok(parameterService.getParameterById(id));
        } catch (NoContentException e) {
            responseEntity = ResponseEntity.notFound().build();
        }
        log.debug("Получили ответ {}", responseEntity);

        return responseEntity;
    }

    /**
     *
     */
    @GetMapping("/byname/{parameterName}")
    @Operation(summary ="Получение параметра по имени")
    public ResponseEntity<Parameter> getParameterByParameterName(@PathVariable("parameterName") String parameterName) {
        log.debug("пытаемся получить параметр по имени {}", parameterName);

        ResponseEntity<Parameter> responseEntity;
        try {
            responseEntity = ResponseEntity.ok(parameterService.getParameterByParameterName(parameterName));
        } catch (NoContentException e) {
            responseEntity = ResponseEntity.noContent().build();
        }
        log.debug("Получили ответ {}", responseEntity);

        return responseEntity;
    }
}
