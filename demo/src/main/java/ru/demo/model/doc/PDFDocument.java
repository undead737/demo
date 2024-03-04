package ru.demo.model.doc;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Component;
import ru.demo.exception.ServiceException;

import java.io.File;

@Component("PDFDocument")
public class PDFDocument extends AbstractDocument {
    @Override
    public Integer getPages(File file) throws ServiceException {
        try(PDDocument document = Loader.loadPDF(file)) {
            return document.getNumberOfPages();
        } catch (Exception e) {
            exceptionHandling(file, e);
            return 0;
        }
    }
}
