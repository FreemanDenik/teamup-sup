package ru.team.up.sup.input.controller.publicController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.team.up.sup.core.service.ParameterService;

import java.security.Principal;

@Controller
public class UserController {

    private final ParameterService parameterService;

    @Autowired
    public UserController(ParameterService parameterService) {
        this.parameterService = parameterService;
    }

    @GetMapping("/user/parameters")
    public String getParameter (Model model, Principal principal) {
        model.addAttribute("parameter",parameterService.getParameterByParameterName(principal.getName()));
        return "user";
    }
}