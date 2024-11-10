package ru.practicum.request.model.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.dto.ParticipationRequestDto;

@Mapper
public interface RequestMapper {
    @Mapping(source = "requester", target = "requester.id")
    @Mapping(source = "event", target = "event.id")
    Request toRequest(ParticipationRequestDto participationRequestDto);

    @InheritInverseConfiguration(name = "toRequest")
    ParticipationRequestDto toParticipationRequestDto(Request request);
}