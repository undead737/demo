package ru.demo.model.enums;

import lombok.Getter;

@Getter
public enum Operation {
    DOCUMENT_AND_PAGE_COUNTING("Подсчет документов и количества страниц")
    ;

    private final String value;

    Operation(String value) {
        this.value = value;
    }
}
