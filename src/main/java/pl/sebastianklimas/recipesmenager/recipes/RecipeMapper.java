package pl.sebastianklimas.recipesmenager.recipes;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pl.sebastianklimas.recipesmenager.recipes.dtos.RecipeDto;

@Mapper(componentModel = "spring")
public interface RecipeMapper {
    RecipeDto toDto(Recipe recipe);
    Recipe toEntity(RecipeDto recipeDto);
    @Mapping(target = "id", ignore = true)
    void update(RecipeDto recipeDto, @MappingTarget Recipe recipe);
}
