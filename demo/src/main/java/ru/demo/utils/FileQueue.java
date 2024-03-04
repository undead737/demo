package ru.demo.utils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.demo.exception.ErrorCodes;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

/**
 * Работа со структурой вложенных папок и файлов
 * <p> {@link #FileQueue} работает по принципу: записываем существующие ресурсы, работаем с этим списком </p>
 * <li> Для папок {@link QueueType#DIRECTORY DIRECTORY}: записываем путь корневой {@link #root} / всех вложенных </li>
 * <li> Для файлов {@link QueueType#FILE FILE}: записываем пути всех файлов в корневой папке {@link #root}. Глубина поиска: 1 </li>
 * <p> Ведется протоколирование исключительных ситуаций. Для исключений {@link AccessDeniedException} и {@link NoSuchFileException}
 * запись идет только в {@link #protocolRecords}. Для остальных (не обработанных) предусмотрено логирование </p>
 * <p> {@link RuntimeException} можем получить в случае вызова метода {@link #next()} без предварительной проверки {@link #hasNext()}
 *      если элементов больше не осталось / не было </p>
 */
@Slf4j
public class FileQueue implements Iterator<File> {

    @Getter
    public enum QueueType {
        DIRECTORY(Integer.MAX_VALUE),
        FILE(1)
        ;

        private final int maxDepth;

        QueueType(int maxDepth) {
            this.maxDepth = maxDepth;
        }
    }

    private final QueueType type;
    private final String root;
    private final List<String> protocolRecords;
    private String[] resources;
    private int cursor;

    public FileQueue(QueueType type, String root, List<String> protocolRecords) {
        this.type = type;
        this.root = root;
        this.protocolRecords = protocolRecords;
    }

    public File next() {
        if (!hasNext())
            throw new NoSuchElementException();
        return new File(resources[cursor++]);
    }

    public boolean hasNext() {
        if (resources == null) {
            getResources();
        }
        return cursor < resources.length;
    }

    private void getResources() {
        Boolean isDir = type.equals(QueueType.DIRECTORY);
        try (Stream<Path> stream = Files.walk(Paths.get(root), type.getMaxDepth())) {
            resources = stream
                    .filter(file -> isDir.equals(Files.isDirectory(file)))
                    .map(Path::toString)
                    .toArray(String[]::new);
        } catch (AccessDeniedException ade) {
            exceptionHandling(String.format(ErrorCodes.PATH_ELEMENT_ACCESS_DENIED.getMessage(), root));
        } catch (NoSuchFileException nsfe) {
            exceptionHandling(String.format(ErrorCodes.PATH_ELEMENT_DELETED.getMessage(), root));
        } catch (Exception e) {
            exceptionHandling(String.format(ErrorCodes.GET_PATH_ELEMENTS_ERROR.getMessage(), root), e);
        } finally {
            cursor = 0;
        }
    }

    private void exceptionHandling(String msg, Throwable th) {
        log.error(msg, th);
        exceptionHandling(msg);
    }

    private void exceptionHandling(String msg) {
        protocolRecords.add(msg);
        resources = new String[0];
    }
}
