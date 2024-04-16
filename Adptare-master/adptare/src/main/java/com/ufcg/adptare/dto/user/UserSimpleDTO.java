package com.ufcg.adptare.dto.user;

import com.ufcg.adptare.model.Enum.UserRole;

public record UserSimpleDTO(String fullName, String login, UserRole role, String id) {
}
