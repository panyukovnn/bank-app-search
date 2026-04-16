package ru.panyukovnn.bankappsearch.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Данные результата полного поиска по разделам")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchSectionResponseData {

    @JsonProperty("section")
    @Schema(description = "Результаты поиска по разделам приложения")
    private List<LocalSectionDto> localSections;

    @JsonProperty("externalSection")
    @Schema(description = "Результаты поиска из внешних источников")
    private List<ExternalSectionDto> externalSections;
}
