package com.naji.gjensidige.sHotel.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.naji.gjensidige.sHotel.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String role);

    boolean existsByName(String name);

}
