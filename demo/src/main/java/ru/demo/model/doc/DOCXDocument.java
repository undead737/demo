package ru.demo.model.doc;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Component;
import ru.demo.exception.ServiceException;

import java.io.File;

@Component("DOCXDocument")
@Slf4j
public class DOCXDocument extends AbstractDocument {
    @Override
    public Integer getPages(File file) throws ServiceException {
        try(XWPFDocument document = new XWPFDocument(OPCPackage.open(file))) {
            return document.getProperties().getExtendedProperties().getUnderlyingProperties().getPages();
        } catch (Exception e) {
            exceptionHandling(file, e);
            return 0;
        }
    }
}
