package com.ufcg.adptare.service;

import com.ufcg.adptare.dto.user.UserPatchDTO;
import com.ufcg.adptare.dto.user.UserPhotoDTO;
import com.ufcg.adptare.dto.user.UserSimpleDTO;
import com.ufcg.adptare.exception.UserException;
import com.ufcg.adptare.model.User;
import com.ufcg.adptare.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public void updateUser(String userId, UserPatchDTO userPatchDTO) {
        User user = getUserById(userId);
        if (user != null) {
            User changeUser = applyPatch(user, userPatchDTO);
            this.userRepository.save(changeUser);
        }
    }

    public List<UserSimpleDTO> getAll() {
        List<User> users = this.userRepository.findAll();
        List<UserSimpleDTO> simpleUserList = new ArrayList<>();

        for (User user : users) {
            simpleUserList.add(new UserSimpleDTO(user.getFullName(), user.getLogin(), user.getRole(), user.getId()));
        }

        return simpleUserList;
    }

    public void changeLogin(String userId, String newLogin) {
        if (!isValidEmail(newLogin)) {
            throw UserException.invalidEmail("O login deve ser um email válido");
        }

        if (this.userRepository.existsByLogin(newLogin)) {
            throw UserException.duplicateEmail("E-mail já cadastrado");
        }

        User user = getUserById(userId);
        if (user != null) {
            user.setLogin(newLogin);
            this.userRepository.save(user);
        }
    }

    public Optional<UserPhotoDTO> changePhoto(String userId, byte[] newPhoto) {
        User user = this.getUserById(userId);
        System.out.println(user);
        if (user != null) {
            user.setPhoto(newPhoto);
            this.userRepository.save(user);
            return Optional.of(new UserPhotoDTO(newPhoto));
        }
        return Optional.empty();
    }

    public void changePassword(String userId, String currentPassword, String newPassword) {
        User user = this.getUserById(userId);
        if (user != null) {
            if (!new BCryptPasswordEncoder().matches(currentPassword, user.getPassword())) {
                throw UserException.invalidPassword("Senha atual incorreta");
            }
            this.validatePassword(newPassword);
            user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
            this.userRepository.save(user);
        }
    }

    public User getUserById(String userId) {
        Optional<User> userOptional = this.userRepository.findById(userId);
        return userOptional.orElse(null);
    }

    public UserDetails getUserByEmail(String email) {
        return this.userRepository.findByLogin(email);
     }

    private User applyPatch(User user, UserPatchDTO userPatchDTO) {
        if (userPatchDTO.firstName() != null) {
            user.setFirstName(userPatchDTO.firstName());
        }
        if (userPatchDTO.lastName() != null) {
            user.setLastName(userPatchDTO.lastName());
        }
        if (userPatchDTO.login() != null) {
            this.changeLogin(userPatchDTO.login(), user.getId());
        }
        return user;
    }

    public boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void validatePassword(String password) {
        validateLength(password);
        validateLowerCaseUpperCase(password);
        validateNumber(password);
        validateSpecialCharacter(password);
        avoidSimpleSequences(password);
    }

    private void validateLength(String password) {
        if ( password.length() < 8) {
            throw UserException.invalidPassword("A senha deve ter no mínimo 8 caracteres");
        }
    }

    private void validateLowerCaseUpperCase(String password) {
        if (!password.matches(".*[a-z].*") || !password.matches(".*[A-Z].*")) {
            throw UserException.invalidPassword("A senha deve incluir pelo menos uma letra maiúscula e uma minúscula");
        }
    }

    private void validateNumber(String password) {
        if (!password.matches(".*\\d.*")) {
            throw UserException.invalidPassword("A senha deve incluir pelo menos um número");
        }
    }

    private void validateSpecialCharacter(String password) {
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
            throw UserException.invalidPassword("A senha deve incluir pelo menos um caractere especial");
        }
    }

    private void avoidSimpleSequences(String password) {
        if (password.matches(".*(123|234|345|456|567|678|789).*")) {
            throw UserException.invalidPassword("A senha não pode incluir sequências simples de caracteres");
        }
    }

    public void deleteUserById(String userId) {
        Optional<User> userOptional = this.userRepository.findById(userId);
        if (userOptional.isPresent()) {
            this.userRepository.delete(userOptional.get());
        } else {
            throw UserException.userNotFound("Usuário não encontrado com o ID: " + userId);
        }
    }

    public byte[] getPhoto(String userId) {
        return this.userRepository.findById(userId).get().getPhoto();
    }

}
