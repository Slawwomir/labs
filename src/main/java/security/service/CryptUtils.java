package security.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CryptUtils {

    /**
     * Wyznacza skrót podanego hasło z wykorzystaniem algorytmu SHA256.
     *
     * @param password hasło, którego hash ma zostać wyznaczony
     * @return skrót hasła, obliczony przy użyciu funkcji SHA256
     */
    public static String sha256(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes(StandardCharsets.UTF_8));
            return String.format("%064x", new java.math.BigInteger(1, md.digest()));
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
    }
}
