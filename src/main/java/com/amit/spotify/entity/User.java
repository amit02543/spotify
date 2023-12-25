package com.amit.spotify.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "pronoun")
    private String pronoun;

    @Column(name = "name")
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "profile_url")
    private String profileUrl;

    public User() { }

    public User(long id, String username, String password, String pronoun, String name, String email, String profileUrl) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.pronoun = pronoun;
        this.name = name;
        this.email = email;
        this.profileUrl = profileUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPronoun() {
        return pronoun;
    }

    public void setPronoun(String pronoun) {
        this.pronoun = pronoun;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(pronoun, user.pronoun) && Objects.equals(name, user.name) && Objects.equals(email, user.email) && Objects.equals(profileUrl, user.profileUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, pronoun, name, email, profileUrl);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", pronoun='" + pronoun + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", profileUrl='" + profileUrl + '\'' +
                '}';
    }

}
