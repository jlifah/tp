package seedu.duke;

import seedu.duke.command.AddExerciseCommand;
import seedu.duke.command.AddFoodCommand;
import seedu.duke.command.Command;
import seedu.duke.command.DeleteCommand;
import seedu.duke.command.ListCommand;
import seedu.duke.userprofile.Initialiseuser;
import seedu.duke.userprofile.Userinfo;

import java.io.IOException;
import java.time.LocalDateTime;

import static seedu.duke.Ui.displayAddCommandErrorMessage;
import static seedu.duke.Ui.displayByeMessage;
import static seedu.duke.Ui.displayDefaultMessage;
import static seedu.duke.Ui.displayIOExceptionMessage;
import static seedu.duke.Ui.displayDeleteCommandNullPointerExceptionMessage;
import static seedu.duke.Ui.displayDeleteCommandNumberFormatExceptionMessage;
import static seedu.duke.Ui.displayStringIndexOutOfBoundsExceptionMessage;

public class Parser {
    protected String userInput;
    protected LocalDateTime date;
    protected DayMap calList;

    public Parser(String userInput) {
        this.userInput = userInput;
        this.date = LocalDateTime.now();
        this.calList = Duke.getDayMap();
    }

    public Command parseCommand() {
        String[] arguments = userInput.split(" ", 2); //TODO split for all types of spaces etc TAB.

        try {
            switch (arguments[0].toLowerCase()) {
            case "add":
                return prepareAddCommand(userInput);
            case "find":
                //TODO apply SLAP
                if (arguments[1].startsWith("d/")) {
                    arguments[1] = arguments[1].substring(3).trim();
                    System.out.println("Here are the matching descriptions: ");
                    //used method inside daymap to get size of the activitylist instead
                    for (int i = 0; i < calList.getSizeOfActivityList(date); i++) {
                        //created new method to get the arraylist inside the activity list from the daymap
                        //maybe can find a better implementation of this later on
                        String description = calList.getArrayList(date).toArray()[i].toString().substring(
                                0, calList.getArrayList(date).toArray()[i].toString().indexOf(",")).trim();
                        if (description.contains(arguments[1])) {
                            System.out.println(calList.getArrayList(date).toArray()[i]);
                        }
                    }
                } else if (arguments[1].startsWith("c/")) {
                    arguments[1] = arguments[1].substring(7).trim();
                    System.out.println("Here are the matching descriptions: ");
                    //used method inside daymap to get size of the activitylist instead
                    for (int i = 0; i < calList.getSizeOfActivityList(date); i++) {
                        //created new method to get the arraylist inside the activity list from the daymap
                        //maybe can find a better implementation of this later on
                        String calories = calList.getArrayList(date).toArray()[i].toString().substring(
                                calList.getArrayList(date).toArray()[i].toString().indexOf(",") + 1).trim();
                        if (calories.equals(arguments[1])) {
                            System.out.println(calList.getArrayList(date).toArray()[i]);
                        }
                    }
                }
                break;
            case "edit":
                //TODO apply SLAP
                Userinfo store = new Userinfo();
                store.editUserInfo(arguments[1]);
                Initialiseuser.save(store);
                break;
            case "delete":
                return prepareDeleteCommand(arguments[1]);
            case "list":
                return new ListCommand();
            case "bye":
                displayByeMessage();
                System.exit(0);
                break;
            default:
                break;
            }
        } catch (StringIndexOutOfBoundsException e) {
            displayStringIndexOutOfBoundsExceptionMessage();
        } catch (IOException e) {
            displayIOExceptionMessage();
        }
        return null;
    }

    private Command prepareAddCommand(String userInput) {
        try {
            String[] arguments = userInput.split(" ", 2);
            if (arguments[1].startsWith("f/")) {
                int calorieIndex = arguments[1].indexOf("c/");
                int calories = Integer.parseInt(arguments[1].substring(calorieIndex + 2).trim());
                arguments[1] = arguments[1].substring(2, calorieIndex - 1).trim();

                return new AddFoodCommand(arguments[1], calories);
            } else if (arguments[1].startsWith("e/")) {
                int calorieIndex = arguments[1].indexOf("c/");
                int calories = Integer.parseInt(arguments[1].substring(calorieIndex + 2).trim());
                arguments[1] = arguments[1].substring(2, calorieIndex - 1).trim();

                return new AddExerciseCommand(arguments[1], calories);
            }
        } catch (NullPointerException | StringIndexOutOfBoundsException e) {
            displayAddCommandErrorMessage();
        }
        return null;
    }

    private Command prepareDeleteCommand(String userInput) {
        try {
            int index = Integer.parseInt(userInput);

            return new DeleteCommand(index - 1);
        } catch (NumberFormatException e) {
            displayDeleteCommandNumberFormatExceptionMessage();
        } catch (NullPointerException e) {
            displayDeleteCommandNullPointerExceptionMessage();
        }
        return null;
    }
}
