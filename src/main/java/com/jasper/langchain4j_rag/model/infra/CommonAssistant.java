package com.jasper.langchain4j_rag.model.infra;

import dev.langchain4j.service.TokenStream;

/**
 * Author:Jasper Cheng
 * Date:2025/5/6
 * Description:
 */
public interface CommonAssistant {

    String chat(String message);

    TokenStream stream(String message);

}
