package com.universite.backend.utils;

import java.security.SecureRandom;
import java.text.Normalizer;
import java.util.Locale;

public class PasswordAndEmailGenerator {

    private static final SecureRandom random = new SecureRandom();
    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LETTERS_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "0123456789";

    public static String generatePassword() {
        StringBuilder sb = new StringBuilder();
        // Première lettre en Majuscule
        sb.append(LETTERS.charAt(random.nextInt(LETTERS.length())));
        // 4 lettres minuscules
        for (int i = 0; i < 4; i++) {
            sb.append(LETTERS_LOWER.charAt(random.nextInt(LETTERS_LOWER.length())));
        }
        sb.append("@");
        // 4 chiffres
        for (int i = 0; i < 4; i++) {
            sb.append(NUMBERS.charAt(random.nextInt(NUMBERS.length())));
        }
        return sb.toString();
    }

    public static String generateEmail(String nom, String prenom) {
        // Supprime les accents et met tout en minuscule
        String email = (normalize(nom) + "." + normalize(prenom)).toLowerCase(Locale.ROOT);
        return email + "@unchk.edu.sn";
    }

    private static String normalize(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .replaceAll("[^a-zA-Z]", "");
    }

    public static String generateINE(int anneeBac) {
        int randomPart = 10000 + random.nextInt(90000);
        return "N0" + randomPart + anneeBac;
    }
}
