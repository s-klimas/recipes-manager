package pl.sebastianklimas.recipesmenager.recipes;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pl.sebastianklimas.recipesmenager.recipes.dtos.RecipeRequestDto;
import pl.sebastianklimas.recipesmenager.recipes.dtos.RecipeResponseDto;

@Mapper(componentModel = "spring")
public interface RecipeMapper {
    RecipeResponseDto toDto(Recipe recipe);
    @Mapping(target = "instructions", source = "instructions")
    Recipe toEntity(RecipeRequestDto recipeDto);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "instructions", source = "instructions")
    void update(RecipeRequestDto recipeDto, @MappingTarget Recipe recipe);
}
