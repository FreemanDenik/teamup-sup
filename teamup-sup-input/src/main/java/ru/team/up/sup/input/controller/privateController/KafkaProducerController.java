package ru.team.up.sup.input.controller.privateController;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.team.up.sup.core.entity.Parameter;
import ru.team.up.sup.core.service.KafkaProducerSupService;

@Slf4j
@Tag(name = "Kafka Private Controller",description = "Kafka API")
@RestController
@RequestMapping("/private/kafka")
public class KafkaProducerController {

    private KafkaProducerSupService kafkaProducerSupService;

    @Autowired
    public KafkaProducerController(KafkaProducerSupService kafkaProducerSupService) {
        this.kafkaProducerSupService = kafkaProducerSupService;
    }

    @PostMapping("/send")
    public ResponseEntity<Parameter> send(@RequestBody(required = false) Parameter parameter) {
        kafkaProducerSupService.send(parameter);
        return new ResponseEntity<>(parameter, HttpStatus.OK);
    }
}