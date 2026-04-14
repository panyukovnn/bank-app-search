package ru.panyukovnn.bankappsearch.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Данные ответа с рекомендуемыми результатами")
public class SuggestedResponseData {

    @Schema(description = "Секции рекомендуемых результатов")
    @NotNull(message = "Список секций рекомендуемых результатов не должен быть null")
    private List<SuggestedSectionDto> section;

    @Schema(description = "Последние поисковые запросы клиента")
    @NotNull(message = "Список последних поисковых запросов клиента не должен быть null")
    private List<LatestSearchDto> latestSearch;
}