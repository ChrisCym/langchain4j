package com.jasper.langchain4j_rag.tool;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;

/**
 * Author:Jasper Cheng
 * Date:2025/5/7
 * Description:
 */
@Service
@Slf4j
public class NameService implements LlmTool{


    @Tool("全国有多少叫什么名字的人")
    public int nameCount(@P("姓名")String name){
        log.info("名字服务被调用");
        if (StringUtils.equals(name, "程一鸣")) {
            return 1;
        }
        return 10;
    }

}
