package com.internalworking.knowledgebase.controller;

import com.internalworking.knowledgebase.dto.article.ArticleRequest;
import com.internalworking.knowledgebase.dto.article.ArticleResponse;
import com.internalworking.knowledgebase.dto.article.ArticleSummaryResponse;
import com.internalworking.knowledgebase.dto.article.UpdateArticleStatusRequest;
import com.internalworking.knowledgebase.model.ArticleCategory;
import com.internalworking.knowledgebase.model.ArticleStatus;
import com.internalworking.knowledgebase.security.AppUserDetails;
import com.internalworking.knowledgebase.service.ArticleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping({"/api/admin/articles", "/api/v1/admin/articles"})
@RequiredArgsConstructor
public class AdminArticleController {

    private final ArticleService articleService;

    @GetMapping
    public List<ArticleSummaryResponse> getAllArticles(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) ArticleCategory category,
            @RequestParam(required = false) ArticleStatus status
    ) {
        return articleService.getAllArticles(search, category, status);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ArticleResponse createArticle(
            @Valid @RequestBody ArticleRequest request,
            @AuthenticationPrincipal AppUserDetails currentUser
    ) {
        return articleService.createArticle(request, currentUser);
    }

    @PutMapping("/{articleId}")
    public ArticleResponse updateArticle(
            @PathVariable String articleId,
            @Valid @RequestBody ArticleRequest request
    ) {
        return articleService.updateArticle(articleId, request);
    }

    @PatchMapping("/{articleId}/status")
    public ArticleResponse updateStatus(
            @PathVariable String articleId,
            @Valid @RequestBody UpdateArticleStatusRequest request
    ) {
        return articleService.updateStatus(articleId, request.status());
    }

    @DeleteMapping("/{articleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteArticle(@PathVariable String articleId) {
        articleService.deleteArticle(articleId);
    }
}
