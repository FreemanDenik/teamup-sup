package ru.team.up.sup.core.service;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.team.up.dto.SupParameterDto;

import javax.annotation.PostConstruct;

@Component
public class DefaultParameterGetter {

    private final RestTemplate restTemplate;
    private final String url = "http://localhost:8080/parameters/core";

    public DefaultParameterGetter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public SupParameterDto[] getParameters() {
        return restTemplate.getForEntity(url, SupParameterDto[].class).getBody();
    }

//    @PostConstruct
//    private void init() {
//        getParameters();
//    }

}
