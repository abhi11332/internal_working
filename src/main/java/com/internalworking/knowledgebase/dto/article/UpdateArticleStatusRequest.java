package com.internalworking.knowledgebase.dto.article;

import com.internalworking.knowledgebase.model.ArticleStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateArticleStatusRequest(
        @NotNull(message = "Status is required")
        ArticleStatus status
) {
}

