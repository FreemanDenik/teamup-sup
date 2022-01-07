package ru.team.up.sup.core.service;

import ru.team.up.sup.core.entity.Parameter;

import java.util.List;

public interface ParameterService {
    List<Parameter> getAllParameters();

    Parameter getOneParameter(Long id);

    Parameter saveParameter(Parameter parameter);

    void deleteParameter(Long id);
}
