package pl.sebastianklimas.recipesmenager.domain.recipe;

import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {
    Set<Recipe> findAllByUserId(Long userId);
}
