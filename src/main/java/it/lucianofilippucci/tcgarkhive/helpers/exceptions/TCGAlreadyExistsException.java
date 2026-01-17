package it.lucianofilippucci.tcgarkhive.helpers.exceptions;

public class TCGAlreadyExistsException extends RuntimeException {
    public TCGAlreadyExistsException(String message) {
        super(message);
    }
}
