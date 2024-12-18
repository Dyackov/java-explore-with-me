package ru.practicum.event.model.enums;

/**
 * Перечисление, представляющее возможные действия для изменения состояния события.
 * Содержит два действия:
 * - SEND_TO_REVIEW (Отправить на рассмотрение)
 * - CANCEL_REVIEW (Отменить рассмотрение)
 */
public enum StateAction {
    /**
     * Отправить событие на рассмотрение.
     * Это действие инициирует процесс проверки события, прежде чем оно будет опубликовано.
     */
    SEND_TO_REVIEW,

    /**
     * Отменить отправку события на рассмотрение.
     * Это действие отменяет процесс проверки, если событие было отправлено на рассмотрение.
     */
    CANCEL_REVIEW
}
