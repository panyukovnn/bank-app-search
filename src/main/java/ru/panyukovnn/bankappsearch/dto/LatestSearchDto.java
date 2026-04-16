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
@Schema(description = "Последний поисковый запрос клиента")
public class LatestSearchDto {

    @Schema(description = "Идентификатор результата поиска")
    @NotEmpty(message = "Идентификатор результата поиска не должен быть пустым или null")
    private String id;

    @Schema(description = "Строка поискового запроса")
    @NotEmpty(message = "Строка поискового запроса не должна быть пустой или null")
    private String searchString;
}