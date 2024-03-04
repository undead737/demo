package ru.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Результат подсчета количества файлов и страниц
 */
@Data
public class DocumentsProcessResult {
    private Integer documents;
    private Integer pages;
    @JsonIgnore
    private List<String> protocolRecords = new ArrayList<>();
}
