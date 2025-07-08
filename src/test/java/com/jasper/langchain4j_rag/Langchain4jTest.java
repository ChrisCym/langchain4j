package com.jasper.langchain4j_rag;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.community.model.dashscope.WanxImageModel;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.output.Response;
import org.junit.jupiter.api.Test;

/**
 * Author:Jasper Cheng
 * Date:2025/5/6 10:04
 * Description:
 */
public class Langchain4jTest {

    /**
     * gpt-4o-mini
     */
    @Test
    void test01(){
        OpenAiChatModel model = OpenAiChatModel.builder()
                .apiKey("demo")
                .modelName("gpt-4o-mini")
                .build();

        String answer = model.chat("你好，你是谁？");
        System.out.println(answer);
    }


    /**
     * deep seek
     */
    @Test
    void test02(){
        ChatLanguageModel model = OpenAiChatModel.builder()
                .baseUrl("https://api.deepseek.com")
                .apiKey(System.getenv("DEEPSEEK_API_KEY"))
                .modelName("deepseek-chat")
                .build();

        String answer = model.chat("你好，我是Jasper,你是谁？");
        System.out.println(answer);
    }

    /**
     * qwen-max
     */
    @Test
    void test03(){
        System.out.println(System.getenv("ALI_AI_API_KEY"));

        ChatLanguageModel qwenChatModel = QwenChatModel.builder()
//                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions")
                .apiKey(System.getenv("ALI_AI_API_KEY"))
                .modelName("qwen-max")
                .build();

        String answer = qwenChatModel.chat("你好，我是Jasper,你是谁？");
        System.out.println(answer);
    }


    /**
     * 本地deepseek
     */
    @Test
    void test04(){
        ChatLanguageModel qwenChatModel = OllamaChatModel.builder()
                .baseUrl("127.0.0.1:11434")
                .modelName("xxxx")
                .build();
        String answer = qwenChatModel.chat("你好，我是Jasper,你是谁？");
        System.out.println(answer);
    }


    /**
     * 文生图-通义万相
     */
    @Test
    void test05(){

        WanxImageModel wanxImageModel = WanxImageModel.builder()
                .apiKey(System.getenv("ALI_AI_API_KEY"))
                .modelName("wanx2.1-t2i-turbo")
                .build();

        Response<Image> imageResponse = wanxImageModel.generate("在海边穿着比基尼的中国美女");
        System.out.println(imageResponse.content().url());

    }
}
