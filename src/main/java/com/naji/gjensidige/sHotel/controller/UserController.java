package com.naji.gjensidige.sHotel.controller;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import com.naji.gjensidige.sHotel.model.User;
import org.springframework.http.ResponseEntity;
import javax.management.relation.RoleNotFoundException;
import com.naji.gjensidige.sHotel.service.IUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import com.naji.gjensidige.sHotel.exception.NoUsersAvailableException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.naji.gjensidige.sHotel.exception.UserNotFoundException;
import com.naji.gjensidige.sHotel.exception.UserAllReadyExistsException;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private IUserService userService;

    @GetMapping
    public ResponseEntity<?> getAllUsers() {

        try {
            List<User> allUsers = userService.getUsers();
            return new ResponseEntity<>(allUsers, HttpStatus.OK);

        } catch (NoUsersAvailableException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("add-user")
    public ResponseEntity<?> registerUser(User user) throws RoleNotFoundException, UserAllReadyExistsException {
        try {
            User theUser = userService.registerUser(user);
            return ResponseEntity.ok("Registration Successfull.");

        } catch (RoleNotFoundException e) {
            throw new RoleNotFoundException(e.getMessage());
        } catch (UserAllReadyExistsException e) {
            throw new UserAllReadyExistsException(e.getMessage());
        }

    }

    @GetMapping("/user/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        try {
            Optional<User> optionalUser = userService.getUser(email);
            if (!optionalUser.isPresent()) {
                throw new UserNotFoundException("User Not found");
            }

            User theUser = optionalUser.get();
            return ResponseEntity.ok().body(theUser);
        } catch (Exception e) {
            throw new UserNotFoundException("User Not found with givene email: " + email);
        }
    }

    @DeleteMapping("/user/delete/{email}")
    public ResponseEntity<String> deleteUser(@PathVariable String email) {
        try {
            userService.deleteUser(email);
            return ResponseEntity.ok().body("User have been deleted successfully.");

        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting user");
        }
    }
}
