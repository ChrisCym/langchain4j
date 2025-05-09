package com.jasper.langchain4j_rag.store;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;

import java.util.List;

/**
 * Author:Jasper Cheng
 * Date:2025/5/6
 * Description:实现对话记忆的持久化
 */
public class PersistentChatMemory implements ChatMemoryStore {
    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        return List.of();
    }

    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {

    }

    @Override
    public void deleteMessages(Object memoryId) {

    }
}
