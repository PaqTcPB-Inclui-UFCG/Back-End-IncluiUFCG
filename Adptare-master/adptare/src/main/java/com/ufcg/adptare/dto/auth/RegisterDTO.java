package com.ufcg.adptare.dto.auth;

import com.ufcg.adptare.model.Enum.UserRole;

public record RegisterDTO(String login, String password, UserRole role, String firstName, String lastName) {
}
