package ru.team.up.sup.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.team.up.dto.SupParameterDto;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

@Component
public class ParameterUpdater {

    private final RestTemplate restTemplate;
    private String url = "http://localhost:8080/parameters/core";

    public ParameterUpdater(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void receive() {
        ResponseEntity<SupParameterDto[]> response = restTemplate
                .getForEntity(url, SupParameterDto[].class);
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File("./Parameters.json"), response.getBody());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    private void init() {
        receive();
    }

}
