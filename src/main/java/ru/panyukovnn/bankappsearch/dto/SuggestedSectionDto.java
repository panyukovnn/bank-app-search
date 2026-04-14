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

    @Schema(description = "Тип сущности секции")
    @NotEmpty(message = "Имя типа сущности секции не должно быть пустым или null")
    private String entityType;

    @Schema(description = "Название секции")
    @NotEmpty(message = "Название секции не должно быть пустым или null")
    private String entityName;

    @Schema(description = "Список страниц секции")
    @NotNull(message = "Список страниц секции не должен быть null")
    private List<PageDto> pages;
}