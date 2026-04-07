package com.internalworking.knowledgebase.repository;

import com.internalworking.knowledgebase.model.Article;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ArticleRepository extends MongoRepository<Article, String> {
}

