package com.tripto.booking_app.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserRole {
    private Long userId;
    private Long roleId;
}
