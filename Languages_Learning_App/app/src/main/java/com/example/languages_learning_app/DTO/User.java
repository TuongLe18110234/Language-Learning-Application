package com.example.languages_learning_app.DTO;

import android.text.format.DateFormat;

import java.util.Date;

public class User {
    // Required parameters
    private String userId;
    private String role;
    private String createDate;

    // Optional parameters
    private String fullName;
    private String phone;
    private String email;
    private int image;
    private boolean isActive;

    // Getters
    public String getUserId() {
        return userId;
    }

    public String getRole() {
        return role;
    }

    public String getCreateDate() {
        return createDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public int getImage() {
        return image;
    }

    // Private constructors
    private User(){}

    public User(UserBuilder builder) {
        this.userId = builder.userId;
        this.role = builder.role;
        this.isActive = builder.isActive;
        this.createDate = builder.createDate;
        this.fullName = builder.fullName;
        this.email = builder.email;
        this.phone = builder.phone;
        this.image = builder.image;
    }

    // Builder class
    public static class UserBuilder {
        // Required parameters
        private String role;
        private boolean isActive;

        // Optional parameters
        private String fullName;
        private String phone;
        private String email;
        private int image;
        private String userId;
        private String createDate;

        public UserBuilder(String role, boolean isActive) {
            this.role = role;
            this.isActive = isActive;
            this.createDate = DateFormat.format("yyyy-MM-dd hh:mm:ss", new Date()).toString();
        }

        public UserBuilder setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public UserBuilder setFullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public UserBuilder setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public UserBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder setImage(int image) {
            this.image = image;
            return this;
        }

        public UserBuilder setActive(boolean active) {
            isActive = active;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}