package seedu.duke;

import seedu.duke.command.AddExerciseCommand;
import seedu.duke.command.AddNewRepeatedSet;
import seedu.duke.command.ByeCommand;
import seedu.duke.command.Command;
import seedu.duke.command.AddFoodCommand;
import seedu.duke.command.CreateNewUserCommand;
import seedu.duke.command.EditFoodCommand;
import seedu.duke.command.EditExerciseCommand;
import seedu.duke.command.FindCalorieCommand;
import seedu.duke.command.FindDescriptionCommand;
import seedu.duke.command.HelpCommand;
import seedu.duke.command.DeleteCommand;
import seedu.duke.command.MoveActivityCommand;
import seedu.duke.command.InvalidCommand;
import seedu.duke.command.ListCommand;
import seedu.duke.command.GraphCommand;
import seedu.duke.userprofile.Initialiseuser;
import seedu.duke.userprofile.Userinfo;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.time.format.DateTimeParseException;

import static seedu.duke.Duke.calList;
import static seedu.duke.Duke.executeCmd;
import static seedu.duke.Duke.storage;
import static seedu.duke.ExceptionMessages.displayAddActivityNumberFormatExceptionMessage;
import static seedu.duke.ExceptionMessages.displayAddCommandErrorMessage;
import static seedu.duke.ExceptionMessages.displayDeleteCommandNullPointerExceptionMessage;
import static seedu.duke.ExceptionMessages.displayDeleteCommandNumberFormatExceptionMessage;
import static seedu.duke.ExceptionMessages.displayEmptyAddActivityErrorMessage;
import static seedu.duke.ExceptionMessages.displayEmptyEditActivityErrorMessage;
import static seedu.duke.ExceptionMessages.displayFindErrorMessage;
import static seedu.duke.ExceptionMessages.displayIoExceptionMessage;
import static seedu.duke.ExceptionMessages.displayStringIndexOutOfBoundsExceptionMessage;
import static seedu.duke.ExceptionMessages.displayIncorrectDateTimeFormatEnteredMessage;

/**
 * Initialises parser class.
 */
public class Parser {
    protected String userInput;
    protected LocalDateTime date;

    /**
     * Store details in the class.
     *
     * @param userInput user from the user.
     */
    public Parser(String userInput) {
        //Trims and removes additional spaces between words
        this.userInput = userInput.trim().replaceAll(" +", " ");
        this.date = LocalDateTime.now();
    }

    /**
     * Parses commands.
     *
     * @return Command type
     */
    public Command parseCommand() {

        String[] arguments = userInput.split(" ", 2);
        try {
            switch (arguments[0].toLowerCase()) {
            case "create":
                return new CreateNewUserCommand();
            case "createset":
                return new AddNewRepeatedSet(arguments[1]);
            case "add":
                return prepareAddCommand(userInput);
            case "find":
                return prepareFindCommand(userInput);
            case "edit":
                Userinfo store = new Userinfo();
                store.editUserInfo(arguments[1]);
                Initialiseuser.save(store);
                break;
            case "edita":
                return prepareEditActivityCommand(arguments[1]);
            case "delete":
                return prepareDeleteCommand(arguments[1]);
            case "list":
                return prepareListCommand(userInput);
            case "help":
                return new HelpCommand();
            case "move":
                return prepareMoveIndexCommand(userInput);
            case "bye":
                return new ByeCommand();
            case "graph":
                return new GraphCommand();
            default:
                return new InvalidCommand();
            }
        } catch (StringIndexOutOfBoundsException e) {
            displayStringIndexOutOfBoundsExceptionMessage();
        } catch (IOException e) {
            displayIoExceptionMessage();
        }
        return null;
    }

    public Command prepareChaining(String userInput) {
        while (userInput.contains("&&")) {
            userInput = userInput + " &&";
            int chainIndex = userInput.indexOf("&&");
            if (chainIndex < 5) {
                break;
            }
            String firstString = userInput.substring(0, chainIndex).trim();

            Command cmd = processCommand(firstString);
            executeCmd(cmd);
            storage.updateFile(calList);

            userInput = userInput.substring(chainIndex + 2).trim();
        }
        return null;
    }

