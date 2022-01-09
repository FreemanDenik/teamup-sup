package ru.team.up.sup.core.service;

import ru.team.up.sup.core.entity.Parameter;

import java.util.List;

public interface ParameterService {

    List<Parameter> getAllParameters();

    List<Parameter> getParametersBySystemName(String systemName);

    Parameter getParameterById(Long id);

    Parameter getParameterByParameterName(String parameterName);

    Parameter saveParameter(Parameter parameter);

    void deleteParameter(Long id);
}
