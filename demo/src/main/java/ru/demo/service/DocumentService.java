package ru.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import ru.demo.exception.ErrorCodes;
import ru.demo.exception.ServiceException;
import ru.demo.model.DocumentsProcessResult;
import ru.demo.model.doc.Document;
import ru.demo.model.enums.DocumentType;
import ru.demo.model.enums.Operation;
import ru.demo.utils.ExecutorHelper;
import ru.demo.utils.FileQueue;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Сервис работы с документами
 */
@Service
@Slf4j
public class DocumentService {

    private final ApplicationContext beanFactory;

    /**
     * Количество потоков для обработки
     */
    @Value("${demo.thread-pool}")
    private int threadPool;

    /**
     * Время в секундах, отведенное на выполнение операции {@link Operation}
     */
    @Value("${demo.executor-timeout}")
    private long executorTimeout;

    public DocumentService(ApplicationContext beanFactory) {
        this.beanFactory = beanFactory;
    }

    /**
     * Подсчет количества файлов и страниц
     * <p> Все вложенные папки по отношению в корневой находим с помощью {@link FileQueue} </p>
     * <p> Итеррируемся по папкам, для каждой создаем итератор {@link FileQueue} по файлам (глубина поиска: 1) в новом потоке.
     * Количество одновременных потоков регулируется настроикой {@link #threadPool}. Берем в работу файлы: {@link DocumentType} </p>
     * <p> После запуска всех потоков на обработку файлов, ждем завершения обработки {@link ExecutorHelper#awaitExecution}
     * с тайм-аутом {@link #executorTimeout}. Отдаем результат </p>
     * @param root Путь до корневой папки
     * @return {@link DocumentsProcessResult} Количество файлов, страниц, протокол выполнения операции
     */
    public DocumentsProcessResult getDocumentAndPageCounting(String root) {
        Operation operation = Operation.DOCUMENT_AND_PAGE_COUNTING;
        DocumentsProcessResult documentsProcessResult = new DocumentsProcessResult();

        ExecutorService executorService = Executors.newFixedThreadPool(threadPool);
        AtomicInteger documents = new AtomicInteger(0);
        AtomicInteger pages = new AtomicInteger(0);
        List<String> protocolRecords = Collections.synchronizedList(documentsProcessResult.getProtocolRecords());

        FileQueue directories = new FileQueue(FileQueue.QueueType.DIRECTORY, root, protocolRecords);

        try {
            while (directories.hasNext()) {
                String path = directories.next().getPath();
                executorService.execute(() -> {

                    FileQueue files = new FileQueue(FileQueue.QueueType.FILE, path, protocolRecords);
                    while (!Thread.currentThread().isInterrupted() && files.hasNext()) {
                        File file = files.next();

                        documents.incrementAndGet();

                        DocumentType docType = DocumentType.getEnumByValue(FilenameUtils.getExtension(file.getName()));
                        Document document = beanFactory.getBean(docType.getDocumentClass());
                        try {
                            pages.addAndGet(document.getPages(file));
                        } catch (ServiceException se) {
                            log.error(String.format(ErrorCodes.OPERATION_ERROR.getMessage(),
                                    operation.getValue()));
                            protocolRecords.add(se.getMessage());
                        }
                    }
                });
            }

            ExecutorHelper.awaitExecution(executorService, operation, executorTimeout);

            documentsProcessResult.setDocuments(documents.get());
            documentsProcessResult.setPages(pages.get());
        } catch (ServiceException se) {
            log.error(String.format(ErrorCodes.OPERATION_ERROR.getMessage(),
                    operation.getValue()));
            documentsProcessResult.getProtocolRecords().add(se.getMessage());
        }

        return documentsProcessResult;
    }
}