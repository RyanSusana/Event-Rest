package com.isa.arnhem.isarest.models;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class IsaPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence charSequence) {
        return encrypt(charSequence.toString());
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return encrypt(charSequence.toString()).equals(s);
    }


    private static String encrypt(final String password)  {

        final MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");

        final byte[] passwordBytes = password.getBytes();

        digest.reset();
        digest.update(passwordBytes);
        final byte[] message = digest.digest();

        final StringBuilder hexString = new StringBuilder();
        for (byte aMessage : message) {
            hexString.append(Integer.toHexString(
                    0xFF & aMessage));
        }
        return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
