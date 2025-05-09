package com.jasper.langchain4j_rag.controller;

import com.jasper.langchain4j_rag.config.AiConfig;
import com.jasper.langchain4j_rag.constant.Constant;
import com.jasper.langchain4j_rag.model.infra.CommonAssistant;
import com.jasper.langchain4j_rag.model.infra.UniqueAssistant;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.community.model.dashscope.QwenStreamingChatModel;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.service.TokenStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * Author:Jasper Cheng
 * Date:2025/5/6
 * Description:
 */
@RestController
@RequestMapping("/ai")
@Slf4j
public class ChatController {

    @Autowired
    private QwenChatModel qwenChatModel;

    @Autowired
    private QwenStreamingChatModel qwenStreamingChatModel;

    @Autowired
    private CommonAssistant commonAssistant;

    @Autowired
    private UniqueAssistant uniqueAssistant;

    @RequestMapping("/chat")
    public String chat(@RequestParam(defaultValue = Constant.DEFAULT_MESSAGE) String msg) {
        return qwenChatModel.chat(msg);
    }


    @RequestMapping(value = "/chat_stream",  produces = "text/stream;charset=UTF-8")
    public Flux<String> stream(@RequestParam(defaultValue = Constant.DEFAULT_MESSAGE) String msg) {

        return Flux.create(fluxSink -> {
            qwenStreamingChatModel.generate(msg, new StreamingResponseHandler<AiMessage>() {
                @Override
                public void onNext(String s) {
                    log.info(s);
                    fluxSink.next(s);
                }

                @Override
                public void onError(Throwable throwable) {
                    log.error(throwable.getMessage());
                }

                @Override
                public void onComplete(Response<AiMessage> response) {
                    log.info("stream response: " + response.toString());
                }
            });
        });
    }

    @RequestMapping("/memory/chat")
    public String memoryChat(@RequestParam(defaultValue = Constant.DEFAULT_MESSAGE) String msg) {
        return commonAssistant.chat(msg);
    }


    @RequestMapping(value = "/memory/chat_stream",  produces = "text/stream;charset=UTF-8")
    public Flux<String> memoryStream(@RequestParam(defaultValue = Constant.DEFAULT_MESSAGE) String msg) {
        TokenStream stream = commonAssistant.stream(msg);
        return Flux.create(fluxSink -> {
           stream.onPartialResponse(s -> fluxSink.next(s))
                   .onComplete(c -> fluxSink.complete())
                   .onError(e -> fluxSink.error(e))
                   .start();
        });

    }

    @RequestMapping("/memory/unique/chat")
    public String uniqueChat(@RequestParam(defaultValue = Constant.DEFAULT_MESSAGE) String msg, String sessionId) {
        return uniqueAssistant.chat(sessionId, msg);
    }


    @RequestMapping(value = "/memory/unique/chat_stream",  produces = "text/stream;charset=UTF-8")
    public Flux<String> uniqueChatStream(@RequestParam(defaultValue = Constant.DEFAULT_MESSAGE) String msg, String sessionId) {
        TokenStream stream = uniqueAssistant.stream(sessionId, msg);
        return Flux.create(fluxSink -> {
            stream.onPartialResponse(s -> fluxSink.next(s))
                    .onComplete(c -> fluxSink.complete())
                    .onError(e -> fluxSink.error(e))
                    .start();
        });

    }

}
