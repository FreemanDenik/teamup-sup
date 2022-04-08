package ru.team.up.sup.core.service;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.team.up.dto.AppModuleNameDto;
import ru.team.up.dto.SupParameterDto;

import java.util.HashMap;
import java.util.Map;

@Component
public class DefaultParameterGetter {

    private final RestTemplate restTemplate;
    private String coreUrl = "http://localhost:8080/parameters/core";
    private String appUrl;
    private String authUrl;
    private String dtoUrl;
    private String externalUrl;
    private String inputUrl;
    private String kafkaUrl;
    private String monitoringUrl;
    private String moderatorUrl;
    private String supUrl;

    public DefaultParameterGetter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, SupParameterDto> getParameters(AppModuleNameDto systemName) {
        String url = "";
        switch (systemName) {
            case TEAMUP_APP:
                url = appUrl;
                break;
            case TEAMUP_AUTH:
                url = authUrl;
                break;
            case TEAMUP_CORE:
                url = coreUrl;
                break;
            case TEAMUP_DTO:
                url = dtoUrl;
                break;
            case TEAMUP_EXTERNAL:
                url = externalUrl;
                break;
            case TEAMUP_INPUT:
                url = inputUrl;
                break;
            case TEAMUP_KAFKA:
                url = kafkaUrl;
                break;
            case TEAMUP_MONITORING:
                url = monitoringUrl;
                break;
            case TEAMUP_MODERATOR:
                url = moderatorUrl;
                break;
            case TEAMUP_SUP:
                url = supUrl;
                break;
        }
        Map<String, SupParameterDto> parameterDtoMap = new HashMap<>();
        SupParameterDto[] dtoArray = restTemplate.getForEntity(url, SupParameterDto[].class).getBody();
        if (dtoArray == null) {
            throw new RuntimeException("От модуля не получены параметры по умолчанию");
        }
        for (SupParameterDto dto : dtoArray) {
            parameterDtoMap.put(dto.getParameterName(), dto);
        }
        return parameterDtoMap;
    }
}
