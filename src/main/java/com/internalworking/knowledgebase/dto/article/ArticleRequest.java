package com.internalworking.knowledgebase.dto.article;

import com.internalworking.knowledgebase.model.ArticleCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ArticleRequest(
        @NotBlank(message = "Title is required")
        String title,
        @NotBlank(message = "Content is required")
        String content,
        @NotNull(message = "Category is required")
        ArticleCategory category,
        List<String> tags
) {
}

