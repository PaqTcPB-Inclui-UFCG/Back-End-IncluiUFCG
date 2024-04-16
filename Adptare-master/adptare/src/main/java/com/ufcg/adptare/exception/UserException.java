package com.ufcg.adptare.exception;

public class UserException extends RuntimeException {

    public UserException(String message) {
        super(message);
    }

    public static UserException invalidEmail(String message) {
        return new UserException(message);
    }

    public static UserException userNotFound(String message) {
        return new UserException(message);
    }

    public static UserException duplicateEmail(String message) {
        return new UserException(message);
    }

    public static UserException passwordValidation(String message) {
        return new UserException("A senha não satisfaz os requisitos mínimos");
    }

    public static UserException incorrectPassword(String message) {
        return new UserException("Usuário ou senha incorreta");
    }

    public static UserException invalidPassword(String message) {
        return new UserException(message);
    }
}
