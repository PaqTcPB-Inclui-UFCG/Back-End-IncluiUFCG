package com.ufcg.adptare.repository;


import com.ufcg.adptare.model.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttachmentRepository extends JpaRepository<Attachment, String> {}
