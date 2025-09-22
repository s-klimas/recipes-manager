package pl.sebastianklimas.recipesmenager.users.roles;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sebastianklimas.recipesmenager.users.UserRole;

import java.util.List;

public interface RoleRepository extends JpaRepository<UserRole, Long> {
    UserRole findByName(String name);
}
