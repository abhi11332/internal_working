package com.internalworking.knowledgebase.service;

import com.internalworking.knowledgebase.dto.article.ArticleRequest;
import com.internalworking.knowledgebase.dto.article.ArticleResponse;
import com.internalworking.knowledgebase.dto.article.ArticleSummaryResponse;
import com.internalworking.knowledgebase.exception.ForbiddenException;
import com.internalworking.knowledgebase.exception.ResourceNotFoundException;
import com.internalworking.knowledgebase.model.Article;
import com.internalworking.knowledgebase.model.ArticleCategory;
import com.internalworking.knowledgebase.model.ArticleStatus;
import com.internalworking.knowledgebase.model.Role;
import com.internalworking.knowledgebase.repository.ArticleRepository;
import com.internalworking.knowledgebase.repository.BookmarkRepository;
import com.internalworking.knowledgebase.security.AppUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final BookmarkRepository bookmarkRepository;
    private final MongoTemplate mongoTemplate;

    public ArticleResponse createArticle(ArticleRequest request, AppUserDetails currentUser) {
        Instant now = Instant.now();
        Article article = Article.builder()
                .title(request.title().trim())
                .content(request.content().trim())
                .category(request.category())
                .tags(normalizeTags(request.tags()))
                .status(ArticleStatus.DRAFT)
                .authorId(currentUser.getId())
                .createdAt(now)
                .updatedAt(now)
                .build();
        return toResponse(articleRepository.save(article));
    }

    public ArticleResponse updateArticle(String articleId, ArticleRequest request) {
        Article article = getArticleEntity(articleId);
        article.setTitle(request.title().trim());
        article.setContent(request.content().trim());
        article.setCategory(request.category());
        article.setTags(normalizeTags(request.tags()));
        article.setUpdatedAt(Instant.now());
        return toResponse(articleRepository.save(article));
    }

    public ArticleResponse updateStatus(String articleId, ArticleStatus status) {
        Article article = getArticleEntity(articleId);
        article.setStatus(status);
        article.setUpdatedAt(Instant.now());
        return toResponse(articleRepository.save(article));
    }

    public void deleteArticle(String articleId) {
        Article article = getArticleEntity(articleId);
        bookmarkRepository.deleteAllByArticleId(articleId);
        articleRepository.delete(article);
    }

    public List<ArticleSummaryResponse> getPublishedArticles(String search, ArticleCategory category) {
        Query query = new Query();
        List<Criteria> criteria = new ArrayList<>();
        criteria.add(Criteria.where("status").is(ArticleStatus.PUBLISHED));
        if (category != null) {
            criteria.add(Criteria.where("category").is(category));
        }
        if (StringUtils.hasText(search)) {
            criteria.add(Criteria.where("title").regex(Pattern.quote(search.trim()), "i"));
        }
        query.addCriteria(new Criteria().andOperator(criteria.toArray(Criteria[]::new)));
        query.with(Sort.by(Sort.Direction.DESC, "updatedAt"));
        return mongoTemplate.find(query, Article.class).stream().map(this::toSummary).toList();
    }

    public List<ArticleSummaryResponse> getAllArticles(String search, ArticleCategory category, ArticleStatus status) {
        Query query = new Query();
        List<Criteria> criteria = new ArrayList<>();
        if (category != null) {
            criteria.add(Criteria.where("category").is(category));
        }
        if (status != null) {
            criteria.add(Criteria.where("status").is(status));
        }
        if (StringUtils.hasText(search)) {
            criteria.add(Criteria.where("title").regex(Pattern.quote(search.trim()), "i"));
        }
        if (!criteria.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteria.toArray(Criteria[]::new)));
        }
        query.with(Sort.by(Sort.Direction.DESC, "updatedAt"));
        return mongoTemplate.find(query, Article.class).stream().map(this::toSummary).toList();
    }

    public ArticleResponse getArticleById(String articleId, AppUserDetails currentUser) {
        Article article = getArticleEntity(articleId);
        if (currentUser.getRole() == Role.USER && article.getStatus() != ArticleStatus.PUBLISHED) {
            throw new ForbiddenException("You can only view published articles");
        }
        return toResponse(article);
    }

    public Article getPublishedArticleEntity(String articleId) {
        Article article = getArticleEntity(articleId);
        if (article.getStatus() != ArticleStatus.PUBLISHED) {
            throw new ForbiddenException("Only published articles can be bookmarked");
        }
        return article;
    }

    private Article getArticleEntity(String articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));
    }

    private List<String> normalizeTags(List<String> tags) {
        if (tags == null) {
            return List.of();
        }

        return tags.stream()
                .filter(StringUtils::hasText)
                .map(String::trim)
                .distinct()
                .toList();
    }

    private ArticleResponse toResponse(Article article) {
        return new ArticleResponse(
                article.getId(),
                article.getTitle(),
                article.getContent(),
                article.getCategory(),
                article.getTags(),
                article.getStatus(),
                article.getAuthorId(),
                article.getCreatedAt(),
                article.getUpdatedAt()
        );
    }

    private ArticleSummaryResponse toSummary(Article article) {
        String content = article.getContent() == null ? "" : article.getContent();
        String excerpt = content.length() > 180 ? content.substring(0, 180) + "..." : content;
        return new ArticleSummaryResponse(
                article.getId(),
                article.getTitle(),
                excerpt,
                article.getCategory(),
                article.getTags(),
                article.getStatus(),
                article.getUpdatedAt()
        );
    }
}
