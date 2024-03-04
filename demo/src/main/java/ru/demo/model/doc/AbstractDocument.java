package ru.demo.model.doc;

import lombok.extern.slf4j.Slf4j;
import ru.demo.exception.ErrorCodes;
import ru.demo.exception.ServiceException;

import java.io.File;
import java.nio.file.NoSuchFileException;

@Slf4j
public abstract class AbstractDocument implements Document{
    @Override
    public abstract Integer getPages(File file) throws ServiceException;

    protected void exceptionHandling(File file, Exception e) throws ServiceException {
        if (e instanceof NoSuchFileException || e.getCause() instanceof NoSuchFileException) {
            throw new ServiceException(ErrorCodes.PATH_ELEMENT_DELETED, file.getName());
        } else {
            log.error(String.format(ErrorCodes.GET_PAGES_ERROR.getMessage(), file.getName()));
            throw new ServiceException(ErrorCodes.GET_PAGES_ERROR, file.getName());
        }
    }
}
