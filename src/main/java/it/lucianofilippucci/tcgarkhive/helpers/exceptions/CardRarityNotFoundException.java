package it.lucianofilippucci.tcgarkhive.helpers.exceptions;

public class CardRarityNotFoundException extends RuntimeException {
    public CardRarityNotFoundException(String message) {
        super(message);
    }
}
