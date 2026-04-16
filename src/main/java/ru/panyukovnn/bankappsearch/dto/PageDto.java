package ru.panyukovnn.bankappsearch.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Страница мобильного приложения")
public class PageDto {

    @Schema(description = "Название страницы")
    @NotEmpty(message = "Название страницы не должно быть пустым или null")
    private String name;

    @Schema(description = "Ссылка на страницу в приложении")
    @NotEmpty(message = "Ссылка на страницу в приложении не должна быть пустой или null")
    private String link;

    @Schema(description = "Иконка страницы")
    private String icon;
}