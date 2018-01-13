package com.isa.arnhem.isarest.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class User implements Identifiable{

    private String id;

    @JsonProperty("username")
    private String username;
    @JsonProperty("email")
    private String email;
    @JsonProperty("password")
    private String password;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("type")
    private UserType type;

    @JsonProperty("creation_date")
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm'Z'")
    private Date creationDate;

    @JsonProperty("activated")
    private boolean activated;


    public User() {
        this.creationDate = Calendar.getInstance().getTime();
        this.id = UUID.randomUUID().toString();
    }


    public User(String username, String email, String password, UserType type) {
        this();
        this.username = username;
        this.email = email;
        this.password = password;
        this.type = type;
    }



    private String getSaltedPassword() {
        return this.id + password + this.password + this.id;
    }

    public boolean samePassword(String id, String password) {
        try {
            return (encrypt(id + password + password + id).equals(this.getPassword()));
        } catch (NoSuchAlgorithmException e) {
            return false;
        }
    }

    public User secure() {
        try {
            password = encrypt(getSaltedPassword());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    private static String encrypt(final String password) throws NoSuchAlgorithmException {

        final MessageDigest digest = MessageDigest.getInstance("MD5");
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
    }
}
