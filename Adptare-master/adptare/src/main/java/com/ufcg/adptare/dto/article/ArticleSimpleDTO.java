package com.ufcg.adptare.dto.article;

import java.util.List;

public record ArticleSimpleDTO(String titulo, String conteudoHtml, String autorId, List<String> tags, byte[] file, String descricaoImage, java.time.LocalDateTime createdAt, String id) {
}
