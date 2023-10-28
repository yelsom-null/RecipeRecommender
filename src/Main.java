import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Recommender recommender = new Recommender();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter ingredients separated by commas: ");
        String input = scanner.nextLine();
        List<String> availableIngredients = Arrays.asList(input.split(","));

        List<String> recommendations = recommender.recommend(availableIngredients);

        if (recommendations.isEmpty()) {
            System.out.println("No recipes found.");
        } else {
            System.out.println("You can make:");
            for (String recommendation : recommendations) {
                System.out.println("- " + recommendation);
            }
        }
    }
}
