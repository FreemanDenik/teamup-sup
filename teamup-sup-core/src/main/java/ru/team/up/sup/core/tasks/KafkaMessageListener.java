package ru.team.up.sup.core.tasks;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.team.up.dto.AppModuleNameDto;
import ru.team.up.sup.core.dto.ListSupParameterDto;
import ru.team.up.sup.core.service.KafkaSupService;
import ru.team.up.sup.core.service.ParameterService;

@Slf4j
@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class KafkaMessageListener {

    private KafkaSupService supService;
    private ParameterService parameterService;

    @KafkaListener(topics = "${kafka.topic.name}", containerFactory = "kafkaListenerContainerFactory")
    public void listener(ListSupParameterDto listParameterDto) {
        if (listParameterDto.getList().isEmpty()) {
            log.debug("KafkaListener: The parameter list is empty.");
        } else {
            log.debug("KafkaListener, parameter list: ");
            listParameterDto.getList().stream().forEach(p -> log.debug(p.getParameterName() + p.getParameterValue()));
        }
    }

    @KafkaListener(topics = "${kafka.init.topic.name}", containerFactory = "kafkaModuleContainerFactory")
    public void initListener(AppModuleNameDto module) {
        if (module == null) {
            log.debug("KafkaListener: The module value is null.");
        } else {
            log.debug("KafkaListener, module: {}", module);
            supService.sendList(parameterService.getParametersBySystemName(module));
        }
    }
}