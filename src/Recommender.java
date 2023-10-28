import java.util.ArrayList;
import java.util.List;

public class Recommender {
    private List<Recipe> recipes;

    public Recommender() {
        this.recipes = new ArrayList<>();

        recipes.add(new Recipe("Pasta", List.of("noodles", "tomato sauce")));
        recipes.add(new Recipe("Salad", List.of("lettuce", "tomato", "cucumber")));
        recipes.add(new Recipe("Fruit Salad", List.of("apple", "banana", "orange")));
    }

    public List<String> recommend(List<String> availableIngredients) {
        List<String> recommendations = new ArrayList<>();

        for (Recipe recipe : recipes) {
            if (availableIngredients.containsAll(recipe.getIngredients())) {
                recommendations.add(recipe.getName());
            }
        }

        return recommendations;
    }
}
