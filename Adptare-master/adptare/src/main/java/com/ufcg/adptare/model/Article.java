package com.ufcg.adptare.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "articles")
@Table(name = "articles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name="title")
    private String title;
    @Column(name="content")
    private String content;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "article", fetch = FetchType.EAGER)
    private List<Attachment> attachments = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "article_tags",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags = new ArrayList<>();

    @CreationTimestamp
    @Column(name="created_date")
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name="date_modified")
    private LocalDateTime dateModified;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User autor;


    @Column(name="external_references")
    private String externalReferences;

    @Column(name="image_description")
    private String imageDescription;

    @Basic(fetch = FetchType.LAZY)
    @Column(name="image")
    private byte[] image;

}
