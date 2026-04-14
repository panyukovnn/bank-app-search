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
@Schema(description = "Основные данные операции")
public class OperationRecordDto {

    @Schema(description = "Идентификатор операции")
    @NotEmpty(message = "Идентификатор операции не может быть пустым или null")
    private String id;

    @Schema(description = "Название операции")
    @NotEmpty(message = "Название операции не может быть пустым или null")
    private String name;

    @Schema(description = "Код категории")
    @NotEmpty(message = "Код категории не может быть пустым или null")
    private String categoryCode;

    @Schema(description = "Код типа операции")
    @NotEmpty(message = "Код типа операции не может быть пустым или null")
    private String typeCode;

    @Schema(description = "Иконка страницы")
    private String icon;
}
