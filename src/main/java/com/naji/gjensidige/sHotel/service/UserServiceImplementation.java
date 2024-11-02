package com.naji.gjensidige.sHotel.service;

import java.util.List;
import java.util.Optional;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import jakarta.transaction.Transactional;
import com.naji.gjensidige.sHotel.model.Role;
import com.naji.gjensidige.sHotel.model.User;
import org.springframework.stereotype.Service;
import com.naji.gjensidige.sHotel.repository.RoleRepository;
import com.naji.gjensidige.sHotel.repository.UserRepository;
import com.naji.gjensidige.sHotel.exception.UserNotFoundException;
import com.naji.gjensidige.sHotel.exception.RoleNotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder; // Import this
import com.naji.gjensidige.sHotel.exception.NoUsersAvailableException;
import com.naji.gjensidige.sHotel.exception.UserAllReadyExistsException;

@Service
@RequiredArgsConstructor
public class UserServiceImplementation implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    // Register User
    @Transactional
    @Override
    public User registerUser(User user) throws RoleNotFoundException {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAllReadyExistsException(user.getEmail() + "Already exixts.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Optional<Role> optionalUserRole = roleRepository.findByName("ROLE_USER");

        if (optionalUserRole.isPresent()) {
            Role userRole = optionalUserRole.get();

            user.setRoles((Collections.singletonList(userRole)));
            return userRepository.save(user);
        } else {
            throw new RoleNotFoundException("ROLE_USER not found.");
        }
    }

    // Get ALl users
    @Override
    public List<User> getUsers() {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            throw new NoUsersAvailableException("No users available.");
        }
    }

    @Transactional
    @Override
    public void deleteUser(String email) {

        User theUser = userRepository.getUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User Not found with givene email: " + email));

        userRepository.deleteByEmail(email);

    }

    @Override
    public Optional<User> getUser(String email) {

        try {

            // User optionalUser= userRepository.getUserBYEmail(email).get();

            Optional<User> optionalUser = userRepository.getUserByEmail(email);
            if (!optionalUser.isPresent()) {
                throw new UserNotFoundException("User Not found");
            }
            return Optional.of(optionalUser.get());
        } catch (Exception e) {
            throw new UserNotFoundException("User Not found with givene email: " + email);
        }
    }
}
