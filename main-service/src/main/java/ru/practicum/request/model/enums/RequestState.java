package ru.practicum.request.model.enums;

/**
 * Перечисление для представления состояния заявки на участие в событии.
 */
public enum RequestState {

    /**
     * Заявка подтверждена.
     */
    CONFIRMED,

    /**
     * Заявка отклонена.
     */
    REJECTED,

    /**
     * Заявка отменена.
     */
    CANCELED,

    /**
     * Заявка в ожидании.
     */
    PENDING
}
