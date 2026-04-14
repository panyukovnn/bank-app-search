package ru.panyukovnn.bankappsearch.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.maven.artifact.versioning.ComparableVersion;
import org.springframework.stereotype.Service;
import ru.panyukovnn.bankappsearch.dto.LatestSearchDto;
import ru.panyukovnn.bankappsearch.dto.PageDto;
import ru.panyukovnn.bankappsearch.dto.SuggestedRequestData;
import ru.panyukovnn.bankappsearch.dto.SuggestedResponseData;
import ru.panyukovnn.bankappsearch.dto.SuggestedSectionDto;
import ru.panyukovnn.bankappsearch.entity.LatestResultEntity;
import ru.panyukovnn.bankappsearch.entity.PageEntity;
import ru.panyukovnn.bankappsearch.repository.LatestResultRepository;
import ru.panyukovnn.bankappsearch.repository.PageRepository;
import ru.panyukovnn.bankappsearch.service.SuggestedService;

import java.util.List;

/**
 * Реализация сервиса для получения рекомендуемых результатов поиска
 */
@Service
@RequiredArgsConstructor
public class SuggestedServiceImpl implements SuggestedService {

    private static final String TOP_ENTITY_TYPE = "TOP";
    private static final String TOP_ENTITY_NAME = "Популярное";

    private final PageRepository pageRepository;
    private final LatestResultRepository latestResultRepository;

    @Override
    public SuggestedResponseData handleSuggested(SuggestedRequestData request) {
        List<PageEntity> topPageEntities = pageRepository.findByTopResultTrueAndPlatform(request.getPlatform());

        List<PageEntity> filteredPageEntities = filterPagesByClientVersion(topPageEntities, request.getClientVersion());

        List<LatestResultEntity> latestResultEntities = latestResultRepository.findByClientIdOrderByCreateTimeDesc(
            request.getClientId()
        );

        SuggestedSectionDto section = mapToSuggestedSection(filteredPageEntities);
        List<LatestSearchDto> latestSearch = mapToLatestSearch(latestResultEntities);

        return SuggestedResponseData.builder()
            .section(List.of(section))
            .latestSearch(latestSearch)
            .build();
    }

    private List<PageEntity> filterPagesByClientVersion(List<PageEntity> pageEntities, String clientVersion) {
        if (clientVersion == null) {
            return pageEntities;
        }

        return pageEntities.stream()
            .filter(page -> page.getVersion() == null
                || new ComparableVersion(page.getVersion()).compareTo(new ComparableVersion(clientVersion)) <= 0)
            .toList();
    }

    private SuggestedSectionDto mapToSuggestedSection(List<PageEntity> pageEntities) {
        List<PageDto> pageDtos = pageEntities.stream()
            .map(this::mapToPageDto)
            .toList();

        return SuggestedSectionDto.builder()
            .entityType(TOP_ENTITY_TYPE)
            .entityName(TOP_ENTITY_NAME)
            .pages(pageDtos)
            .build();
    }

    private PageDto mapToPageDto(PageEntity pageEntity) {
        return PageDto.builder()
            .name(pageEntity.getName())
            .link(pageEntity.getLink())
            .icon(null)
            .build();
    }

    private List<LatestSearchDto> mapToLatestSearch(List<LatestResultEntity> latestResultEntities) {
        return latestResultEntities.stream()
            .map(this::mapToLatestSearchDto)
            .toList();
    }

    private LatestSearchDto mapToLatestSearchDto(LatestResultEntity latestResultEntity) {
        return LatestSearchDto.builder()
            .id(latestResultEntity.getId().toString())
            .searchString(latestResultEntity.getSearchString())
            .build();
    }
}
