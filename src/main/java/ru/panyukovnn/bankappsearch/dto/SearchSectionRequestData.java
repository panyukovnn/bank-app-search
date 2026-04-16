package ru.panyukovnn.bankappsearch.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Данные запроса полного поиска по разделам")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchSectionRequestData {

    @Schema(description = "Идентификатор клиента")
    @NotNull(message = "Идентификатор клиента не может быть null")
    @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$",
            message = "Идентификатор клиента не соответствует формату")
    private String clientId;

    @NotNull(message = "Строка поискового запроса не может быть null")
    @Schema(description = "Текст поискового запроса")
    @Size(min = 3, message = "Длина текста поискового запроса не может быть меньше 3")
    private String searchString;

    @Schema(description = "Платформа (ios, android)")
    @NotBlank(message = "Платформа не может быть пустой")
    private String platform;

    @Schema(description = "Версия клиентского приложения")
    private String clientVersion;

    @Schema(description = "Типы поиска (PAGE, HISTORY)")
    private List<SearchType> searchTypes;

    @Schema(description = "Ограничение максимального количества результатов поиска")
    @Positive(message = "Ограничение максимального количества результатов поиска не должна быть меньше 1")
    private Integer size;
}
