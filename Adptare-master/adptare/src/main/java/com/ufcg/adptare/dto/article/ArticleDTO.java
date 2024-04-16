package com.ufcg.adptare.dto.article;

import java.util.List;

public record ArticleDTO(String titulo, String conteudoHtml, String autorId, List<String> tags, byte[] file, String descricaoImage) {}
