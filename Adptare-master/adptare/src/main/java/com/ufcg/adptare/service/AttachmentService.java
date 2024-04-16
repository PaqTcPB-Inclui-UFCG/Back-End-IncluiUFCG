package com.ufcg.adptare.service;

import com.ufcg.adptare.dto.attachment.AttachmentDTO;
import com.ufcg.adptare.model.Article;
import com.ufcg.adptare.model.Attachment;
import com.ufcg.adptare.repository.ArticleRepository;
import com.ufcg.adptare.repository.AttachmentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class AttachmentService {

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleRepository articleRepository;

    public List<Attachment> getAllAttachments() {
        return attachmentRepository.findAll();
    }

    public Optional<Attachment> getAttachmentById(String id) {
        return attachmentRepository.findById(id);
    }

    public Attachment saveAttachment(AttachmentDTO attachmentDTO) {

        Article article = articleService.getArticleById(attachmentDTO.articleId())
                .orElseThrow(() -> new IllegalArgumentException("Artigo nao encontrado"));

        validateFile(attachmentDTO.file());

        try {
            Attachment attachment = new Attachment();

            attachment.setNameFile(attachmentDTO.file().getOriginalFilename());
            attachment.setTypeFile(attachmentDTO.file().getContentType());
            attachment.setContent(attachmentDTO.file().getBytes());
            attachment.setArticle(article);
            return attachmentRepository.save(attachment);
        } catch (IOException e) {
            throw new RuntimeException("Erro durante o processamento do arquivo.", e);
        }
    }


    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Arquivo nao encontrado ou nullo.");
        }
    }
    @Transactional
    public void deleteAttachment(String id) {
        Optional<Attachment> attachmentOptional = attachmentRepository.findById(id);
        attachmentOptional.ifPresent(attachment -> {
            if (attachment.getArticle() != null) {
                attachment.getArticle().getAttachments().remove(attachment);
                articleRepository.save(attachment.getArticle());
            }
            attachmentRepository.deleteById(id);
        });
    }

    public byte[] downloadAttachment(String attachmentID) {
        Optional<Attachment> optionalAttachment = attachmentRepository.findById(attachmentID);

        if (optionalAttachment.isPresent()) {
            Attachment attachment = optionalAttachment.get();
            return attachment.getContent();
        } else {
            throw new EntityNotFoundException("Nao encontrado artigo de id:: " + attachmentID);
        }
    }

}