package com.ufcg.adptare.controller;

import com.ufcg.adptare.dto.attachment.AttachmentDTO;
import com.ufcg.adptare.model.Attachment;
import com.ufcg.adptare.repository.AttachmentRepository;
import com.ufcg.adptare.service.AttachmentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/attachments")
public class AttachmentController {

    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private AttachmentRepository attachmentRepository;

    @GetMapping
    public List<Attachment> getAllAttachments() {
        return attachmentService.getAllAttachments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Attachment> getAttachmentById(@PathVariable String id) {
        return attachmentService.getAttachmentById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Attachment> saveAttachment(
            @RequestParam("articleId") String articleId,
            @RequestBody MultipartFile file) {
        AttachmentDTO attachmentDTO = new AttachmentDTO(articleId, file);
        Attachment savedAttachment = attachmentService.saveAttachment(attachmentDTO);
        return ResponseEntity.ok(savedAttachment);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttachment(@PathVariable String id) {
        attachmentService.deleteAttachment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/download/{attachmentID}")
    public ResponseEntity<byte[]> downloadAttachment(@PathVariable String attachmentID) {
        try {
            byte[] content = attachmentService.downloadAttachment(attachmentID);
            HttpHeaders headers = new HttpHeaders();

            String tipoConteudo = determinarTipoConteudoComBaseNoTipoDeArquivo(attachmentID);
            headers.setContentType(MediaType.parseMediaType(tipoConteudo));
            headers.setContentDispositionFormData("attachment", "attachment_file");
            return ResponseEntity.ok().headers(headers).body(content);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private String determinarTipoConteudoComBaseNoTipoDeArquivo(String attachmentID) {
        Optional<Attachment> optionalAttachment = attachmentRepository.findById(attachmentID);

        if (optionalAttachment.isPresent()) {
            Attachment attachment = optionalAttachment.get();
            return attachment.getTypeFile();
        } else {
            throw new EntityNotFoundException("Attachment not found with id: " + attachmentID);
        }
    }
}
