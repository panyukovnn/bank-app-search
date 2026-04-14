package ru.panyukovnn.bankappsearch.service.impl;

import feign.FeignException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.CollectionUtils;
import ru.panyukovnn.bankappsearch.client.OperationHistoryClient;
import ru.panyukovnn.bankappsearch.dto.ExternalSectionDto;
import ru.panyukovnn.bankappsearch.dto.PageDto;
import ru.panyukovnn.bankappsearch.dto.SearchSectionRequestData;
import ru.panyukovnn.bankappsearch.dto.SearchSectionResponseData;
import ru.panyukovnn.bankappsearch.dto.SearchType;
import ru.panyukovnn.bankappsearch.entity.LatestResultEntity;
import ru.panyukovnn.bankappsearch.entity.PageEntity;
import ru.panyukovnn.bankappsearch.mapper.SectionMapper;
import ru.panyukovnn.bankappsearch.mapper.SectionMapperImpl;
import ru.panyukovnn.bankappsearch.repository.PageRepository;
import ru.panyukovnn.bankappsearch.service.LatestResultService;
import ru.panyukovnn.bankappsearch.util.DataFactoryTest;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchSectionServiceImplTest {

    @Spy
    private final SectionMapper sectionMapper = new SectionMapperImpl();

    @Spy
    private final ExecutorService latestResultsPool = Executors.newFixedThreadPool(10);

    @Mock
    private PageRepository pageRepository;

    @Mock
    private OperationHistoryClient operationHistoryClient;

    @Mock
    private LatestResultService latestResultService;

    @InjectMocks
    private SearchSectionServiceImpl searchSectionService;

    @Test
    void findSections_whenSearchTypesIsNull_expectEmptyResult() {
        SearchSectionRequestData requestData = DataFactoryTest.createSearchSectionRequestData(null, 10);

        SearchSectionResponseData response = searchSectionService.findSections(requestData);

        assertNotNull(response);
        assertNull(response.getLocalSections());
        assertNull(response.getExternalSections());

        verify(sectionMapper)
                .toLatestResultEntity(requestData);
        verify(latestResultService, timeout(1000))
                .saveLatestResultEntityAndDeleteOldest(any(LatestResultEntity.class));
        verify(pageRepository, never())
                .findByPlatformAndVersionAndDictionaryPart(anyString(), anyString(), anyString(), any(BigDecimal.class),
                        anyInt());
        verify(operationHistoryClient, never())
                .findExternalSections(requestData);
    }

    @Test
    void findSections_whenSearchTypesIsEmpty_expectEmptyResult() {
        SearchSectionRequestData requestData = DataFactoryTest.createSearchSectionRequestData(Collections.emptyList(), 10);

        SearchSectionResponseData response = searchSectionService.findSections(requestData);

        assertNotNull(response);
        assertNull(response.getLocalSections());
        assertNull(response.getExternalSections());

        verify(sectionMapper)
                .toLatestResultEntity(requestData);
        verify(latestResultService, timeout(1000))
                .saveLatestResultEntityAndDeleteOldest(any(LatestResultEntity.class));
        verify(pageRepository, never())
                .findByPlatformAndVersionAndDictionaryPart(anyString(), anyString(), anyString(), any(BigDecimal.class),
                        anyInt());
        verify(operationHistoryClient, never())
                .findExternalSections(requestData);
    }

    @Test
    void findSections_whenSearchTypeIsOnlyPage_expectEmptyResult() {
        SearchSectionRequestData requestData = DataFactoryTest.createSearchSectionRequestData(List.of(SearchType.PAGE), 10);
        PageEntity pageEntity = DataFactoryTest.createPageEntity();

        when(pageRepository.findByPlatformAndVersionAndDictionaryPart(requestData.getPlatform(),
                requestData.getClientVersion(), requestData.getSearchString(), null, 10))
                .thenReturn(List.of(pageEntity));

        SearchSectionResponseData response = searchSectionService.findSections(requestData);

        assertNotNull(response);
        assertNotNull(response.getLocalSections());
        assertNull(response.getExternalSections());
        assertEquals(1, response.getLocalSections().size());
        assertEquals(1, response.getLocalSections().get(0).getPages().size());

        PageDto pageDto = response.getLocalSections().get(0).getPages().get(0);

        assertEquals(pageEntity.getName(), pageDto.getName());
        assertEquals(pageEntity.getLink(), pageDto.getLink());
        assertEquals(pageEntity.getIcon(), pageDto.getIcon());

        verify(sectionMapper)
                .toLatestResultEntity(requestData);
        verify(latestResultService, timeout(1000))
                .saveLatestResultEntityAndDeleteOldest(any(LatestResultEntity.class));
        verify(pageRepository)
                .findByPlatformAndVersionAndDictionaryPart(anyString(), anyString(), anyString(),
                        nullable(BigDecimal.class), anyInt());
        verify(operationHistoryClient, never())
                .findExternalSections(requestData);
    }

    @Test
    @SneakyThrows
    void findSections_whenSearchTypeIsOnlyHistoryAndOperationsHistoryClientThrowsException_expectEmptyResult() {
        SearchSectionRequestData requestData = DataFactoryTest.createSearchSectionRequestData(List.of(SearchType.HISTORY), 10);

        when(operationHistoryClient.findExternalSections(requestData))
                .thenThrow(FeignException.FeignClientException.class);

        SearchSectionResponseData response = searchSectionService.findSections(requestData);
        assertNotNull(response);
        assertNull(response.getLocalSections());
        assertNotNull(response.getExternalSections());
        assertTrue(CollectionUtils.isEmpty(response.getExternalSections()));

        verify(sectionMapper)
                .toLatestResultEntity(requestData);
        verify(latestResultService, timeout(1000))
                .saveLatestResultEntityAndDeleteOldest(any(LatestResultEntity.class));
        verify(pageRepository, never())
                .findByPlatformAndVersionAndDictionaryPart(anyString(), anyString(), anyString(),
                        nullable(BigDecimal.class), anyInt());
        verify(operationHistoryClient)
                .findExternalSections(requestData);
    }

    @Test
    @SneakyThrows
    void findSections_whenSearchTypeIsOnlyHistoryAndOperationsHistoryClientReturnSuccessfulResult_expectEmptyResult() {
        SearchSectionRequestData requestData = DataFactoryTest.createSearchSectionRequestData(List.of(SearchType.HISTORY), 10);
        List<ExternalSectionDto> externalSectionDtoList = List.of(
                ExternalSectionDto.builder()
                        .operationRecords(List.of(DataFactoryTest.createOperationRecordDto()))
                        .historyOperations(List.of(DataFactoryTest.createHistoryOperationDto()))
                        .build()
        );

        when(operationHistoryClient.findExternalSections(requestData))
                .thenReturn(externalSectionDtoList);

        SearchSectionResponseData response = searchSectionService.findSections(requestData);

        assertNotNull(response);
        assertNull(response.getLocalSections());
        assertNotNull(response.getExternalSections());
        assertEquals(1, response.getExternalSections().size());
        assertEquals(1, response.getExternalSections().get(0).getOperationRecords().size());
        assertEquals(1, response.getExternalSections().get(0).getHistoryOperations().size());

        verify(sectionMapper)
                .toLatestResultEntity(requestData);
        verify(latestResultService, timeout(1000))
                .saveLatestResultEntityAndDeleteOldest(any(LatestResultEntity.class));
        verify(pageRepository, never())
                .findByPlatformAndVersionAndDictionaryPart(anyString(), anyString(), anyString(),
                        nullable(BigDecimal.class), anyInt());
        verify(operationHistoryClient)
                .findExternalSections(requestData);
    }
}