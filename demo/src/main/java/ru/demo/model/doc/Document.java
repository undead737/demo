package ru.demo.model.doc;

import ru.demo.exception.ServiceException;

import java.io.File;

public interface Document {
    Integer getPages(File file) throws ServiceException;
}