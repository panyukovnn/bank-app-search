package ru.panyukovnn.bankappsearch.mapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import ru.panyukovnn.bankappsearch.dto.PageDto;
import ru.panyukovnn.bankappsearch.dto.SearchSectionRequestData;
import ru.panyukovnn.bankappsearch.entity.LatestResultEntity;
import ru.panyukovnn.bankappsearch.entity.PageEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SectionMapper {

    @Named("toPageDto")
    PageDto toPageDto(PageEntity pageEntity);

    @IterableMapping(qualifiedByName = "toPageDto")
    List<PageDto> toPageDtoList(List<PageEntity> pageEntities);

    LatestResultEntity toLatestResultEntity(SearchSectionRequestData requestData);
}
