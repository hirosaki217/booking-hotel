package com.tripto.booking_app.mapper;


import com.tripto.booking_app.entity.Role;
import com.tripto.booking_app.entity.UserRole;

import java.util.Optional;

public interface RoleMapper {
    Optional<Role> findByName(String role) throws Exception;
    void saveUserRole(UserRole userRole) throws Exception;
}
