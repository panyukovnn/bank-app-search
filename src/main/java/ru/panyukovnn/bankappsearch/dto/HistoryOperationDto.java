package ru.panyukovnn.bankappsearch.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Исторические данные операции")
public class HistoryOperationDto {

    @Schema(description = "Идентификатор операции")
    @NotEmpty(message = "Идентификатор операции не может быть пустым или null")
    private String id;

    @Schema(description = "Название операции")
    @NotEmpty(message = "Название операции не может быть пустым или null")
    private String name;

    @Schema(description = "Сумма операции")
    private BigDecimal amount;

    @Schema(description = "Валюта операции")
    private String currency;

    @Schema(description = "Дата/время операции")
    @NotNull(message = "Значение времени операции не может быть пустым или null")
    private OffsetDateTime date;
}
