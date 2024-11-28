package ru.practicum.request.model.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.dto.ParticipationRequestDto;

/**
 * Маппер для преобразования между сущностью заявки (Request) и DTO (ParticipationRequestDto).
 */
@Mapper
public interface RequestMapper {

    /**
     * Преобразует DTO заявки в сущность.
     *
     * @param participationRequestDto DTO заявки на участие в событии.
     * @return Сущность заявки (Request).
     */
    @Mapping(source = "requester", target = "requester.id")
    @Mapping(source = "event", target = "event.id")
    Request toRequest(ParticipationRequestDto participationRequestDto);

    /**
     * Преобразует сущность заявки в DTO.
     *
     * @param request Сущность заявки (Request).
     * @return DTO заявки на участие в событии.
     */
    @InheritInverseConfiguration(name = "toRequest")
    ParticipationRequestDto toParticipationRequestDto(Request request);
}
