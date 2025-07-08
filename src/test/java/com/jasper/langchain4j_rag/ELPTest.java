package com.jasper.langchain4j_rag;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.community.model.dashscope.QwenEmbeddingModel;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentByCharacterSplitter;
import dev.langchain4j.data.document.splitter.DocumentBySentenceSplitter;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Author:Jasper Cheng
 * Date:2025/5/26
 * Description:
 */
public class ELPTest {

    /**
     * Rag的整个流程
     */
    @Test
    public void test01() {
        //读取文档
        Document document = FileSystemDocumentLoader.loadDocument("C:\\Users\\29799\\IdeaProjects\\langchain4j_rag\\src\\main\\resources\\static\\terms-of-service.txt", new TextDocumentParser());

        //文档分块 chunk : 拆分语义单元
//        DocumentByCharacterSplitter splitter = new DocumentByCharacterSplitter(20, 10);
        DocumentBySentenceSplitter splitter = new DocumentBySentenceSplitter(100, 20);
        List<TextSegment> segments = splitter.split(document);
        for (int i = 0; i < segments.size(); i++) {
            TextSegment segment = segments.get(i);
            System.out.println(i + " : " + segment.text());
        }

        //向量化
        QwenEmbeddingModel embeddingModel = QwenEmbeddingModel.builder()
                .apiKey(System.getenv("ALI_AI_API_KEY"))
                .build();
        List<Embedding> embeddings = embeddingModel.embedAll(segments).content();
        System.out.println(embeddings);

        InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        embeddingStore.addAll(embeddings, segments);

        //生成向量
        Response<Embedding> embed = embeddingModel.embed("退费费用");
        EmbeddingSearchRequest build = EmbeddingSearchRequest.builder()
                .queryEmbedding(embed.content())
                .maxResults(1)
                .build();
        //查询
        EmbeddingSearchResult<TextSegment> result = embeddingStore.search(build);
        for (EmbeddingMatch<TextSegment> match : result.matches()) {
            System.out.println("内容是：" + match.embedded().text() + "; 分数是：" + match.score());
        }


        //检索增强
        QwenChatModel qwenChatModel = QwenChatModel.builder()
                .apiKey(System.getenv("ALI_AI_API_KEY"))
                .modelName("qwen-max")
                .build();

        EmbeddingStoreContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(5)
                .minScore(0.6)
                .build();

        Assistant assistant = AiServices.builder(Assistant.class)
                .chatLanguageModel(qwenChatModel)
                .contentRetriever(contentRetriever)
                .build();

        String chat = assistant.chat("退费费用");
        System.out.println(chat);
    }


    public interface Assistant{

        String chat(String msg);
    }

}
