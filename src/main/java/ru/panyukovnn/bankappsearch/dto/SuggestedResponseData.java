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

    @NotNull
    @Schema(description = "Секции рекомендуемых результатов")
    private List<SuggestedSectionDto> section;

    @NotNull
    @Schema(description = "Последние поисковые запросы клиента")
    private List<LatestSearchDto> latestSearch;
}