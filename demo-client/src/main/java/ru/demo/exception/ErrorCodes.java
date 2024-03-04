package ru.demo.exception;

import lombok.Getter;

@Getter
public enum ErrorCodes implements IErrorCode{
    OPERATION_ERROR("Ошибка выполнения операции: %s", "demo_0000"),
    TIMEOUT_ERROR ("Превышено время на выполнение операции: %s", "demo_0001"),
    GET_PATH_ELEMENTS_ERROR("Ошибка обработки файла/директории: %s", "demo_0002"),
    PATH_ELEMENT_DELETED("Файл или директория больше не существуют: %s", "demo_0003"),
    PATH_ELEMENT_ACCESS_DENIED("Ошибка доступа: %s", "demo_0004"),
    GET_PAGES_ERROR("Ошибка обработки файла: %s", "demo_0005")
    ;

    private final String message;
    private final String code;

    ErrorCodes(String message, String code) {
        this.message = message;
        this.code = code;
    }

}
