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

    @NotEmpty
    @Schema(description = "Название страницы")
    private String name;

    @NotEmpty
    @Schema(description = "Ссылка на страницу в приложении")
    private String link;

    @Schema(description = "Иконка страницы")
    private String icon;
}