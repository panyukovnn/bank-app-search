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

    @NotEmpty
    @Schema(description = "Идентификатор результата поиска")
    private String id;

    @NotEmpty
    @Schema(description = "Строка поискового запроса")
    private String searchString;
}