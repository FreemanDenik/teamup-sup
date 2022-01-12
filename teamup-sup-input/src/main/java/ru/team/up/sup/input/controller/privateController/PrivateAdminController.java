package ru.team.up.sup.input.controller.privateController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.team.up.sup.core.entity.User;
import ru.team.up.sup.core.service.UserService;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Alexey Tkachenko
 *
 * @link localhost:8080/swagger-ui.html
 * Документация API
 */

@Slf4j
@Tag(name = "Admin Private Controller",description = "Admin API")
@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/private/account/admin")
public class PrivateAdminController {
    private UserService userService;
    /**
     * @return Результат работы метода adminService.getAllAdmins() в виде коллекции админов
     * в теле ResponseEntity
     */
    @Operation(summary ="Получение списка всех администраторов")
    @GetMapping
    public ResponseEntity<List<User>> getAllAdmins() {
        log.debug("Старт метода ResponseEntity<List<Admin>> getAllAdmins()");

        ResponseEntity<List<User>> responseEntity = ResponseEntity.ok(userService.getAllUsers());
        log.debug("Получили ответ {}", responseEntity);

        return responseEntity;
    }

    /**
     * @param id Значение ID админа
     * @return Результат работы метода adminService.getOneAdmin(id) в виде объекта Admin
     * в теле ResponseEntity
     */
    @Operation(summary ="Получение администратора по id")
    @GetMapping("/{id}")
    public ResponseEntity<User> getOneAdmin(@PathVariable Long id) {
        log.debug("Старт метода ResponseEntity<Admin> getOneAdmin(@PathVariable Long id) с параметром {}", id);

        ResponseEntity<User> responseEntity = ResponseEntity.ok(userService.getOneUser(id));
        log.debug("Получили ответ {}", responseEntity);

        return responseEntity;
    }

    /**
     * @param admin Создаваемый объект класса Admin
     * @return Результат работы метода adminService.saveAdmin(admin) в виде объекта Admin
     * в теле ResponseEntity
     */
    @Operation(summary ="Создание нового администратора")
    @PostMapping
    public ResponseEntity<User> createAdmin(@RequestParam String admin, @RequestBody @NotNull User user) {
        log.debug("Старт метода ResponseEntity<Admin> createAdmin(@RequestBody @NotNull Admin admin) с параметром {}", user);

        ResponseEntity<User> responseEntity = new ResponseEntity<>(userService.saveUser(user), HttpStatus.CREATED);
        log.debug("Получили ответ {}", responseEntity);

        return responseEntity;
    }

    /**
     * @param user Обновляемый объект класса Admin
     * @return Результат работы метода adminService.saveAdmin(admin) в виде объекта Admin
     * в теле ResponseEntity
     */
    @Operation(summary ="Обновление данных администратора")
    @PatchMapping
    public ResponseEntity<User> updateAdmin(@RequestBody @NotNull User user) {
        log.debug("Старт метода ResponseEntity<Admin> updateAdmin(@RequestBody @NotNull Admin admin) с параметром {}", user);

        log.debug("Проверка наличия обновляемого администратора в БД");
        userService.getOneUser(user.getId());

        ResponseEntity<User> responseEntity = ResponseEntity.ok(userService.saveUser(user));
        log.debug("Получили ответ {}", responseEntity);

        return responseEntity;
    }

    /**
     * @param id Удаляемый объект класса Admin
     * @return Объект ResponseEntity со статусом OK
     */
    @Operation(summary ="Удаление администратора по id")
    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteAdmin(@PathVariable Long id) {
        log.debug("Старт метода ResponseEntity<Admin> updateAdmin(@RequestBody @NotNull Admin admin) с параметром {}", id);

        userService.deleteUser(id);

        ResponseEntity<User> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        log.debug("Получили ответ {}", responseEntity);

        return responseEntity;
    }
}
