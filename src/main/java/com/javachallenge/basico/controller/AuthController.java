package com.javachallenge.basico.controller;

import com.javachallenge.basico.entity.User;
import com.javachallenge.basico.security.jwt.JwtUtils;
import com.javachallenge.basico.security.request.UserRequest;
import com.javachallenge.basico.security.response.MessageResponse;
import com.javachallenge.basico.security.response.JwtResponse;
import com.javachallenge.basico.security.service.UserDetailsImpl;
import com.javachallenge.basico.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private UserService userService;
    @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired private JwtUtils jwtUtils;
    @Autowired AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody UserRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), roles));
    }

    @PostMapping("/registration")
    public ResponseEntity create(@RequestBody UserRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();

        if (userService.existsByUsername(username)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        String encodedPassword = bCryptPasswordEncoder.encode(password);

        User user = new User(username, encodedPassword);
        userService.save(user);
        UserDetailsImpl impl = UserDetailsImpl.build(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(impl);
    }
}