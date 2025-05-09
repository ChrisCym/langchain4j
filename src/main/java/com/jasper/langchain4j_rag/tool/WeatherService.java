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
public class WeatherService implements LlmTool{


    @Tool("全国每个城市的天气")
    public String queryWeather(@P("城市")String city){
        log.info("天气服务被调用了");
        if (StringUtils.equals(city, "合肥")) {
            return "晴天";
        }
        return "雨天";
    }
}
