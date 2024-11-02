package com.naji.gjensidige.sHotel.service;

import java.util.List;
import java.util.Optional;

import javax.management.relation.RoleNotFoundException;

import com.naji.gjensidige.sHotel.model.User;

public interface IUserService {

    User registerUser(User user) throws RoleNotFoundException;

    List<User> getUsers();

    void deleteUser(String email);

    Optional<User> getUser(String email);
}
