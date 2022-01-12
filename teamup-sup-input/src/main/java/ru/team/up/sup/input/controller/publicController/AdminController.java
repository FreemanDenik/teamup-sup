package ru.team.up.sup.input.controller.publicController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.team.up.sup.core.entity.Parameter;
import ru.team.up.sup.core.service.ParameterService;

import java.util.List;


@Controller
public class AdminController {

    private final ParameterService parameterService;

    @Autowired
    public AdminController(ParameterService parameterService) {
        this.parameterService = parameterService;
    }

    @GetMapping("/")
    public String getParameterList(Model model) {
        List<Parameter> parameters = parameterService.getAllParameters();
        model.addAttribute("parameters", parameters);
        return "admin";
    }

    @GetMapping("/admin")
    public String createParameterFrom(Parameter parameter, Model model) {
        model.addAttribute("parameter", parameter);
        List<Parameter> parameters = parameterService.getAllParameters();
        model.addAttribute("parameters", parameters);
        return "admin";
    }

    @PostMapping("/admin")
    public String createParameter(Parameter parameter) {
        parameterService.saveParameter(parameter);
        return "redirect:/admin";
    }

    @PostMapping ("/admin/{id}/delete")
    public String removeParameter(@PathVariable("id") long id) {
        parameterService.deleteParameter(id);
        return "redirect:/admin";
    }

    @GetMapping("/admin/{id}/edit")
    public String updateParameterForm(@PathVariable("id") long id, Model model) {
        Parameter parameter = parameterService.getParameterById(id);
        model.addAttribute("parameter", parameter);
        return "redirect:/admin";
    }

//    @PostMapping("/admin/{id}/edit")
//    public String updateParameter(Parameter parameter) {
//        parameterService.updateParameter(parameter);
//        return "redirect:/admin";
//    }
}