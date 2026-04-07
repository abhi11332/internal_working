package com.internalworking.knowledgebase.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "articles")
public class Article {

    @Id
    private String id;

    @Indexed
    private String title;

    private String content;

    @Indexed
    private ArticleCategory category;

    @Builder.Default
    private List<String> tags = new ArrayList<>();

    @Indexed
    private ArticleStatus status;

    private String authorId;

    private Instant createdAt;

    private Instant updatedAt;
}

