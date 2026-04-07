package com.internalworking.knowledgebase.dto.article;

import com.internalworking.knowledgebase.model.ArticleCategory;
import com.internalworking.knowledgebase.model.ArticleStatus;

import java.time.Instant;
import java.util.List;

public record ArticleSummaryResponse(
        String id,
        String title,
        String excerpt,
        ArticleCategory category,
        List<String> tags,
        ArticleStatus status,
        Instant updatedAt
) {
}

