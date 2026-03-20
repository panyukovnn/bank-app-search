package ru.panyukovnn.bankappsearch.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Данные запроса рекомендуемых результатов")
public class SuggestedRequestData {

    @NotBlank(message = "Идентификатор клиента не может быть пустым")
    @Schema(description = "Идентификатор клиента")
    private String clientId;

    @NotBlank(message = "Платформа не может быть пустой")
    @Schema(description = "Платформа (ios, android)")
    private String platform;

    @Schema(description = "Версия клиентского приложения")
    private String clientVersion;
}