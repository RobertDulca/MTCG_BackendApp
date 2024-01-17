package fhtechnikum.robert.application.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    @JsonProperty("Username")
    private String username;
    @JsonProperty("Password")
    private String password;

    public User() {}
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
