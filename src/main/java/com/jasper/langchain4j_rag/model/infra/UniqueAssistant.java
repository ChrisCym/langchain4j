package com.jasper.langchain4j_rag.model.infra;

import dev.langchain4j.service.*;

/**
 * Author:Jasper Cheng
 * Date:2025/5/6
 * Description:
 */
public interface UniqueAssistant {

    String chat(@MemoryId String memoryId, @UserMessage String message);

    TokenStream stream(@MemoryId String memoryId, @UserMessage String message);

    @SystemMessage("""
            你是一名航空公司的客服支持代理，请以友好的态度协助客户解决问题。
            你正在通过聊天系统每个与客户进行互动。
            请提供有关预订或取消预订消息或退票之前，你必须从用户处获取以下信息：预订单号、客户姓名。
            请说中文，今天的日期是{{current_date}}
            """)
    TokenStream stream(@MemoryId String memoryId, @UserMessage String message, @V("current_date") String currentDate);
}
