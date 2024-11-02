package com.naji.gjensidige.sHotel.service;

import java.util.List;
import com.naji.gjensidige.sHotel.model.User;
import com.naji.gjensidige.sHotel.model.Role;

public interface IRoleService {

    List<Role> getAllRoles();

    Role createRole(Role role);

    void deleteRole(Long id);

    Role findRoleByName(String name);

    User removeUserFromRole(Long userId, Long roleId);

    User assignRoleToUser(Long userId, Long roleId);

    Role removeAllUsersFromRole(Long roleId);

}
