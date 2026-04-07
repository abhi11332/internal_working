package com.internalworking.knowledgebase.dto.article;

import com.internalworking.knowledgebase.model.ArticleCategory;
import com.internalworking.knowledgebase.model.ArticleStatus;

import java.time.Instant;
import java.util.List;

public record ArticleResponse(
        String id,
        String title,
        String content,
        ArticleCategory category,
        List<String> tags,
        ArticleStatus status,
        String authorId,
        Instant createdAt,
        Instant updatedAt
) {
}

