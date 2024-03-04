package ru.demo.service;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.demo.exception.ServiceException;
import ru.demo.model.DocumentsProcessResult;

import java.nio.file.Path;
import java.nio.file.Paths;

import static ru.demo.exception.ErrorCodes.GET_PAGES_ERROR;

@RunWith(SpringRunner.class)
@SpringBootTest
class DocumentServiceTest {

    @Autowired
    private DocumentService documentService;

    @Test
    public void getDocumentAndPageCounting_ok() {
        Path resourceDirectory = Paths.get("src", "test", "resources", "test_dir");
        String root = resourceDirectory.toFile().getAbsolutePath();
        DocumentsProcessResult result = documentService.getDocumentAndPageCounting(root);

        Assert.assertNotNull(result.getDocuments());
        Assert.assertEquals(Integer.valueOf(6), result.getDocuments());
        Assert.assertNotNull(result.getPages());
        Assert.assertEquals(Integer.valueOf(5), result.getPages());
    }

    @Test
    public void incorrectDocument_error() {
        Path resourceDirectory = Paths.get("src", "test", "resources", "test_dir2");
        String root = resourceDirectory.toFile().getAbsolutePath();
        DocumentsProcessResult result = documentService.getDocumentAndPageCounting(root);

        Assert.assertNotNull(result.getDocuments());
        Assert.assertEquals(Integer.valueOf(1), result.getDocuments());
        Assert.assertNotNull(result.getPages());
        Assert.assertEquals(Integer.valueOf(0), result.getPages());

        Assert.assertEquals(1, result.getProtocolRecords().size());

        String er_msg = String.format(ServiceException.INTERNAL_SERVER_ERROR,
                String.format(GET_PAGES_ERROR.getMessage(), "test3.pdf"), GET_PAGES_ERROR.getCode());
        Assert.assertEquals(er_msg, result.getProtocolRecords().get(0));
    }
}