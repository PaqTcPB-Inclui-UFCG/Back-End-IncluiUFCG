package com.ufcg.adptare.service;
import com.ufcg.adptare.exception.UserException;
import com.ufcg.adptare.model.Enum.UserRole;
import com.ufcg.adptare.model.User;
import com.ufcg.adptare.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByLogin(username);
    }

    public String register(String login, String password, String firstName, String lastName, UserRole role) {
        if (userRepository.findByLogin(login) != null) {
            throw new RuntimeException("Usuário já registrado");
        }
        if(!this.userService.isValidEmail(login)){
            throw UserException.invalidEmail("Email inválido.");
        }
        this.userService.validatePassword(password);

        String encryptedPassword = new BCryptPasswordEncoder().encode(password);
        User newUser = new User(login, encryptedPassword, role, firstName, lastName);
        userRepository.save(newUser);

        return "Registro bem-sucedido";
    }



}
