package ru.panyukovnn.bankappsearch.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Общая обёртка запроса")
public class CommonRequest<T> {

    @Valid
    @NotNull(message = "Тело запроса не может быть пустым")
    @Schema(description = "Данные запроса")
    private T data;
}