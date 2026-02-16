package it.lucianofilippucci.tcgarkhive.helpers.exceptions;

public class RoleNotExistsException extends RuntimeException {
    public RoleNotExistsException(String message) {
        super(message);
    }
}
