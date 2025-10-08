package pl.sebastianklimas.recipesmenager.users.roles;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.sebastianklimas.recipesmenager.users.UserRole;
import pl.sebastianklimas.recipesmenager.users.exceptions.RoleNotFoundException;

@AllArgsConstructor
@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public UserRole getRole(String roleName) {
        UserRole role = roleRepository.findByName(roleName);
        if (role == null) {
            throw new RoleNotFoundException("Role is null.");
        }
        return role;
    }
}
