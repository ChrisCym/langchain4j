package com.jasper.langchain4j_rag;

import com.alibaba.dashscope.embeddings.TextEmbedding;
import dev.langchain4j.community.model.dashscope.QwenEmbeddingModel;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.junit.jupiter.api.Test;

/**
 * Author:Jasper Cheng
 * Date:2025/5/10
 * Description:
 */
public class VectorTest {


    @Test
    void testVector() {
        QwenEmbeddingModel qwenEmbeddingModel = QwenEmbeddingModel.builder().apiKey(System.getenv("ALI_AI_API_KEY")).build();
        //默认的向量模型 text-embedding-v2

        //文本向量化
        Response<Embedding> embed = qwenEmbeddingModel.embed("你好");

        System.out.println(embed.content().toString());

        System.out.println(embed.content().vector().length);

    }


    @Test
    void testProcess(){
        InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        QwenEmbeddingModel embeddingModel = QwenEmbeddingModel.builder().apiKey(System.getenv("ALI_AI_API_KEY")).build();
        //1. 文本向量化
        TextSegment textSegment1 = TextSegment.from("你好，我的名字叫程一鸣，英文名是Jasper Cheng");
        Embedding embedding1 = embeddingModel.embed(textSegment1).content();

        //2. 存储到向量数据库
        embeddingStore.add(embedding1, textSegment1);

        TextSegment textSegment2 = TextSegment.from("你好，我的名字叫程一鸣，年龄18岁");
        Embedding embedding2 = embeddingModel.embed(textSegment2).content();
        embeddingStore.add(embedding2, textSegment2);

        //3. 检索内容向量化
        Embedding query = embeddingModel.embed("我的年龄是多少？").content();
        EmbeddingSearchRequest queryRequest = EmbeddingSearchRequest.builder()
                .queryEmbedding(query)
                .maxResults(10)
                .build();

        //4. 检索
        EmbeddingSearchResult<TextSegment> result = embeddingStore.search(queryRequest);

        //5.输出
        result.matches().forEach(match -> {
            System.out.println("相似度分数：" + match.score());
            System.out.println("内容：" + match.embedded().text());
        });
    }
}
