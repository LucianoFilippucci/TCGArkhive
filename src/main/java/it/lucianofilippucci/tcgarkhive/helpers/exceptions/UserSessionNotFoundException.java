package it.lucianofilippucci.tcgarkhive.helpers.exceptions;

public class UserSessionNotFoundException extends RuntimeException {
    public UserSessionNotFoundException(String message) {
        super(message);
    }
}
