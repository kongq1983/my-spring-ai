package com.kq.spring.ai.zhipu.service;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmbeddingService {

    private EmbeddingModel embeddingModel;

    private final List<float[]> docVertors;

    /**
     * 准备知识库内容
     */
    private final List<String> docs = List.of(
            "美食非常美味，服务员也很友好",
            "这部电影既刺激又令人兴奋",
            "阅读书籍是扩展知识的好方法"
    );

    public EmbeddingService(EmbeddingModel embeddingModel){
        // 对知识库，进行向量化
        this.embeddingModel = embeddingModel;

        this.docVertors = this.embeddingModel.embed(docs);
    }


    public String queryBestMatch(String query) {

        // 1. 对用户传入的query，进行向量化
        float[] queryVec = embeddingModel.embed(query);

        // 2. 遍历docVertors 用与用户传入的文本向量进行计算相似度,找到最相似的

        // 记录目前最大的相似度
        double bastSim = -1;
        // 记录与当前输入文本最相似文本的下标
        int bastInx = -1;

        for(int i=0;i<docVertors.size();i++){

            // 计算余玄相似度
            double sim = cosineSimilarity(queryVec,docVertors.get(i));

            if(sim > bastSim) {
                bastSim = sim;
                bastInx = i;
            }

        }

        return docs.get(bastInx);
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
