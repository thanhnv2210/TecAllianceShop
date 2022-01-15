package com.tecalliance.shop.repository;

import com.tecalliance.shop.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
    Optional<Article> findByName(String name);
}
