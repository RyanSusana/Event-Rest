package com.isa.arnhem.isarest.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class User {


    @JsonProperty("user_id")
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
    private final Date creationDate;

    @JsonProperty("activated")
    private boolean activated;


    public User() {
        this.creationDate = Calendar.getInstance().getTime();
        id = UUID.randomUUID().toString();
    }


    public User(String username, String email, String password, UserType type) {
        this();
        this.username = username;
        this.email = email;
        this.password = password;
        this.type = type;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UserType getType() {
        return type;
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
        }

        return this;
    }
    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    private static String encrypt(final String password) throws NoSuchAlgorithmException {


        final MessageDigest digest = MessageDigest.getInstance("MD5");
        final byte[] passwordBytes = password.getBytes();

        digest.reset();
        digest.update(passwordBytes);
        final byte[] message = digest.digest();

        final StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < message.length; i++) {
            hexString.append(Integer.toHexString(
                    0xFF & message[i]));
        }
        return hexString.toString();

    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

}
