package com.internalworking.knowledgebase.service;

import com.internalworking.knowledgebase.dto.article.ArticleSummaryResponse;
import com.internalworking.knowledgebase.exception.ConflictException;
import com.internalworking.knowledgebase.model.Article;
import com.internalworking.knowledgebase.model.ArticleStatus;
import com.internalworking.knowledgebase.model.Bookmark;
import com.internalworking.knowledgebase.repository.BookmarkRepository;
import com.internalworking.knowledgebase.repository.ArticleRepository;
import com.internalworking.knowledgebase.security.AppUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final ArticleRepository articleRepository;
    private final ArticleService articleService;

    public void addBookmark(String articleId, AppUserDetails currentUser) {
        Article article = articleService.getPublishedArticleEntity(articleId);
        if (bookmarkRepository.findByUserIdAndArticleId(currentUser.getId(), article.getId()).isPresent()) {
            throw new ConflictException("Article is already bookmarked");
        }

        bookmarkRepository.save(Bookmark.builder()
                .userId(currentUser.getId())
                .articleId(article.getId())
                .createdAt(Instant.now())
                .build());
    }

    public void removeBookmark(String articleId, AppUserDetails currentUser) {
        bookmarkRepository.deleteByUserIdAndArticleId(currentUser.getId(), articleId);
    }

    public List<ArticleSummaryResponse> getBookmarks(AppUserDetails currentUser) {
        return bookmarkRepository.findByUserId(currentUser.getId()).stream()
                .map(bookmark -> articleRepository.findById(bookmark.getArticleId()).orElse(null))
                .filter(article -> article != null && article.getStatus() == ArticleStatus.PUBLISHED)
                .sorted(Comparator.comparing(Article::getUpdatedAt).reversed())
                .map(article -> new ArticleSummaryResponse(
                        article.getId(),
                        article.getTitle(),
                        article.getContent().length() > 180 ? article.getContent().substring(0, 180) + "..." : article.getContent(),
                        article.getCategory(),
                        article.getTags(),
                        article.getStatus(),
                        article.getUpdatedAt()
                ))
                .toList();
    }
}
