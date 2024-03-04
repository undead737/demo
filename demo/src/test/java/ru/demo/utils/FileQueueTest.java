package ru.demo.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.demo.exception.ErrorCodes.PATH_ELEMENT_DELETED;

class FileQueueTest {

    @Test
    @SuppressWarnings("ignored")
    void deletedDir_error(@TempDir Path tempDir) throws IOException {
        List<String> protocolRecords = new ArrayList<>();
        String tempDirPath = tempDir.toFile().getPath();

        assertTrue(Files.exists(tempDir));

        FileQueue directories = new FileQueue(FileQueue.QueueType.DIRECTORY, tempDirPath, protocolRecords);
        while (directories.hasNext()) {
            String path = directories.next().getPath();

            Files.delete(tempDir);
            assertFalse(Files.exists(tempDir));

            FileQueue files = new FileQueue(FileQueue.QueueType.FILE, path, protocolRecords);
            assertFalse(files.hasNext());

            assertEquals(1, protocolRecords.size());
            assertEquals(String.format(PATH_ELEMENT_DELETED.getMessage(), tempDirPath), protocolRecords.get(0));
        }
    }

}