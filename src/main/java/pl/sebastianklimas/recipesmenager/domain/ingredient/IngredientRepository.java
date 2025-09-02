package pl.sebastianklimas.recipesmenager.domain.ingredient;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
}
