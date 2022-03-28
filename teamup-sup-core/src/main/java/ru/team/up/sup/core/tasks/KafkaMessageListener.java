package ru.team.up.sup.core.tasks;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.team.up.dto.AppModuleNameDto;
import ru.team.up.dto.ListSupParameterDto;
import ru.team.up.sup.core.service.KafkaSupService;
import ru.team.up.sup.core.service.ParameterService;

@Slf4j
@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class KafkaMessageListener {

    private KafkaSupService kafkaSupService;
    private ParameterService parameterService;

    @KafkaListener(topics = "${kafka.topic.name}", containerFactory = "kafkaParamContainerFactory")
    public void kafkaSupParametersListListener(ListSupParameterDto listParameterDto) {
        if (listParameterDto.getList().isEmpty()) {
            log.debug("KafkaSupParametersListListener: Входящий лист параметров пуст.");
        } else {
            log.debug("KafkaSupParametersListListener, parameter list:");
            listParameterDto.getList().stream().forEach(
                    p -> log.debug(p.getParameterName() + " " + p.getParameterValue()));
        }
    }

    @KafkaListener(topics = "${kafka.init.topic.name}", containerFactory = "kafkaModuleContainerFactory")
    public void kafkaInitializationListener(AppModuleNameDto module) {
        if (module == null) {
            log.debug("KafkaInitializationListener: Имя инициализированного модуля = null.");
        } else {
            log.debug("KafkaInitializationListener: модуль {} инициализирован", module);
            kafkaSupService.sendList(parameterService.getParametersBySystemName(module));
        }
    }
}