    private Command processCommand(String userInput) {
        String[] arguments = userInput.split(" ", 2);
        try {
            switch (arguments[0].toLowerCase()) {
            case "create":
                return new CreateNewUserCommand();
            case "add":
                return prepareAddCommand(userInput);
            case "find":
                return prepareFindCommand(userInput);
            case "edit":
                Userinfo store = new Userinfo();
                store.editUserInfo(arguments[1]);
                Initialiseuser.save(store);
                break;
            case "edita":
                return prepareEditActivityCommand(arguments[1]);
            case "delete":
                return prepareDeleteCommand(arguments[1]);
            case "list":
                return prepareListCommand(userInput);
            case "help":
                return new HelpCommand();
            case "move":
                return prepareMoveIndexCommand(userInput);
            case "bye":
                return new ByeCommand();
            default:
                return new InvalidCommand();
            }
        } catch (StringIndexOutOfBoundsException e) {
            displayStringIndexOutOfBoundsExceptionMessage();
        } catch (IOException e) {
            displayIoExceptionMessage();
        }
        return null;
    }


    /**
     * Prepares the edit command by checking the userInput.
     *
     * @param userInput description of the edit command.
     * @return EditExerciseCommand
     */
    private Command prepareEditActivityCommand(String userInput) {
        String[] arguments = userInput.split(" ", 2);
        int index = Integer.parseInt(arguments[0]) - 1;
        userInput = arguments[1];
        try {
            if (userInput.startsWith("f/")) {
                int calorieIndex = userInput.indexOf("c/");
                int dateIndex = userInput.indexOf("d/");
                int calories = Integer.parseInt(userInput.substring(calorieIndex + 2, dateIndex).trim());
                LocalDate date = processDate(userInput.substring(dateIndex + 2).trim());

                String foodDescription = userInput.substring(2, calorieIndex - 1).trim();

                new DeleteCommand(index);
                return new EditFoodCommand(index, foodDescription, calories, false, date);
            } else if (userInput.startsWith("e/")) {
                int calorieIndex = userInput.indexOf("c/");
                int dateIndex = userInput.indexOf("d/");
                int calories = Integer.parseInt(userInput.substring(calorieIndex + 2, dateIndex).trim());
                LocalDate date = processDate(userInput.substring(dateIndex + 2).trim());

                String exerciseDescription = userInput.substring(2, calorieIndex - 1).trim();

                new DeleteCommand(index);
                return new EditExerciseCommand(index, exerciseDescription, calories, false, date);
            } else {
                displayEmptyEditActivityErrorMessage();
            }
        } catch (NullPointerException | StringIndexOutOfBoundsException e) {
            displayAddCommandErrorMessage();
        } catch (NumberFormatException e) {
            displayAddActivityNumberFormatExceptionMessage();
        }
        return null;
    }

    /**
     * Prepares the add command by checking the userInput.
     *
     * @param userInput description of the add command.
     * @return AddExerciseCommand
     */
    private Command prepareAddCommand(String userInput) {
        try {
            String[] arguments = userInput.split(" ", 2);
            if (arguments[1].startsWith("f/")) {
                int calorieIndex = arguments[1].indexOf("c/");
                int dateIndex = arguments[1].indexOf("d/");
                int calories = Integer.parseInt(arguments[1].substring(calorieIndex + 2, dateIndex).trim());
                LocalDate date = processDate(arguments[1].substring(dateIndex + 2).trim());

                String foodDescription = arguments[1].substring(2, calorieIndex - 1).trim();

                assert calories > 0 : "calories should be greater than 0";
                return new AddFoodCommand(foodDescription, calories, false, date);
            } else if (arguments[1].startsWith("e/")) {
                int calorieIndex = arguments[1].indexOf("c/");
                int dateIndex = arguments[1].indexOf("d/");
                int calories = Integer.parseInt(arguments[1].substring(calorieIndex + 2, dateIndex).trim());
                LocalDate date = processDate(arguments[1].substring(dateIndex + 2).trim());

                String exerciseDescription = arguments[1].substring(2, calorieIndex - 1).trim();

                assert calories > 0 : "calories should be greater than 0";
                return new AddExerciseCommand(exerciseDescription, calories, false, date);
            } else {
                displayEmptyAddActivityErrorMessage();
            }
        } catch (NullPointerException | StringIndexOutOfBoundsException e) {
            displayAddCommandErrorMessage();
        } catch (NumberFormatException e) {
            displayAddActivityNumberFormatExceptionMessage();
        }
        return null;
    }

