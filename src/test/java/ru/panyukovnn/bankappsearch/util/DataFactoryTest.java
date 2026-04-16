package ru.panyukovnn.bankappsearch.util;

import lombok.experimental.UtilityClass;
import ru.panyukovnn.bankappsearch.dto.EntityType;
import ru.panyukovnn.bankappsearch.dto.ExternalSectionDto;
import ru.panyukovnn.bankappsearch.dto.HistoryOperationDto;
import ru.panyukovnn.bankappsearch.dto.OperationRecordDto;
import ru.panyukovnn.bankappsearch.dto.SearchSectionRequestData;
import ru.panyukovnn.bankappsearch.dto.SearchType;
import ru.panyukovnn.bankappsearch.entity.PageEntity;

import java.util.List;
import java.util.UUID;

@UtilityClass
public class DataFactoryTest {

    public PageEntity createPageEntity() {
        return PageEntity.builder()
                .id(ConstantTest.PAGE_ENTITY_ID)
                .name(ConstantTest.PAGE_NAME)
                .platform(ConstantTest.IOS_PLATFORM)
                .version(ConstantTest.PLATFORM_VERSION)
                .link(ConstantTest.PAGE_LINK)
                .dictionary(ConstantTest.PAGE_DICTIONARY)
                .icon(ConstantTest.PAGE_ICON)
                .build();
    }

    public PageEntity createPageEntity(UUID pageEntityId, String name, String link, String icon) {
        return PageEntity.builder()
                .id(pageEntityId)
                .name(name)
                .platform(ConstantTest.IOS_PLATFORM)
                .version(ConstantTest.PLATFORM_VERSION)
                .link(link)
                .dictionary(ConstantTest.PAGE_DICTIONARY)
                .icon(icon)
                .build();
    }

    public SearchSectionRequestData createSearchSectionRequestData(List<SearchType> searchTypes, Integer size) {
        return SearchSectionRequestData.builder()
                .clientId(ConstantTest.CLIENT_ID)
                .searchString(ConstantTest.SEARCH_STRING)
                .platform(ConstantTest.IOS_PLATFORM)
                .clientVersion(ConstantTest.PLATFORM_VERSION)
                .searchTypes(searchTypes)
                .size(size)
                .build();
    }

    public OperationRecordDto createOperationRecordDto() {
        return OperationRecordDto.builder()
                .id(ConstantTest.OPERATION_ID)
                .name(ConstantTest.OPERATION_NAME)
                .categoryCode(ConstantTest.OPERATION_CATEGORY_CODE)
                .typeCode(ConstantTest.OPERATION_TYPE_CODE)
                .icon(ConstantTest.PAGE_ICON)
                .build();
    }

    public HistoryOperationDto createHistoryOperationDto() {
        return HistoryOperationDto.builder()
                .id(ConstantTest.HISTORY_ID)
                .name(ConstantTest.HISTORY_NAME)
                .amount(ConstantTest.AMOUNT)
                .currency(ConstantTest.RUB_CURRENCY)
                .date(ConstantTest.DATE_TIME_NOW)
                .build();
    }

    public List<ExternalSectionDto> createExternalSectionDtoList() {
        return List.of(
                ExternalSectionDto.builder()
                        .entityType(EntityType.OPERATION)
                        .operationRecords(List.of(createOperationRecordDto()))
                .build(),
                ExternalSectionDto.builder()
                        .entityType(EntityType.HISTORY)
                        .historyOperations(List.of(createHistoryOperationDto()))
                        .build()
        );
    }
}
