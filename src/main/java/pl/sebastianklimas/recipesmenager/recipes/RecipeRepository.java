package pl.sebastianklimas.recipesmenager.recipes;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Set<Recipe> findAllByUserId(Long userId);
}
