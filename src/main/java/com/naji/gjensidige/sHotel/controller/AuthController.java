package com.naji.gjensidige.sHotel.controller;

import javax.management.relation.RoleNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naji.gjensidige.sHotel.exception.UserAllReadyExistsException;
import com.naji.gjensidige.sHotel.model.User;
import com.naji.gjensidige.sHotel.service.IUserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final IUserService userService;

    @PostMapping("/register-user")
    public ResponseEntity<?> registerUser(User user) throws RoleNotFoundException, UserAllReadyExistsException {
        try {
            userService.registerUser(user);
            return ResponseEntity.ok("Registration Successfull.");

        } catch (RoleNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (UserAllReadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }

    }

}
