package ru.team.up.sup.core.tasks;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.team.up.dto.SupParameterDto;
import ru.team.up.sup.core.repositories.SupRepository;


@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class KafkaMessageListener {

    private SupRepository supRepository;

    @KafkaListener(topics = "${kafka.topic.name}", containerFactory = "kafkaListenerContainerFactory")

    public void listener(SupParameterDto parameterDto) {
        if (parameterDto == null) {
            log.debug("KafkaListener: The parameter value is null.");
        } else {
            log.debug("KafkaListener, message: {}", parameterDto);
            supRepository.add(parameterDto);
        }

    }
}
