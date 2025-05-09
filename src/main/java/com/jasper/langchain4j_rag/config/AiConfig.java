package com.jasper.langchain4j_rag.config;

import com.jasper.langchain4j_rag.model.infra.CommonAssistant;
import com.jasper.langchain4j_rag.model.infra.UniqueAssistant;
import com.jasper.langchain4j_rag.tool.LlmTool;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.service.AiServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Author:Jasper Cheng
 * Date:2025/5/6
 * Description:
 */
@Configuration
public class AiConfig {

    @Bean
    public CommonAssistant commonAssistant(ChatLanguageModel chatLanguageModel, StreamingChatLanguageModel streamingChatLanguageModel) {
        ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);

        //动态代理
        return AiServices.builder(CommonAssistant.class)
                .chatLanguageModel(chatLanguageModel)
                .streamingChatLanguageModel(streamingChatLanguageModel)
                .chatMemory(chatMemory)
                .build();
    }


    @Bean
    public UniqueAssistant uniqueAssistant(ChatLanguageModel chatLanguageModel
            , StreamingChatLanguageModel streamingChatLanguageModel
            , List<LlmTool> toolList) {


        //动态代理
        return AiServices.builder(UniqueAssistant.class)
                .chatLanguageModel(chatLanguageModel)
                .streamingChatLanguageModel(streamingChatLanguageModel)
                //也可以实现DB存储chatMemory
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.builder().id(memoryId).maxMessages(10).build())
                //tools
                .tools(toolList.toArray())
                .build();
    }
}
