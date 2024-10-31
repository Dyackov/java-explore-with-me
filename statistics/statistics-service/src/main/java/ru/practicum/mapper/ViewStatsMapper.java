package ru.practicum.mapper;

import org.mapstruct.Mapper;
import ru.practicum.model.DtoViewStats;
import ru.practicum.model.ViewStats;

@Mapper
public interface ViewStatsMapper {
    DtoViewStats toDtoViewStats(ViewStats viewStats);
}