package com.ufcg.adptare.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "tags")
@Table(name = "tags")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true)
    private String name;

    @ManyToMany(mappedBy = "tags")
    private List<Article> articles = new ArrayList<>();

    public Tag(String tagName) {
        this.name = tagName;
    }
}
