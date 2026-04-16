package ru.panyukovnn.bankappsearch.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Результаты поиска из внешних источников")
public class ExternalSectionDto {

    @Schema(description = "Тип сущности раздела")
    @NotNull(message = "Тип сущности не может быть null")
    private EntityType entityType;

    @JsonProperty("records")
    @Schema(description = "Записи операций")
    private List<OperationRecordDto> operationRecords;

    @JsonProperty("operations")
    @Schema(description = "История операций")
    private List<HistoryOperationDto> historyOperations;
}
