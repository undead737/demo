package ru.demo.utils;

import org.junit.jupiter.api.Test;
import ru.demo.exception.ServiceException;
import ru.demo.model.enums.Operation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExecutorHelperTest {

    @Test
    void awaitExecution_ok() throws Exception {
        AtomicInteger nothing = new AtomicInteger(0);
        int threadPool = 4;

        ExecutorService executorService = Executors.newFixedThreadPool(4);

        for (int i = 0; i < threadPool; i++) {
            executorService.execute(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    nothing.incrementAndGet();
                    nothing.decrementAndGet();
                }
            });
        }

        assertThrows(ServiceException.class, () ->
                ExecutorHelper.awaitExecution(executorService, Operation.DOCUMENT_AND_PAGE_COUNTING, 1L));

        Thread.sleep(1);

        assertTrue(executorService.isTerminated());
        assertTrue(executorService.isShutdown());
    }
}