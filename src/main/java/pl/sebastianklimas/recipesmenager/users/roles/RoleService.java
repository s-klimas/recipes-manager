package pl.sebastianklimas.recipesmenager.users.roles;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.sebastianklimas.recipesmenager.users.UserRole;

@AllArgsConstructor
@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public UserRole getRole(String roleName) {
        return roleRepository.findByName(roleName);
    }
}
