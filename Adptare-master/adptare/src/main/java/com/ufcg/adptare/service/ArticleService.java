package com.ufcg.adptare.service;

import com.ufcg.adptare.dto.article.ArticleDTO;
import com.ufcg.adptare.dto.article.ArticleSimpleDTO;
import com.ufcg.adptare.dto.attachment.AttachmentSimpleDTO;
import com.ufcg.adptare.model.Article;
import com.ufcg.adptare.model.Attachment;
import com.ufcg.adptare.model.User;
import com.ufcg.adptare.model.Tag;
import com.ufcg.adptare.repository.ArticleRepository;
import com.ufcg.adptare.repository.TagRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TagRepository tagRepository;

    public List<ArticleSimpleDTO> getAllArticles() {
        return articleRepository.findAll().stream()
                .map(this::convertToArticleDTO)
                .collect(Collectors.toList());
    }

    public Optional<ArticleSimpleDTO> getArticleDTOById(String id) {
        return articleRepository.findById(id)
                .map(this::convertToArticleDTO);
    }

    private ArticleSimpleDTO convertToArticleDTO(Article article) {
        String tagsAsString = article.getTags().stream()
                .map(Tag::getName)
                .collect(Collectors.joining(", "));
        return new ArticleSimpleDTO(
                article.getTitle(),
                article.getContent(),
                article.getAutor().getId(),
                List.of(tagsAsString.split(",")),
                article.getImage(),
                article.getImageDescription(),
                article.getCreatedDate(),
                article.getId()

        );
    }

    public Optional<Article> getArticleById(String id) {
        return articleRepository.findById(id);
    }

    public Article saveArticle(ArticleDTO articleDTO) {
        User autor = userService.getUserById(articleDTO.autorId());
        if (autor == null) {
            throw new RuntimeException("Autor não encontrado com ID: " + articleDTO.autorId());
        }
        Article article = new Article();
        article.setFavorites(0);
        article.setTitle(articleDTO.titulo());
        article.setAutor(autor);
        article.setImage(articleDTO.file());
        article.setImageDescription(articleDTO.descricaoImage());
        article.setContent(articleDTO.conteudoHtml());

        List<Tag> tags = articleDTO.tags().stream()
                .map(tagName -> tagRepository.findByName(tagName)
                        .orElseGet(() -> new Tag(tagName)))
                .collect(Collectors.toList());
        article.setTags(tags);

        return articleRepository.save(article);

    }

    // curtir um artigo
    public void likeArticle(String id) {
        Optional<Article> optionalArticle = articleRepository.findById(id);
        if (optionalArticle.isPresent()) {
            Article article = optionalArticle.get();
            article.setFavorites(article.getFavorites() + 1);
            articleRepository.save(article);
        } else {
            throw new EntityNotFoundException("Article not found with id " + id);
        }

    }

    public void deleteArticle(String id) {
        articleRepository.deleteById(id);
    }

    @Transactional
    public List<AttachmentSimpleDTO> getAllAttachmentsByArticleId(String articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article not found with ID: " + articleId));

        return article.getAttachments().stream()
                .map(this::convertToAttachmentSimpleDTO)
                .collect(Collectors.toList());
    }

    private AttachmentSimpleDTO convertToAttachmentSimpleDTO(Attachment attachment) {
        return new AttachmentSimpleDTO(
                attachment.getId(),
                attachment.getNameFile(),
                attachment.getTypeFile(),
                attachment.getContent());
    }

    public Article updateArticle(String articleId, ArticleDTO articleDTO) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Nao encontrado o artigo de ID: " + articleId));

        if (!StringUtils.isEmpty(articleDTO.titulo())) {
            article.setTitle(articleDTO.titulo());
        }
        if (!StringUtils.isEmpty(articleDTO.conteudoHtml())) {
            article.setContent(articleDTO.conteudoHtml());
        }
        if (articleDTO.file().length != 0) {
            article.setImage(articleDTO.file());
        }
        if (!StringUtils.isEmpty(articleDTO.descricaoImage())) {
            article.setImageDescription(articleDTO.descricaoImage());
        }
        return articleRepository.save(article);
    }

    public List<ArticleSimpleDTO> searchArticles(String keyword, String searchType) {
        List<Article> articles = articleRepository.findAll();

        List<ArticleSimpleDTO> matchingArticles = articles.stream()
                .filter(article -> {
                    switch (searchType) {
                        case "all":
                            return articleMatchesAllKeywords(article, keyword);
                        case "any":
                            return articleMatchesAnyKeyword(article, keyword);
                        case "exact":
                            return articleMatchesExactExpression(article, keyword);
                        default:
                            return false;
                    }
                })
                .map(this::convertToArticleDTO)
                .collect(Collectors.toList());

        return matchingArticles;
    }

    private boolean articleMatchesAllKeywords(Article article, String keyword) {
        String[] keywords = keyword.split("\\s+");
        for (String kw : keywords) {
            if (!articleMatchesKeyword(article, kw)) {
                return false;
            }
        }
        return true;
    }

    private boolean articleMatchesAnyKeyword(Article article, String keyword) {
        String[] keywords = keyword.split("\\s+");
        for (String kw : keywords) {
            if (articleMatchesKeyword(article, kw)) {
                return true;
            }
        }
        return false;
    }

    private boolean articleMatchesExactExpression(Article article, String keyword) {
        return articleMatchesKeyword(article, keyword);
    }

    private boolean articleMatchesKeyword(Article article, String keyword) {
        String lowercaseKeyword = keyword.toLowerCase();
        if (article.getTitle().toLowerCase().contains(lowercaseKeyword)) {
            return true;
        }
        if (article.getContent().toLowerCase().contains(lowercaseKeyword)) {
            return true;
        }
        for (Tag tag : article.getTags()) {
            if (tag.getName().toLowerCase().contains(lowercaseKeyword)) {
                return true;
            }
        }
        return false;
    }

    public Set<String> getAllUniqueTags() {
        Set<String> uniqueTags = new HashSet<>();
        List<Article> articles = articleRepository.findAll();

        articles.forEach(article -> {
            article.getTags().forEach(tag -> {
                uniqueTags.add(tag.getName());
            });
        });

        return uniqueTags;
    }
}
