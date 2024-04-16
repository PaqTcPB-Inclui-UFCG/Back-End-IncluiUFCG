package com.ufcg.adptare.repository;

import com.ufcg.adptare.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PostRepository  extends JpaRepository<Post, String>{
}
