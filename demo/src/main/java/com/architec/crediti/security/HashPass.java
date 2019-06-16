package com.architec.crediti.security;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.commons.codec.binary.Hex;

public class HashPass {
    private HashPass() {
    }

    public static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    public static byte[] hashPassword(final char[] password, final byte[] salt) {

        final int iterations = 10000;
        final int keyLength = 256;
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength);
            SecretKey key = skf.generateSecret(spec);
            return key.getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Grabs the password of the extern and hashes it with the pbkdf2 hash,
     * then the function returns an array with the salted pbkdf2 hash(0) and the salt(1)
     */
    public static Object[] convertToPbkdf2(char[] password) {
        Object[] passAndSalt = new Object[2];

        byte[] saltBytes = generateSalt();
        byte[] hashedBytes = hashPassword(password, saltBytes);

        passAndSalt[0] = Hex.encodeHexString(hashedBytes);
        passAndSalt[1] = saltBytes;

        return passAndSalt;
    }

    /**
     * This method is used to check if an extern's password is equal to the one in the database
     */
    public static String convertToPbkdf2Salt(char[] password, byte[] salt) {
        byte[] saltBytes = salt;
        byte[] hashedBytes = hashPassword(password, saltBytes);

        return Hex.encodeHexString(hashedBytes);
    }

}
