package ru.team.up.sup.input.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.team.up.sup.core.entity.Event;

import java.time.LocalDate;

/**
 * Класс для запроса сущности мероприятия
 *
 * @author Pavel Kondrashov
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRequest {

    /**
     * Сущность мероприятия
     */
    private Event event;

    public String getEventName() {
        return null;
    }

    public LocalDate getDate() {
        return null;
    }
}
