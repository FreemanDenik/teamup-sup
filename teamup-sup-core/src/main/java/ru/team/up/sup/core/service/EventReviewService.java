package ru.team.up.sup.core.service;

import ru.team.up.sup.core.entity.EventReview;


/**
 * @author Stanislav Ivashchenko
 * Сервис для управления отзывами ru.team.up.core.entity.EventReview
 */
public interface EventReviewService {

    /**
     * Метод сохраняет отзыв для мероприятия
     */
    EventReview saveEventReview(EventReview eventReview);

}
