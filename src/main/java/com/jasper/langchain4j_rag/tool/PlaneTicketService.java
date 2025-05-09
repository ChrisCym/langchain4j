package com.jasper.langchain4j_rag.tool;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Author:Jasper Cheng
 * Date:2025/5/9
 * Description:
 */
@Service
public class PlaneTicketService implements LlmTool{


    @Tool("机票退票服务")
    public boolean unsubscribe(@P("预订单号")String ticketId, @P("客户姓名") String userName) {
        if (StringUtils.isAnyBlank(ticketId, userName)) {
            return false;
        }
        if (StringUtils.equals("程一鸣", userName)) {
            return true;
        }
        return false;
    }
}
