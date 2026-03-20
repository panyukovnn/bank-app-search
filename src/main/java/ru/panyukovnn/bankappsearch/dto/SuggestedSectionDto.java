package ru.panyukovnn.bankappsearch.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
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
@Schema(description = "Секция рекомендуемых результатов")
public class SuggestedSectionDto {

    @NotEmpty
    @Schema(description = "Тип сущности секции")
    private String entityType;

    @NotEmpty
    @Schema(description = "Название секции")
    private String entityName;

    @NotNull
    @Schema(description = "Список страниц секции")
    private List<PageDto> pages;
}