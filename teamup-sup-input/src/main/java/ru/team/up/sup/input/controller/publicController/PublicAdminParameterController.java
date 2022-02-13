package ru.team.up.sup.input.controller.publicController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.team.up.dto.AppModuleNameDto;
import ru.team.up.sup.core.entity.Parameter;
import ru.team.up.sup.core.entity.ParameterType;
import ru.team.up.sup.core.service.ParameterService;
import ru.team.up.sup.core.service.UserService;

@Slf4j
@Controller
public class PublicAdminParameterController {

    private final ParameterService parameterService;
    private final UserService userService;

    @Autowired
    public PublicAdminParameterController(ParameterService parameterService, UserService userService) {
        this.parameterService = parameterService;
        this.userService = userService;
    }

    @GetMapping("/admin/parameters")
    public String adminParametersPage(ModelMap model) {
        model.addAttribute("newParameter", new Parameter());
        model.addAttribute("allParams", parameterService.getAllParameters());
        model.addAttribute("allSystems", AppModuleNameDto.values());
        model.addAttribute("allTypes", ParameterType.values());
        return "admin";
    }

    @PostMapping("/save")
    public String createParameter(Parameter parameter,
                                  @RequestParam(value = "parameterType", required = false) String parameterType,
                                  @RequestParam(value = "systemName", required = false) String systemName) {

        parameter.setParameterType(ParameterType.valueOf(parameterType));
        parameter.setSystemName(AppModuleNameDto.valueOf(systemName));
        parameter.setUserWhoLastChangeParameters(userService.getUserByName(
                                                                        SecurityContextHolder.getContext().
                                                                        getAuthentication().getName()));

        log.debug("Добавили параметр с именем {}", parameter.getParameterName());

        parameterService.saveParameter(parameter);
        return "redirect:/admin/parameters";
    }

    @GetMapping("/delete")
    public String deleteUser(long id) {

        log.debug("Удалили параметр с id {}", id);

        parameterService.deleteParameter(id);
        return "redirect:/admin/parameters";
    }

    @GetMapping("/findById")
    @ResponseBody
    public Parameter findOne(Long id) {
        return parameterService.getParameterById(id);
    }
}
