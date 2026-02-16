package it.lucianofilippucci.tcgarkhive.helpers.exceptions;

public class CardNotFoundException extends RuntimeException {
    public CardNotFoundException(String message) {
        super(message);
    }
}
