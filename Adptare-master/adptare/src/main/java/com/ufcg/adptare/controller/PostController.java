package com.ufcg.adptare.controller;

import com.ufcg.adptare.model.Post;
import com.ufcg.adptare.repository.PostRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController()
@RequestMapping("post")
public class PostController {

    @Autowired
    PostRepository postRepository;

    @PostMapping
    public ResponseEntity postProduct(@RequestBody @Valid String text){
        Post newPost = new Post(text);

        this.postRepository.save(newPost);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> postList = postRepository.findAll();

        return ResponseEntity.ok(postList);
    }

}

