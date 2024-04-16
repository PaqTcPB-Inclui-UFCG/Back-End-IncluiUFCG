package com.ufcg.adptare.dto.attachment;

public record AttachmentSimpleDTO(String sysId, String name, String contentType, byte[] file) {
}
