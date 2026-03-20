package ru.panyukovnn.bankappsearch.exception;

import lombok.Getter;
import ru.panyukovnn.bankappsearch.dto.common.CommonResponseDefaultErrorCode;

@Getter
public class CriticalException extends RuntimeException {

    /**
     * Идентификатор места возникновения ошибки
     */
    private String location;
    /**
     * Код ошибки
     */
    private String code = CommonResponseDefaultErrorCode.BUSINESS.getCode();
    /**
     * Текст ошибки, отображаемый в ответе
     */
    private String displayMessage;

    public CriticalException() {
        super();
    }

    public CriticalException(String displayMessage) {
        super(displayMessage);
        this.displayMessage = displayMessage;
    }

    public CriticalException(String location, String displayMessage) {
        super(displayMessage);
        this.location = location;
        this.displayMessage = displayMessage;
    }

    public CriticalException(String location, String displayMessage, Throwable cause) {
        super(cause);
        this.location = location;
        this.displayMessage = displayMessage;
    }

    public CriticalException(String location, String code, String displayMessage) {
        super(displayMessage);
        this.location = location;
        this.code = code;
        this.displayMessage = displayMessage;
    }

    public CriticalException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public CriticalException(String location, String code, String displayMessage, Throwable cause) {
        super(cause);
        this.location = location;
        this.code = code;
        this.displayMessage = displayMessage;
    }

    public CriticalException(String location, String code, String displayMessage, String message, Throwable cause) {
        super(message, cause);
        this.location = location;
        this.code = code;
        this.displayMessage = displayMessage;
    }
}