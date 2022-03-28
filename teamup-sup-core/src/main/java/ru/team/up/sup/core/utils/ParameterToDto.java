package ru.team.up.sup.core.utils;

import ru.team.up.dto.SupParameterDto;
import ru.team.up.sup.core.entity.Parameter;

public class ParameterToDto {
    public static SupParameterDto<?> convert(Parameter parameter) {
        SupParameterDto dto = SupParameterDto.builder()
                .parameterName(parameter.getParameterName())
                .systemName(parameter.getSystemName())
                .updateTime(parameter.getUpdateDate())
                .isDeleted(false)
                .build();
        switch (parameter.getParameterType()) {
            case DOUBLE:
                dto.setParameterValue(Double.parseDouble(parameter.getParameterValue()));
                break;
            case BOOLEAN:
                dto.setParameterValue(Boolean.parseBoolean(parameter.getParameterValue()));
                break;
            case INTEGER:
                dto.setParameterValue(Integer.parseInt(parameter.getParameterValue()));
                break;
            case STRING:
                dto.setParameterValue(parameter.getParameterValue());
                break;
        }
        return dto;
    }
}
