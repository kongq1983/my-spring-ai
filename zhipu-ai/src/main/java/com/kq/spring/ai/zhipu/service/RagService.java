package com.kq.spring.ai.zhipu.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 加载文档
 * 切分文档
 */
@Slf4j
@Service
public class RagService {

    private EmbeddingModel embeddingModel;

    private List<String> docs = new ArrayList();
    private List<float[]> vectors = new ArrayList();

    /**
     * 聊天客户端
     */
    private ChatClient chatClient;

    public RagService(EmbeddingModel embeddingModel,ChatClient.Builder builder) throws Exception{
        log.debug("embeddingModel={}",embeddingModel);
        this.embeddingModel = embeddingModel;
        this.chatClient = builder.build();
        // 加载本地文档
        ClassPathResource res = new ClassPathResource("poetry.txt");
        String content = new String(res.getInputStream().readAllBytes(),"UTF-8");

        // 分隔文档内容，生成向量
        String[] split = content.split("----");

        for(String part : split) {
            System.out.println("part>>>"+part);
            // 存储切分的内容
            docs.add(part);
            // 将文档进行向量化
            vectors.add(embeddingModel.embed(part));
        }

    }

    public String answer(String question) {
        // 对用户提问进行向量化
        float[] qv = embeddingModel.embed(question);

        // 将向量化与知识库中各个向量进行相似度比对，获取top2

        // 相似度最大
        double v1 = -1;
        // 相似度第2
        double v2 = -1;

        int v1Index = -1;
        int v2Index = -1;

        for(int i=0;i<vectors.size();i++) {
            double sim = cosineSimilarity(qv,vectors.get(i));

            if(sim > v1) {
                v2 = v1;
                v1 = sim;

                v2Index = v1Index;
                v1Index = i;
            }else if(sim > v2) {
                v2 = sim;
                v2Index = i;
            }


            // 构建回复
            // 准备prompt

             }

        // 获取top2 最相似文本内容，拼接一起，做为上下文，提供给LLM

        String ctx = "";
        if(v1Index!=-1) {
            ctx = docs.get(v1Index)+ (v2Index >=0? "\n----\n"+docs.get(v2Index):"");
        }

        String prompt = "以下是知识库内容: \n"+ctx+"\n 请基于上述知识库内容回答用户答题: "+question;


        // 将获取到的top2文档内容，交给大模型
        ChatClient.CallResponseSpec resp = chatClient.prompt().system("你是知识助手，结合上下文回答用户问题")
                .user(prompt)
                .call();

        return resp.content();
    }


    /**
     * 余玄相似度计算
     * @param a
     * @param b
     * @return
     */

    private double cosineSimilarity(float[] a ,float[] b){
        double dot = 0,na = 0,nb =0;
        for(int i=0;i<a.length;i++) {
            dot += a[i] * b[i];
            na += a[i] * a[i];
            nb += b[i] * b[i];
        }

        return  dot /(Math.sqrt(na) * Math.sqrt(nb));
    }

}
