package pl.sebastianklimas.recipesmenager.recipes.ingredients;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
}
