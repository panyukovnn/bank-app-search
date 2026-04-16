package ru.panyukovnn.bankappsearch.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.panyukovnn.bankappsearch.dto.PageDto;
import ru.panyukovnn.bankappsearch.dto.SearchSectionRequestData;
import ru.panyukovnn.bankappsearch.entity.LatestResultEntity;
import ru.panyukovnn.bankappsearch.entity.PageEntity;
import ru.panyukovnn.bankappsearch.util.ConstantTest;
import ru.panyukovnn.bankappsearch.util.DataFactoryTest;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class SectionMapperTest {

    private final SectionMapper sectionMapper = new SectionMapperImpl();

    @Test
    void toPageDto_whenArgumentNotNull_expectSuccessMappingAllFields() {
        PageEntity pageEntity = DataFactoryTest.createPageEntity();

        PageDto pageDto = sectionMapper.toPageDto(pageEntity);

        assertNotNull(pageDto);
        assertEquals(pageEntity.getName(), pageDto.getName());
        assertEquals(pageEntity.getLink(), pageDto.getLink());
        assertEquals(pageEntity.getIcon(), pageDto.getIcon());
    }

    @Test
    void toPageDtoList_whenArgumentNotNull_expectSuccessMappingAllFields() {
        List<PageEntity> pageEntities = List.of(
                DataFactoryTest.createPageEntity(ConstantTest.PAGE_ENTITY_ID, ConstantTest.PAGE_NAME,
                        ConstantTest.PAGE_LINK, ConstantTest.PAGE_ICON),
                DataFactoryTest.createPageEntity(ConstantTest.PAGE_ENTITY_ID_2, ConstantTest.PAGE_NAME_2,
                        ConstantTest.PAGE_LINK_2, ConstantTest.PAGE_ICON_2)
        );

        List<PageDto> pageDtoList = sectionMapper.toPageDtoList(pageEntities);

        assertNotNull(pageDtoList);
        assertEquals(pageEntities.size(), pageDtoList.size());
        IntStream.range(0, pageEntities.size())
                .forEach(i -> {
                    assertEquals(pageEntities.get(i).getName(), pageDtoList.get(i).getName());
                    assertEquals(pageEntities.get(i).getLink(), pageDtoList.get(i).getLink());
                    assertEquals(pageEntities.get(i).getIcon(), pageDtoList.get(i).getIcon());
                });
    }

    @Test
    void toLatestResultEntity_whenArgumentNotNull_expectSuccessMappingAllFields() {
        SearchSectionRequestData requestData = DataFactoryTest.createSearchSectionRequestData(List.of(), 0);

        LatestResultEntity latestResultEntity = sectionMapper.toLatestResultEntity(requestData);

        assertNotNull(latestResultEntity);
        assertEquals(requestData.getClientId(), latestResultEntity.getClientId());
        assertEquals(requestData.getSearchString(), latestResultEntity.getSearchString());
    }
}