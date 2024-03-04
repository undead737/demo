package ru.demo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import ru.demo.model.doc.Document;

@Configuration
public class DocumentProvider {

    @Bean
    @Primary
    public Document getDocument() {
        return file -> 0;
    }
}
