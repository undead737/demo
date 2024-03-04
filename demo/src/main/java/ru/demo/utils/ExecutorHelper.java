package ru.demo.utils;

import lombok.extern.slf4j.Slf4j;
import ru.demo.exception.ErrorCodes;
import ru.demo.exception.ServiceException;
import ru.demo.model.enums.Operation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ExecutorHelper {

    /**
     * @param executorService   *
     * @param operation         Операция выполнения в потоке
     * @param timeout           Задерка перед прерыванием процесса
     * @throws ServiceException Операция не выполнилась в отведенный timeout
     */
    public static void awaitExecution(ExecutorService executorService, Operation operation, Long timeout) throws ServiceException {
        try {
            executorService.shutdown();
            if (!executorService.awaitTermination(timeout, TimeUnit.SECONDS)) {
                throw new ServiceException(ErrorCodes.TIMEOUT_ERROR, operation.getValue());
            }
        } catch (InterruptedException ie) {
            log.error(String.format(ErrorCodes.OPERATION_ERROR.getMessage(), operation.getValue()), ie);
        } finally {
            executorService.shutdownNow();
        }
    }
}
