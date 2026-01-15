package it.lucianofilippucci.tcgarkhive.helpers;

import java.security.MessageDigest;
import java.util.Base64;

public class ShaHash {
    public static String hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
