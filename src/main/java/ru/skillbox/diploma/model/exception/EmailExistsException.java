package ru.skillbox.diploma.model.exception;

public class EmailExistsException extends Throwable {
    public EmailExistsException(String email) {
        super("There is an account with that email address: " + email);
    }
}