    private Command prepareMoveIndexCommand(String userInput) throws IndexOutOfBoundsException {
        //Removing additional spaces in the user's input
        String after = userInput.trim().replaceAll(" +", " ");
        String firstIndexKey = "from/";
        String secondIndexKey = "below/";

        int firstIndex = after.indexOf(firstIndexKey) + firstIndexKey.length(); //index after first keyword
        int secondIndex = after.indexOf(secondIndexKey) + secondIndexKey.length(); //index after second keyword

        String firstIndexString = after.substring(firstIndex).trim().split(" ")[0];
        String secondIndexString = after.substring(secondIndex).trim().split(" ")[0];
        int indexToBeChanged = Integer.parseInt(firstIndexString);
        int indexToBeInsertedBelow = Integer.parseInt(secondIndexString);
        return new MoveActivityCommand(indexToBeChanged, indexToBeInsertedBelow);
    }

    /**
     * Process date input by user.
     *
     * @param dateInput date input by user.
     * @return date
     */
    private LocalDate processDate(String dateInput) {
        try {
            LocalDate date = LocalDate.parse(dateInput);

            return date;
        } catch (DateTimeException e) {
            //displayDateTimeExceptionMessage();
            return currentDate();
        }
    }

    /**
     * Returns current date.
     *
     * @return current date
     */
    private LocalDate currentDate() {
        LocalDate date = LocalDate.now();

        return date;
    }

    /**
     * Prepares the list command by checking the userInput.
     *
     * @param userInput description of the list command.
     * @return ListCommand
     */
    private Command prepareListCommand(String userInput) {

        if (userInput.toLowerCase().equals("list")) {
            return new ListCommand();
        } else {
            String dateString = userInput.split(" ")[1];
            try {
                LocalDate date = checkDate(dateString);
                return new ListCommand(date);
            } catch (DateTimeParseException e) {
                displayIncorrectDateTimeFormatEnteredMessage();
                return null;
            }
        }
    }

    /**
     * Prepares the delete command by checking the userInput.
     *
     * @param userInput description of the delete command.
     * @return DeleteCommand
     */
    private Command prepareDeleteCommand(String userInput) {
        try {
            if (userInput.equals("/all")) {
                return new DeleteCommand();
            } else {
                int index = Integer.parseInt(userInput) - 1;
                return new DeleteCommand(index);
            }
        } catch (NumberFormatException e) {
            displayDeleteCommandNumberFormatExceptionMessage();
        } catch (NullPointerException e) {
            displayDeleteCommandNullPointerExceptionMessage();
        }
        return null;
    }

    /**
     * Prepares the find command by checking the userInput.
     * If the keyword contains activity description, returns FindDescriptionCommand.
     * Else if the keyword contains calories count, returns FindCalorieCommand.
     *
     * @param userInput description of the find command.
     * @return FindCalorieCommand
     */
    private Command prepareFindCommand(String userInput) {
        try {
            String[] arguments = userInput.split(" ", 2);
            if (arguments[1].startsWith("d/")) {
                String description = arguments[1].substring(2).trim();
                return new FindDescriptionCommand(description);
            } else if (arguments[1].startsWith("c/")) {
                String calorie = arguments[1].substring(2).trim();
                return new FindCalorieCommand(calorie);
            } else {
                displayFindErrorMessage();
            }
        } catch (NullPointerException | StringIndexOutOfBoundsException e) {
            displayFindErrorMessage();
        }
        return null;
    }

    private LocalDate checkDate(String dateTimeString) throws DateTimeParseException {
        LocalDate dateTime = LocalDate.parse(dateTimeString);
        return dateTime;
    }

}
