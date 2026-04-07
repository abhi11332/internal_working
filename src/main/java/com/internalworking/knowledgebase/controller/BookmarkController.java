package com.internalworking.knowledgebase.controller;

import com.internalworking.knowledgebase.dto.article.ArticleSummaryResponse;
import com.internalworking.knowledgebase.security.AppUserDetails;
import com.internalworking.knowledgebase.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @GetMapping
    public List<ArticleSummaryResponse> getBookmarks(@AuthenticationPrincipal AppUserDetails currentUser) {
        return bookmarkService.getBookmarks(currentUser);
    }

    @PostMapping("/{articleId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addBookmark(
            @PathVariable String articleId,
            @AuthenticationPrincipal AppUserDetails currentUser
    ) {
        bookmarkService.addBookmark(articleId, currentUser);
    }

    @DeleteMapping("/{articleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeBookmark(
            @PathVariable String articleId,
            @AuthenticationPrincipal AppUserDetails currentUser
    ) {
        bookmarkService.removeBookmark(articleId, currentUser);
    }
}
