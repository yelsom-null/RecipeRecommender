import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DatabaseManager {
    private static Connection connection;

    static {
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/recipe_recommender";
            String username = "yelsom";
            String password = "";

            connection = DriverManager.getConnection(url, username, password);

            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    clearDatabase();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    // Clear the database
    public static void clearDatabase() {
        try {
            String query = "DELETE FROM recipes";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void populateDatabase() {
        String[][] recipes = {
                {"Spaghetti Bolognese", "Spaghetti,Ground Beef,Tomato Sauce,Onion,Garlic"},
                {"Chicken Alfredo", "Chicken Breast,Fettuccine,Alfredo Sauce"},
                {"Chicken Curry", "Chicken,Curry Paste,Coconut Milk"},
                {"Vegetable Stir-Fry", "Broccoli,Carrot,Bell Pepper,Soy Sauce"},
                {"BBQ Chicken", "Chicken Breast,BBQ Sauce"},
                {"Fish Tacos", "Fish Fillet,Tortilla,Cabbage,Lime"},
                {"Vegan Curry", "Chickpea,Coconut Milk,Curry Paste"},
                {"Beef Stew", "Beef Chuck,Carrot,Potato,Beef Broth"},
                {"Clam Chowder", "Clam,Potato,Celery,Milk"},
                {"Caesar Salad", "Romaine Lettuce,Croutons,Caesar Dressing"},
                {"Hamburger", "Ground Beef,Bun,Lettuce,Tomato"},
                {"Veggie Burger", "Veggie Patty,Bun,Lettuce,Tomato"},
                {"Chicken Parmesan", "Chicken Breast,Parmesan Cheese,Tomato Sauce"},
                {"Mushroom Risotto", "Arborio Rice,Mushroom,Chicken Broth"},
                {"Tomato Soup", "Tomato,Onion,Chicken Broth"}
        };

        try {
            for (String[] recipe : recipes) {
                String query = "INSERT INTO recipes (recipe_name, ingredient) VALUES (?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);

                preparedStatement.setString(1, recipe[0]);

                Array ingredientsArray = connection.createArrayOf("text", recipe[1].split(","));
                preparedStatement.setArray(2, ingredientsArray);

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getRecipes(List<String> ingredients) {
        List<String> matchingRecipes = new ArrayList<>();
        try {
            String query = "SELECT DISTINCT recipe_name FROM recipes, unnest(ingredient) as u_ingredient WHERE LOWER(u_ingredient) = ANY(?);";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            for (int i = 0; i < ingredients.size(); i++) {
                ingredients.set(i, ingredients.get(i).toLowerCase());
            }

            Array ingredientsArray = connection.createArrayOf("text", ingredients.toArray());
            preparedStatement.setArray(1, ingredientsArray);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                matchingRecipes.add(resultSet.getString("recipe_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Error Code: " + e.getErrorCode());
            System.out.println("Message: " + e.getMessage());
        }

        return matchingRecipes;
    }



    public List<Recipe> getAllRecipes() {
        List<Recipe> allRecipes = new ArrayList<>();
        try {
            String query = "SELECT * FROM recipes";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString("recipe_name");

                Object[] objectArray = (Object[]) resultSet.getObject("ingredient");
                List<String> ingredientsList = Arrays.stream(objectArray)
                        .map(object -> Objects.toString(object, null))
                        .collect(Collectors.toList());

                allRecipes.add(new Recipe(name, ingredientsList));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Error Code: " + e.getErrorCode());
            System.out.println("Message: " + e.getMessage());
        }
        return allRecipes;
    }





}
