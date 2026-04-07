package com.internalworking.knowledgebase.repository;

import com.internalworking.knowledgebase.model.Bookmark;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends MongoRepository<Bookmark, String> {
    Optional<Bookmark> findByUserIdAndArticleId(String userId, String articleId);

    List<Bookmark> findByUserId(String userId);

    void deleteByUserIdAndArticleId(String userId, String articleId);

    void deleteAllByArticleId(String articleId);
}
