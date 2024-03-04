package ru.demo.controller;

import org.springframework.web.bind.annotation.RestController;
import ru.demo.client.DocumentsRestService;
import ru.demo.model.DocumentsProcessResult;
import ru.demo.service.DocumentService;

@RestController
public class DocumentController implements DocumentsRestService {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    /**
     * Подсчет документов и количнства страниц
     * @param path путь до корневой папки
     * @return response = {@link DocumentsProcessResult}
     */
    @Override
    public DocumentsProcessResult getDocumentAndPageCounting(String path) {
        return documentService.getDocumentAndPageCounting(path);
    }
}
