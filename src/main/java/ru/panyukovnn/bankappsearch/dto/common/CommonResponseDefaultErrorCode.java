package ru.panyukovnn.bankappsearch.dto.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonResponseDefaultErrorCode {

    BUSINESS("business"),
    VALIDATION("validation"),
    FATAL("fatal");

    private final String code;
}