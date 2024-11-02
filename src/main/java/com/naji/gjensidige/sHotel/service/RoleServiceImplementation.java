package com.naji.gjensidige.sHotel.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import com.naji.gjensidige.sHotel.model.Role;
import com.naji.gjensidige.sHotel.model.User;
import org.springframework.stereotype.Service;
import com.naji.gjensidige.sHotel.repository.RoleRepository;
import com.naji.gjensidige.sHotel.repository.UserRepository;
import com.naji.gjensidige.sHotel.exception.RoleNotFoundException;
import com.naji.gjensidige.sHotel.exception.UserNotFoundException;
import com.naji.gjensidige.sHotel.exception.RoleAlreadyExistsException;

@Service
@RequiredArgsConstructor
public class RoleServiceImplementation implements IRoleService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role createRole(Role role) {
        String roleName = "ROLE_" + role.getname().toUpperCase();

        Role theRole = new Role(roleName);

        if (roleRepository.existsByName(theRole.getname())) {
            throw new RoleAlreadyExistsException(theRole.getname() + "Role already existis.");
        }
        return roleRepository.save(role);
    }

    @Override
    public void deleteRole(Long roleId) {
        this.removeAllUsersFromRole(roleId);
        roleRepository.deleteById(roleId);

    }

    @Override
    public Role findRoleByName(String name) {
        Optional<Role> optionalRole = roleRepository.findByName(name);

        if (optionalRole.isPresent()) {
            Role thRole = optionalRole.get();

            return thRole;
        } else {
            throw new RoleNotFoundException(name + "Not found");
        }
    }

    @Override
    public User removeUserFromRole(Long userId, Long roleId) {

        try {
            Optional<User> optionalUser = userRepository.findById(userId);

            if (optionalUser.isPresent()) {
                User theUser = optionalUser.get();

                return theUser;
            } else {
                throw new UserNotFoundException("User with given id Not found: " + userId);
            }
        } catch (Exception e) {
            throw new UserNotFoundException("User with given id Not found: " + userId);

        }
    }

    @Override
    public User assignRoleToUser(Long userId, Long roleId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'assignRoleToUser'");
    }

    @Override
    public Role removeAllUsersFromRole(Long roleId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeAllUsersFromRole'");
    }

}
