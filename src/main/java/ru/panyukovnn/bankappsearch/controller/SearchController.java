package ru.panyukovnn.bankappsearch.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.panyukovnn.bankappsearch.dto.SuggestedResponseData;
import ru.panyukovnn.bankappsearch.dto.SuggestedRequestData;
import ru.panyukovnn.bankappsearch.dto.SearchSectionResponseData;
import ru.panyukovnn.bankappsearch.dto.SearchSectionRequestData;
import ru.panyukovnn.bankappsearch.dto.common.CommonRequest;
import ru.panyukovnn.bankappsearch.dto.common.CommonResponse;
import ru.panyukovnn.bankappsearch.service.SearchSectionService;
import ru.panyukovnn.bankappsearch.service.SuggestedService;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
@Tag(name = "Поиск", description = "API для работы с поиском в мобильном приложении")
public class SearchController {

    private final SuggestedService suggestedService;
    private final SearchSectionService searchSectionService;

    @PostMapping("/suggested")
    @Operation(summary = "Получить рекомендуемые результаты поиска")
    public CommonResponse<SuggestedResponseData> getSuggested(@RequestBody @Valid CommonRequest<SuggestedRequestData> request) {
        SuggestedResponseData responseData = suggestedService.handleSuggested(request.getData());

        return CommonResponse.<SuggestedResponseData>builder()
            .data(responseData)
            .build();
    }

    @PostMapping
    @Operation(summary = "Получить по разделам или по истории операций")
    public CommonResponse<SearchSectionResponseData> findSections(@RequestBody @Valid CommonRequest<SearchSectionRequestData> request) {
        SearchSectionResponseData responseData = searchSectionService.findSections(request.getData());

        return CommonResponse.<SearchSectionResponseData>builder()
                .data(responseData)
                .build();
    }
}
