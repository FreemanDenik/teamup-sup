package ru.team.up.sup.core.dto;

import lombok.Data;
import ru.team.up.dto.SupParameterDto;

import java.util.ArrayList;
import java.util.List;

@Data
public class ListSupParameterDto {
    private List<SupParameterDto<?>> list = new ArrayList<>();

    public void addParameter(SupParameterDto<?> parameter) {
        list.add(parameter);
    }
}
