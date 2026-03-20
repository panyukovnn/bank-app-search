package ru.panyukovnn.bankappsearch.controller;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.method.MethodValidationException;
import org.springframework.validation.method.MethodValidationResult;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import ru.panyukovnn.bankappsearch.dto.common.CommonResponse;
import ru.panyukovnn.bankappsearch.dto.common.CommonResponseDefaultErrorCode;
import ru.panyukovnn.bankappsearch.dto.common.CommonResponseError;
import ru.panyukovnn.bankappsearch.dto.common.CommonResponseFieldValidation;
import ru.panyukovnn.bankappsearch.exception.BusinessException;
import ru.panyukovnn.bankappsearch.exception.CriticalException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class CommonControllerExceptionHandler {

    public static final String REQUEST_VALIDATION_DEFAULT_MESSAGE = "Ошибка валидации входящего запроса";
    public static final String BUSINESS_EXCEPTION_DEFAULT_MESSAGE = "Ошибка бизнес-логики";
    public static final String NO_RESOURCE_EXCEPTION_DEFAULT_MESSAGE = "Отсутствует ресурс по заданному url";
    public static final String FATAL_EXCEPTION_DEFAULT_MESSAGE = "Что-то пошло не так, обратитесь к администратору";

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResponse<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();

        List<CommonResponseFieldValidation> fieldValidations = bindingResult.getFieldErrors().stream()
            .map(fe -> CommonResponseFieldValidation.builder()
                .path(fe.getField())
                .message(fe.getDefaultMessage())
                .build())
            .toList();

        log.warn("Ошибка валидации: {}", fieldValidations, e);

        CommonResponseError responseError = CommonResponseError.builder()
            .code(CommonResponseDefaultErrorCode.VALIDATION.getCode())
            .message(REQUEST_VALIDATION_DEFAULT_MESSAGE)
            .validations(fieldValidations)
            .build();

        return CommonResponse.<Void>builder()
            .error(responseError)
            .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({HandlerMethodValidationException.class, MethodValidationException.class})
    public CommonResponse<Void> handleMethodValidationException(MethodValidationResult e) {
        List<ParameterValidationResult> allValidationResults = e.getParameterValidationResults();

        List<CommonResponseFieldValidation> fieldValidations = allValidationResults.stream()
            .flatMap(parameterValidationResult -> parameterValidationResult.getResolvableErrors().stream()
                .map(messageSourceResolvable -> CommonResponseFieldValidation.builder()
                    .path(parameterValidationResult.getMethodParameter().getParameterName())
                    .message(messageSourceResolvable.getDefaultMessage())
                    .build()))
            .toList();

        log.warn("Ошибка валидации: {}", fieldValidations, e);

        CommonResponseError responseError = CommonResponseError.builder()
            .code(CommonResponseDefaultErrorCode.VALIDATION.getCode())
            .message(REQUEST_VALIDATION_DEFAULT_MESSAGE)
            .validations(fieldValidations)
            .build();

        return CommonResponse.<Void>builder()
            .error(responseError)
            .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public CommonResponse<Void> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        if (e.getCause() instanceof InvalidFormatException ife) {
            return handleInvalidFormatException(ife, e);
        }

        return handleException(e);
    }

    private CommonResponse<Void> handleInvalidFormatException(InvalidFormatException ife, HttpMessageNotReadableException e) {
        String path = ife.getPath().stream()
            .map(ref -> (ref.getFieldName() == null) ? String.valueOf(ref.getIndex()) : ref.getFieldName())
            .collect(Collectors.joining("."));

        log.warn("Ошибка валидации, указан некорректный формат поля: {}", path, e);

        CommonResponseFieldValidation fieldValidation = CommonResponseFieldValidation.builder()
            .path(path)
            .message("Некорректный формат поля")
            .build();

        CommonResponseError responseError = CommonResponseError.builder()
            .code(CommonResponseDefaultErrorCode.VALIDATION.getCode())
            .message(REQUEST_VALIDATION_DEFAULT_MESSAGE)
            .validations(List.of(fieldValidation))
            .build();

        return CommonResponse.<Void>builder()
            .error(responseError)
            .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingRequestHeaderException.class)
    public CommonResponse<Void> handleMissingRequestHeaderException(MissingRequestHeaderException e) {
        String headerName = e.getHeaderName();

        log.warn("Ошибка валидации, не задан обязательный заголовок запроса '{}'", headerName, e);

        CommonResponseFieldValidation fieldValidation = CommonResponseFieldValidation.builder()
            .path(headerName)
            .message("Не задан обязательный заголовок запроса")
            .build();

        CommonResponseError responseError = CommonResponseError.builder()
            .code(CommonResponseDefaultErrorCode.VALIDATION.getCode())
            .message(REQUEST_VALIDATION_DEFAULT_MESSAGE)
            .validations(List.of(fieldValidation))
            .build();

        return CommonResponse.<Void>builder()
            .error(responseError)
            .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public CommonResponse<Void> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        String path = e.getParameterName();

        log.warn("Ошибка валидации, не задан query параметр запроса '{}'", path, e);

        CommonResponseFieldValidation fieldValidation = CommonResponseFieldValidation.builder()
            .path(path)
            .message("Не задан query параметр запроса")
            .build();

        CommonResponseError responseError = CommonResponseError.builder()
            .code(CommonResponseDefaultErrorCode.VALIDATION.getCode())
            .message(REQUEST_VALIDATION_DEFAULT_MESSAGE)
            .validations(List.of(fieldValidation))
            .build();

        return CommonResponse.<Void>builder()
            .error(responseError)
            .build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoResourceFoundException.class)
    public CommonResponse<Void> handleNoResourceFoundException(NoResourceFoundException e) {
        log.warn("Отсутствует ресурс по заданному url: {}", e.getMessage(), e);

        CommonResponseError responseError = CommonResponseError.builder()
            .code(CommonResponseDefaultErrorCode.BUSINESS.getCode())
            .message(NO_RESOURCE_EXCEPTION_DEFAULT_MESSAGE)
            .build();

        return CommonResponse.<Void>builder()
            .error(responseError)
            .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BusinessException.class)
    public CommonResponse<Void> handleBusinessException(BusinessException e) {
        log.warn("Бизнес-исключение. location: '{}'. code: '{}'. Сообщение: '{}'", e.getLocation(), e.getCode(), e.getDisplayMessage(), e);

        String message = StringUtils.hasText(e.getDisplayMessage())
            ? e.getDisplayMessage()
            : BUSINESS_EXCEPTION_DEFAULT_MESSAGE;

        CommonResponseError responseError = CommonResponseError.builder()
            .location(e.getLocation())
            .code(e.getCode() != null ? e.getCode() : CommonResponseDefaultErrorCode.BUSINESS.getCode())
            .message(message)
            .build();

        return CommonResponse.<Void>builder()
            .error(responseError)
            .build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(CriticalException.class)
    public CommonResponse<Void> handleCriticalException(CriticalException e) {
        log.warn("Критическое исключение. location: '{}'. code: '{}'. Сообщение: '{}'", e.getLocation(), e.getCode(), e.getDisplayMessage(), e);

        String message = StringUtils.hasText(e.getDisplayMessage())
            ? e.getDisplayMessage()
            : FATAL_EXCEPTION_DEFAULT_MESSAGE;

        CommonResponseError responseError = CommonResponseError.builder()
            .location(e.getLocation())
            .code(e.getCode() != null ? e.getCode() : CommonResponseDefaultErrorCode.FATAL.getCode())
            .message(message)
            .build();

        return CommonResponse.<Void>builder()
            .error(responseError)
            .build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public CommonResponse<Void> handleException(Exception e) {
        log.error("Непредвиденное критическое исключение: {}", e.getMessage(), e);

        CommonResponseError responseError = CommonResponseError.builder()
            .code(CommonResponseDefaultErrorCode.FATAL.getCode())
            .message(FATAL_EXCEPTION_DEFAULT_MESSAGE)
            .build();

        return CommonResponse.<Void>builder()
            .error(responseError)
            .build();
    }

}