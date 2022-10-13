package com.javachallenge.basico.controller;

import com.javachallenge.basico.entity.User;
import com.javachallenge.basico.security.request.UserRequest;
import com.javachallenge.basico.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired private UserService userService;

    @PutMapping("/upgrade-permissions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity upgradePermissions(@RequestBody UserRequest request) {
        User user = userService.findByUsername(request.getUsername());
        user.addRole(User.Role.ROLE_ADMIN);
        userService.save(user);
        return ResponseEntity.noContent().build();
    }
}
