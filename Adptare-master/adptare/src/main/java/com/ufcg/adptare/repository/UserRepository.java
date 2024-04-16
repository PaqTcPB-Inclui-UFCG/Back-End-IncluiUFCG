package com.ufcg.adptare.repository;

import com.ufcg.adptare.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    UserDetails findByLogin(String login);

    boolean existsByLogin(String newLogin);

    byte[] findPhotoById(String userId);
}
