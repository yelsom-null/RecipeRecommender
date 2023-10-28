import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class IngredientRecommender {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.populateDatabase();

        while (true) {
            System.out.println("Please enter your current ingredients separated by commas (or type 'q' to quit):");
            String input = scanner.nextLine();

            if ("q".equalsIgnoreCase(input.trim())) {
                break;
            }

            List<String> userIngredients = new ArrayList<>(Arrays.asList(input.split(",")));

            for (int i = 0; i < userIngredients.size(); i++) {
                userIngredients.set(i, userIngredients.get(i).trim());
            }

            List<String> recipes = databaseManager.getRecipes(userIngredients);

            if (recipes.isEmpty()) {
                System.out.println("Sorry, we couldn't find any recipes that match your ingredients.");
            } else {
                System.out.println("You can cook the following recipes:");
                for (String recipe : recipes) {
                    System.out.println("- " + recipe);
                }
            }
        }
    }
}
