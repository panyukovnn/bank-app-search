package ru.panyukovnn.bankappsearch.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.maven.artifact.versioning.ComparableVersion;
import org.springframework.stereotype.Service;
import ru.panyukovnn.bankappsearch.dto.LatestSearchDto;
import ru.panyukovnn.bankappsearch.dto.PageDto;
import ru.panyukovnn.bankappsearch.dto.SuggestedRequestData;
import ru.panyukovnn.bankappsearch.dto.SuggestedResponseData;
import ru.panyukovnn.bankappsearch.dto.SuggestedSectionDto;
import ru.panyukovnn.bankappsearch.entity.LatestResult;
import ru.panyukovnn.bankappsearch.entity.Page;
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
        List<Page> topPages = pageRepository.findByTopResultTrueAndPlatform(request.getPlatform());

        List<Page> filteredPages = filterPagesByClientVersion(topPages, request.getClientVersion());

        List<LatestResult> latestResults = latestResultRepository.findByClientIdOrderByCreateTimeDesc(
            request.getClientId()
        );

        SuggestedSectionDto section = mapToSuggestedSection(filteredPages);
        List<LatestSearchDto> latestSearch = mapToLatestSearch(latestResults);

        return SuggestedResponseData.builder()
            .section(List.of(section))
            .latestSearch(latestSearch)
            .build();
    }

    private List<Page> filterPagesByClientVersion(List<Page> pages, String clientVersion) {
        if (clientVersion == null) {
            return pages;
        }

        return pages.stream()
            .filter(page -> page.getVersion() == null
                || new ComparableVersion(page.getVersion()).compareTo(new ComparableVersion(clientVersion)) <= 0)
            .toList();
    }

    private SuggestedSectionDto mapToSuggestedSection(List<Page> pages) {
        List<PageDto> pageDtos = pages.stream()
            .map(this::mapToPageDto)
            .toList();

        return SuggestedSectionDto.builder()
            .entityType(TOP_ENTITY_TYPE)
            .entityName(TOP_ENTITY_NAME)
            .pages(pageDtos)
            .build();
    }

    private PageDto mapToPageDto(Page page) {
        return PageDto.builder()
            .name(page.getName())
            .link(page.getLink())
            .icon(null)
            .build();
    }

    private List<LatestSearchDto> mapToLatestSearch(List<LatestResult> latestResults) {
        return latestResults.stream()
            .map(this::mapToLatestSearchDto)
            .toList();
    }

    private LatestSearchDto mapToLatestSearchDto(LatestResult latestResult) {
        return LatestSearchDto.builder()
            .id(latestResult.getId().toString())
            .searchString(latestResult.getSearchString())
            .build();
    }
}
