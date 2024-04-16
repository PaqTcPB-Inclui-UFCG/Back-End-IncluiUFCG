package com.ufcg.adptare.repository;


import com.ufcg.adptare.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, String> {
    Optional<Tag> findByName(String tagName);
}
