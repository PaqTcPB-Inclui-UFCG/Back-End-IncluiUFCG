package com.ufcg.adptare.repository;

import com.ufcg.adptare.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, String> {
}
