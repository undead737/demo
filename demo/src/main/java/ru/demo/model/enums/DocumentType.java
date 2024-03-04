package ru.demo.model.enums;

import ru.demo.model.doc.DOCXDocument;
import ru.demo.model.doc.Document;
import ru.demo.model.doc.PDFDocument;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Список документов доступных для обработки
 * <li> {@link #value} - расширение файла </li>
 * <li> {@link #documentClass} - класс обработки документа </li>
 * <p> Файлы, для которых не предусмотрен подсчет количества страниц идут с стандартной реализацией {@link ru.demo.configuration.DocumentProvider#getDocument()} </p>
 */
@Getter
public enum DocumentType {
    PDF("pdf", PDFDocument.class),
    DOCX("docx", DOCXDocument.class),
    OTHER("", Document.class)
    ;

    private final String value;
    private final Class<? extends Document> documentClass;

    DocumentType(String value, Class<? extends Document> documentClass) {
        this.value = value;
        this.documentClass = documentClass;
    }

    private static final Map<String, DocumentType> lookup = Arrays.stream(values())
            .collect(Collectors.toMap(v -> v.value, v -> v));

    public static DocumentType getEnumByValue(String value) {
        DocumentType documentType = lookup.get(value);
        return documentType != null ? documentType : OTHER;
    }

}
