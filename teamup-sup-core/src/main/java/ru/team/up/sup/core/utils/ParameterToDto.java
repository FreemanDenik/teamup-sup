package ru.team.up.sup.core.utils;

import lombok.extern.slf4j.Slf4j;
import ru.team.up.dto.AppModuleNameDto;
import ru.team.up.dto.ListSupParameterDto;
import ru.team.up.dto.SupParameterDto;
import ru.team.up.sup.core.entity.Parameter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class ParameterToDto {
    /**
     * Метод для конвертации параметра Parameter parameter в  SupParameterDto<?> dto
     * С помощью билдера собирается  SupParameterDto<?> dto
     * Билдер включает в себя метод getParameterType(), значение которого проходит через цикл для получения содержимого приходящих параметров
     * В зависимости от значения isList - цикл парсит либо единственное значение параметра, либо его множество значений
     */
    public static SupParameterDto<?> convert(Parameter parameter) {
        SupParameterDto dto = SupParameterDto.builder()
                .parameterName(parameter.getParameterName())
                .parameterType(parameter.getParameterType())
                .systemName(parameter.getSystemName())
                .isList(parameter.getIsList())
                .updateTime(parameter.getUpdateDate())
                .build();
        switch (dto.getParameterType()) {
            case DOUBLE:
                if (!parameter.getIsList()) {
                    dto.setParameterValue(Double.parseDouble(parameter.getParameterValue().get(0)));

                } else {
                    dto.setParameterValue(convertStringList(parameter.getParameterValue(), Double::parseDouble));
                }
                break;
            case BOOLEAN:
                if (!parameter.getIsList()) {
                    dto.setParameterValue(Boolean.parseBoolean(parameter.getParameterValue().get(0)));
                } else {
                    dto.setParameterValue(convertStringList(parameter.getParameterValue(), Boolean::parseBoolean));
                }
                break;
            case INTEGER:
                if (!parameter.getIsList()) {
                    dto.setParameterValue(Integer.parseInt(parameter.getParameterValue().get(0)));
                } else {
                    dto.setParameterValue(convertStringList(parameter.getParameterValue(), Integer::parseInt));
                }
                break;
            case STRING:
                if (!parameter.getIsList()) {
                    dto.setParameterValue(parameter.getParameterValue().get(0));
                } else {
                    dto.setParameterValue(parameter.getParameterValue());
                }
                break;
        }
        return dto;
    }

    /**
     * Метод парсящий из листа параметров с конвертацией в дто
     */
    public static List<ListSupParameterDto> parseParameterListToListsDto(List<Parameter> parameterList) {
        if (parameterList == null || parameterList.isEmpty()) {
            log.debug("На вход метода parseParameterListToListsDto пришел null или пустой лист");
            throw new RuntimeException("На вход метода parseParameterListToListsDto пришел null или пустой лист");
        }
        List<ListSupParameterDto> resultList = new ArrayList<>();
        for (AppModuleNameDto module : AppModuleNameDto.values()) {
            log.debug("Проверка наличия параметров из модуля {}", module);
            List<Parameter> filteredList = parameterList.stream().filter(
                            p -> p.getSystemName() == module)
                    .collect(Collectors.toList());
            if (filteredList.isEmpty()) {
                log.debug("Параметры из модуля {} не найдены", module);
                continue;
            }
            log.debug("Найдены параметры из модуля {}", module);
            ListSupParameterDto list = new ListSupParameterDto();
            for (Parameter parameter : filteredList) {
                list.addParameter(convert(parameter));
                log.debug("Параметр {} добавлен в лист модуля {}", parameter, module);
            }
            resultList.add(list);
        }
        if (resultList.isEmpty()) {
            throw new RuntimeException("Ни один параметр не добавлен в результирующий лист");
        }
        log.debug("Размер результирующего листа {}", resultList.size());
        return resultList;
    }

    /**
     * Метод для конвертации List<String> parameterValue
     */
    public static <T, U> List<U> convertStringList(List<T> listOfString, Function<T, U> function) {
        return listOfString.stream()
                .map(function)
                .collect(Collectors.toList());
    }

}
