package com.tripto.booking_app.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class Role {

    private Long id;

    private String name;

    private String description;

    private LocalDateTime createdAt;

    private Set<User> users;

    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

}