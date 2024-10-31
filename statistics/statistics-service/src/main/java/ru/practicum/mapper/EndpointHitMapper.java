package ru.practicum.mapper;

import org.mapstruct.Mapper;
import ru.practicum.model.DtoEndpointHit;
import ru.practicum.model.EndpointHit;

@Mapper
public interface EndpointHitMapper {
    EndpointHit toStats(DtoEndpointHit dtoEndpointHit);

    DtoEndpointHit toDtoStats(EndpointHit endpointHit);
}
