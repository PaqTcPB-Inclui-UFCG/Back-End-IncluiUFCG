package com.ufcg.adptare.controller;

import com.ufcg.adptare.dto.user.ChangePasswordDTO;
import com.ufcg.adptare.dto.user.UserPhotoDTO;
import com.ufcg.adptare.dto.user.UserPatchDTO;
import com.ufcg.adptare.dto.user.UserSimpleDTO;
import com.ufcg.adptare.exception.UserException;
import com.ufcg.adptare.model.User;
import com.ufcg.adptare.service.ArticleService;
import com.ufcg.adptare.service.UserService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    private ArticleService articleService;

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        try {
            List<UserSimpleDTO> users = this.userService.getAll();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao obter usuários: " + e.getMessage());
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable String userId) {
        try {
            User user = this.userService.getUserById(userId);
            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (UserException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao obter usuário por ID: " + e.getMessage());
        }
    }

    @GetMapping("/userByEmail={email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        try {
            UserDetails user = this.userService.getUserByEmail(email);
            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (UserException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao obter usuário por Email: " + e.getMessage());
        }
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable String userId, @RequestBody @Valid UserPatchDTO userPatchDTO) {
        try {
            this.userService.updateUser(userId, userPatchDTO);
            return ResponseEntity.ok().build();
        } catch (UserException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar usuário: " + e.getMessage());
        }
    }

    @PatchMapping("/{userId}/change-login")
    public ResponseEntity<?> changeLogin(@PathVariable String userId, @RequestParam @Valid String newLogin) {
        try {
            this.userService.changeLogin(userId, newLogin);
            return ResponseEntity.ok().build();
        } catch (UserException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao alterar login: " + e.getMessage());
        }
    }

    @PatchMapping("/{userId}/change-password")
    public ResponseEntity<?> changePassword(@PathVariable String userId, @RequestBody @Valid ChangePasswordDTO changePasswordDTO) {
        try {
            this.userService.changePassword(userId, changePasswordDTO.currentPassword(), changePasswordDTO.newPassword());
            return ResponseEntity.ok().build();
        } catch (UserException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao alterar senha: " + e.getMessage());
        }
    }

    @PostMapping("/{userId}/photo")
    public ResponseEntity<?> changePhoto(@PathVariable String userId, @RequestBody UserPhotoDTO changePhotoUserDTO) {
        try {
            Optional<UserPhotoDTO> userPhotoDTO= this.userService.changePhoto(userId, changePhotoUserDTO.photo());
            return ResponseEntity.ok().body(userPhotoDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar a foto do usuário." + e);
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable String userId) {
        try {
            this.userService.deleteUserById(userId);
            return ResponseEntity.noContent().build();
        } catch (UserException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao excluir usuário: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor");
        }
    }
    @GetMapping("/{userId}/photo")
    public ResponseEntity<byte[]> getPhoto(@PathVariable String userId) {
        byte[] photo = this.userService.getPhoto(userId);
        if (photo != null) {
            return ResponseEntity.ok().body(photo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

     // Curtir Artigo
    @PutMapping("{userId}/{idArticle}/like")
    public ResponseEntity<?> likeArticle(@PathVariable String idArticle, @PathVariable String userId) {
        try {
            articleService.likeArticle(idArticle,userId);
            return ResponseEntity.ok(new String[] { "Article liked successfully!" });
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }




}
