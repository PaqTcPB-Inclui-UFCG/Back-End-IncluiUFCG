package com.ufcg.adptare.controller;

import com.ufcg.adptare.dto.article.ArticleSimpleDTO;
import com.ufcg.adptare.dto.attachment.AttachmentSimpleDTO;
import com.ufcg.adptare.service.ArticleService;

import jakarta.persistence.EntityNotFoundException;

import com.ufcg.adptare.model.Article;
import com.ufcg.adptare.dto.article.ArticleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/all")
    public List<ArticleSimpleDTO> getAllArticles() {
        return articleService.getAllArticles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleSimpleDTO> getArticleById(@PathVariable String id) {
        return articleService.getArticleDTOById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Article> saveArticle(@RequestBody ArticleDTO articleDTO) {
        Article savedArticle = articleService.saveArticle(articleDTO);
        return ResponseEntity.ok(savedArticle);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable String id) {
        articleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{idArticle}/attachments")
    public ResponseEntity<List<AttachmentSimpleDTO>> getAllAttachmentsByArticleId(@PathVariable String idArticle) {
        List<AttachmentSimpleDTO> attachments = articleService.getAllAttachmentsByArticleId(idArticle);
        return ResponseEntity.ok(attachments);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable String id, @RequestBody ArticleDTO articleDTO) {
        Article updatedArticle = articleService.updateArticle(id, articleDTO);
        return ResponseEntity.ok(updatedArticle);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ArticleSimpleDTO>> searchArticles(
            @RequestParam String keyword,
            @RequestParam String searchType) {
        return ResponseEntity.ok(articleService.searchArticles(keyword, searchType));
    }

    @GetMapping("/getAllTags")
    public ResponseEntity<Set<String>> getAllUniqueTags() {
        Set<String> uniqueTags = articleService.getAllUniqueTags();
        return ResponseEntity.ok(uniqueTags);
    }

   

}
