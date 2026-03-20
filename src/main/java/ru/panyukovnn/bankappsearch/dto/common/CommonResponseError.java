package ru.panyukovnn.bankappsearch.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Информация об ошибке в ответе")
public class CommonResponseError {

    @Schema(description = "Уникальный идентификатор ошибки, для удобства определения места возникновения")
    private String location;

    @Schema(description = "Человекочитаемый код ошибки")
    private String code;

    @Schema(description = "Данные ошибок валидации")
    private List<CommonResponseFieldValidation> validations;

    @Schema(description = "Отображаемый текст ошибки")
    private String message;
}