package com.ufcg.adptare.model;

import com.ufcg.adptare.model.Enum.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

@Table(name = "users")
@Entity(name = "users")
@Getter
@NoArgsConstructor
@Setter
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String login;
    private String password;
    private UserRole role;
    private String firstName;
    private String lastName;
    private String fullName;
    private byte[] photo;
    private ArrayList<Article> articles;

    public User(String login, String encryptePassword, UserRole userRole, String firstName, String lastName) {
        this.login = login;
        this.role = userRole;
        this.password = encryptePassword;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = firstName + " " + lastName;
        this.articles = new ArrayList<Article>();

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == UserRole.ADMIN)
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("NEW_USER"));
        else
            return List.of(new SimpleGrantedAuthority("NEW_USER"));
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // favorita um artigo
    public void favoriteArticle(Article article) {
        articles.add(article);
    }
}
