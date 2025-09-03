package pl.sebastianklimas.recipesmenager.domain.recipeOld;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.sebastianklimas.recipesmenager.domain.ingredient.Ingredient;
import pl.sebastianklimas.recipesmenager.domain.user.User;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "recipes")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "MANUAL", columnDefinition = "text")
    private String manual;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "recipe")
    private Set<Ingredient> ingredients = new HashSet<>();
}


