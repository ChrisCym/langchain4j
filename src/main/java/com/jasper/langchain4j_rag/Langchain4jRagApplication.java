package com.jasper.langchain4j_rag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class Langchain4jRagApplication {

    public static void main(String[] args) {
        log.info("Langchain4jRagApplication started");
        SpringApplication.run(Langchain4jRagApplication.class, args);
    }

}
