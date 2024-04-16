package com.ufcg.adptare.dto.attachment;

import org.springframework.web.multipart.MultipartFile;

public record AttachmentDTO(String articleId, MultipartFile file) {}
