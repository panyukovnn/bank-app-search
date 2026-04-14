package ru.panyukovnn.bankappsearch.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.panyukovnn.bankappsearch.client.OperationHistoryClient;
import ru.panyukovnn.bankappsearch.dto.EntityType;
import ru.panyukovnn.bankappsearch.dto.SearchType;
import ru.panyukovnn.bankappsearch.dto.PageDto;
import ru.panyukovnn.bankappsearch.dto.ExternalSectionDto;
import ru.panyukovnn.bankappsearch.dto.LocalSectionDto;
import ru.panyukovnn.bankappsearch.dto.SearchSectionRequestData;
import ru.panyukovnn.bankappsearch.dto.SearchSectionResponseData;
import ru.panyukovnn.bankappsearch.entity.LatestResultEntity;
import ru.panyukovnn.bankappsearch.entity.PageEntity;
import ru.panyukovnn.bankappsearch.mapper.SectionMapper;
import ru.panyukovnn.bankappsearch.repository.PageRepository;
import ru.panyukovnn.bankappsearch.service.LatestResultService;
import ru.panyukovnn.bankappsearch.service.SearchSectionService;
import ru.panyukovnn.bankappsearch.util.Constant;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchSectionServiceImpl implements SearchSectionService {

    private final PageRepository pageRepository;
    private final SectionMapper sectionMapper;
    private final OperationHistoryClient operationHistoryClient;
    private final LatestResultService latestResultService;
    private final ExecutorService latestResultsPool;

    @Value("${bank-app-search.min-rank}")
    private BigDecimal minRank;

    @Override
    public SearchSectionResponseData findSections(SearchSectionRequestData requestData) {
        saveRequestToSearchHistory(requestData);

        List<LocalSectionDto> localSectionDtoList = findLocalSectionsOrNull(requestData);
        List<ExternalSectionDto> externalSectionDtoList = findExternalSectionsOrNull(requestData);

        return SearchSectionResponseData.builder()
                .localSections(localSectionDtoList)
                .externalSections(externalSectionDtoList)
                .build();
    }

    private void saveRequestToSearchHistory(SearchSectionRequestData requestData) {
        LatestResultEntity latestResultEntity = sectionMapper.toLatestResultEntity(requestData);

        CompletableFuture.runAsync(() -> latestResultService.saveLatestResultEntityAndDeleteOldest(latestResultEntity),
                latestResultsPool);

    }

    private List<LocalSectionDto> findLocalSectionsOrNull(SearchSectionRequestData requestData) {
        return isSearchTypeExists(SearchType.PAGE, requestData)
                ? List.of(buildSearchSectionDto(requestData))
                : null;
    }

    private boolean isSearchTypeExists(SearchType searchType, SearchSectionRequestData requestData) {
        return Optional.ofNullable(requestData.getSearchTypes())
                .map(searchTypes -> searchTypes.stream()
                        .anyMatch(st -> st == searchType)
                )
                .orElse(Boolean.FALSE);
    }

    private LocalSectionDto buildSearchSectionDto(SearchSectionRequestData requestData) {
        List<PageEntity> pageEntities = findPageEntities(requestData);
        List<PageDto> pageDtoList = sectionMapper.toPageDtoList(pageEntities);

        return new LocalSectionDto(EntityType.SECTION, Constant.SEARCH_SECTION_ENTITY_NAME, pageDtoList);
    }

    private List<PageEntity> findPageEntities(SearchSectionRequestData requestData) {
        String platform = requestData.getPlatform();
        String version = requestData.getClientVersion();
        String query = requestData.getSearchString();
        Integer size = Optional.ofNullable(requestData.getSize())
                .orElse(Constant.DEFAULT_SIZE_RESULTS);

        return pageRepository.findByPlatformAndVersionAndDictionaryPart(platform, version, query, minRank, size);
    }

    private List<ExternalSectionDto> findExternalSectionsOrNull(SearchSectionRequestData requestData) {
        return isSearchTypeExists(SearchType.HISTORY, requestData)
                ? findExternalSections(requestData)
                : null;
    }

    private List<ExternalSectionDto> findExternalSections(SearchSectionRequestData requestData) {
        requestData.setSearchTypes(null);

        try {
            return operationHistoryClient.findExternalSections(requestData);
        } catch (Exception fe) {
            log.warn("Ошибка при запросе к внешнему сервису: {}", fe.getMessage());

            return Collections.emptyList();
        }
    }
}
