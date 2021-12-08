package org.library.builder;

import org.library.model.*;

import javax.swing.*;
import java.util.List;
import java.util.Set;

public class UserBuilder {
    private User user;
    private UserBuilder userBuilder;
    {
        user = new User();
    }

    private UserBuilder() {}

    public static synchronized UserBuilder init() {
        UserBuilder userBuilder = new UserBuilder();
        userBuilder.setUserBuilder(userBuilder);
        return userBuilder;
    }
    public UserBuilder setId(Long id) {
        user.setId(id);
        return this.userBuilder;
    }

    public UserBuilder setFirstName(String firstName) {
        user.setFirstName(firstName);
        return this.userBuilder;
    }

    public UserBuilder setLastName(String lastName) {
        user.setLastName(lastName);
        return this.userBuilder;
    }
    public UserBuilder setUsername(String username) {
        user.setUsername(username);
        return this.userBuilder;
    }
    public UserBuilder setPassword(String password) {
        user.setPassword(password);
        return this.userBuilder;
    }
    public UserBuilder setEmail(String email) {
        user.setEmail(email);
        return this.userBuilder;
    }

    public UserBuilder setEnabled(Boolean enabled) {
        user.setEnabled(enabled);
        return this.userBuilder;
    }

    public UserBuilder setRoles(Set<Role> roles) {
        user.setRoles(roles);
        return this.userBuilder;
    }

    public UserBuilder setBooks(List<Book> books) {
        user.setBooks(books);
        return this.userBuilder;
    }

    public UserBuilder setJournals(List<Journal> journals) {
        user.setJournals(journals);
        return this.userBuilder;
    }
    public UserBuilder setNespaper(List<Newspaper> newspapers) {
        user.setNewspapers(newspapers);
        return this.userBuilder;
    }

    public User build() {
        return user;
    }

    public void setUserBuilder(UserBuilder userBuilder) {
        this.userBuilder = userBuilder;
    }
}
