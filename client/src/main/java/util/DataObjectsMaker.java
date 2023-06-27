package util;

import data.Coordinates;
import data.Dragon;
import data.DragonCave;
import data.DragonCharacter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Asks and receives user input data to make a Dragon/username&password object.
 */
public class DataObjectsMaker {
    private static final int MAX_STRING_LENGTH = 100;
    private static final String ERROR_MESSAGE = "Your enter was not correct type. Try again";
    private final OutputManager outputManager;
    private final Asker asker;
    private final String authorName;

    public DataObjectsMaker(InputManager inputManager, OutputManager outputManager, String authorName) {
        this.outputManager = outputManager;
        this.asker = new Asker(inputManager, outputManager);
        this.authorName = authorName;
    }

    public String[] makeLoginAndPassword() throws IOException {
        String login = asker.ask(arg -> arg.length() > 0, "Enter login", ERROR_MESSAGE, "Login must not be empty", x -> x, false);
        String password = asker.ask(arg -> arg.length() > 0, "Enter password", ERROR_MESSAGE, "Password must not be empty", x -> x, false);

        return new String[] {login, password};
    }

    public Dragon makeDragon() throws IOException {
        outputManager.println("Enter dragon data");
        String name = asker.ask(arg -> (arg).length() > 0 , "Enter name (String)",
                ERROR_MESSAGE, "The string must not be empty ", x -> x, false);

        Integer wingspan = asker.ask(arg -> (arg) > 0 && (arg).toString().length() > 0, "Enter wingspan",
                ERROR_MESSAGE, "Your int must be >0. The string must not be empty", Integer::parseInt, false); // >0

        DragonCharacter dragon_character = asker.ask(arg -> true, "Enter Dragon character (EVIL, GOOD, CHAOTIC, CHAOTIC_EVIL, CUNNING)",
                ERROR_MESSAGE, ERROR_MESSAGE, DragonCharacter::valueOf, false); //not null
        Long dragons_age = asker.ask(arg -> (arg) > 0 && (arg).toString().length() > 0, "Enter dragon age",
                ERROR_MESSAGE, "Your long must be >0. The string must not be empty", Long::parseLong, false); // >0
        Double weight = asker.ask(arg -> (arg) > 0 && (arg).toString().length() > 0, "Enter dragon weight",
                ERROR_MESSAGE, "Your Double must be >0. The string must not be empty", Double::parseDouble, false); // >0

        Coordinates coordinates = askForCoordinates(); //not null
        DragonCave cave = askForDragonCave(); //not null
        return new Dragon(name, coordinates, dragons_age, dragon_character, cave, wingspan,
                weight,  authorName);
    }

    private Coordinates askForCoordinates() throws IOException {
        outputManager.println("Enter coordinates data");
        final long xLimitation = -896;
        final double yLimitation = -513;
        Double x = asker.ask(arg -> (arg) >= xLimitation && Double.isFinite(arg), "Enter the x value (Double)", ERROR_MESSAGE,   //<=- 896
                "The double must be <= -896. Try again", Double::parseDouble, false);
        Double y = asker.ask(arg -> (arg) >= yLimitation && Double.isFinite(arg), "Enter y (Double)",
                ERROR_MESSAGE, "The double must be <= -513. Try again", Double::parseDouble, false); //<=- 513, not null
        return new Coordinates(x, y);
    }

    private DragonCave askForDragonCave() throws IOException {
        outputManager.println("Enter info about dragon cave");

        Double depth = asker.ask(arg -> (arg).toString().length() > 0, "Enter depth of cave ",
                ERROR_MESSAGE, "The depth must not be empty. Try again", Double::parseDouble, false);

        float numberOfTreasures = asker.ask(arg -> (arg).toString().length() > 0, "Enter number of treasures ",
                ERROR_MESSAGE, "The depth must not be empty. Try again", Float::parseFloat, false);



        return new DragonCave(depth, numberOfTreasures);
    }

    public static class Asker {

        private final InputManager inputManager;
        private final OutputManager outputManager;


        public Asker(InputManager inputManager, OutputManager outputManager) {
            this.inputManager = inputManager;
            this.outputManager = outputManager;
        }

        public <T> T ask(Predicate<T> predicate,
                         String askMessage,
                         String errorMessage,
                         String wrongValueMessage,
                         Function<String, T> converter,
                         boolean nullable) throws IOException {
            outputManager.println(askMessage);
            String input;
            T value;
            do {
                try {
                    input = inputManager.nextLine();
                    if ("".equals(input) && nullable) {
                        return null;
                    }

                    value = converter.apply(input);

                } catch (IllegalArgumentException e) {
                    outputManager.println(errorMessage);
                    continue;
                }
                if (predicate.test(value)) {
                    return value;
                } else {
                    outputManager.println(wrongValueMessage);
                }
            } while (true);
        }
    }
}