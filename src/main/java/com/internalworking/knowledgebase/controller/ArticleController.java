package com.internalworking.knowledgebase.controller;

import com.internalworking.knowledgebase.dto.article.ArticleResponse;
import com.internalworking.knowledgebase.dto.article.ArticleSummaryResponse;
import com.internalworking.knowledgebase.model.ArticleCategory;
import com.internalworking.knowledgebase.security.AppUserDetails;
import com.internalworking.knowledgebase.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping
    public List<ArticleSummaryResponse> getPublishedArticles(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) ArticleCategory category
    ) {
        return articleService.getPublishedArticles(search, category);
    }

    @GetMapping("/{articleId}")
    public ArticleResponse getArticleById(
            @PathVariable String articleId,
            @AuthenticationPrincipal AppUserDetails currentUser
    ) {
        return articleService.getArticleById(articleId, currentUser);
    }
}

