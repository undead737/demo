package ru.demo.client;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.demo.model.DocumentsProcessResult;

@RestController(value = "/document")
public interface DocumentsRestService {

    /**
     * Подсчет документов и количества страниц
     * @param path путь до корневой папки
     * @return response = {@link DocumentsProcessResult}
     */
    @GetMapping(value = "/documentAndPageCounting", produces = MediaType.APPLICATION_JSON_VALUE)
    DocumentsProcessResult getDocumentAndPageCounting(@RequestParam(value = "path") String path);
}
