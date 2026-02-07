package pl.sebastianklimas.recipesmenager.recipes;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.sebastianklimas.recipesmenager.users.User;

import java.util.Set;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Set<Recipe> findAllByUserId(Long userId);

    Page<Recipe> findByUserOrVisibility(User user, String visibility, Pageable pageable);
}
