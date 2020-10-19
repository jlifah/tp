package seedu.duke;

import java.util.ArrayList;
import java.util.Arrays;

import static seedu.duke.Ui.displayEmptyActivityCounterMessage;


/**
 * List of activities for any day.
 */
public class ActivityList extends Duke {
    private ArrayList<Activity> activities;
    private int activityCounter;
    private int netCalorie;

    public ActivityList() {
        activities = new ArrayList<>();
        activityCounter = 0;
        netCalorie = 0;
    }

    /**
     * Returns the current number of activities in the list.
     *
     * @return current number of activities in the list
     */
    public int getNumberOfActivities() {
        return activities.size();
    }

    public ArrayList getArrayList() {
        return activities;
    }


    public void addActivity(Activity activity) {
        activities.add(activity);
        activityCounter++;
        if (activity instanceof Food) {
            netCalorie += activity.calories;
        } else if (activity instanceof Exercise) {
            netCalorie -= activity.calories;
        }
    }

    /**
     * This method replaces the current activity at index with a new activity.
     * To change the description of the current activity.
     * @param index is the index of the current activity to be replaced
     * @param activity is the new activity that will be replacing the current activity
     */
    public void addNewActivity(int index, Activity activity) {
        //Activity item = new Activity(userInput, calories);
        //activities.add(index, activity);
        activities.set(index, activity);
        //activityCounter++;
        if (activity instanceof Food) {
            netCalorie += activity.calories;
        } else if (activity instanceof Exercise) {
            netCalorie -= activity.calories;
        }
    }

    /**
     * This method inserts a activity from a given index and moves it to another index.
     * @param indexToBeChanged this is the index at which the activity will be moved from
     * @param indexToBeInsertedBelow this is the index at which the activity will be moved to below
     * @throws IndexOutOfBoundsException if the index is not within the limits
     */
    public void insertActivityAt(int indexToBeChanged, int indexToBeInsertedBelow) throws IndexOutOfBoundsException {

        if (isValidIndex(indexToBeChanged) && isValidIndex(indexToBeInsertedBelow)) {
            Activity activity = getActivity(indexToBeChanged );
            removeActivity(indexToBeChanged);
            activities.add(indexToBeInsertedBelow, activity);
            activityCounter++;
            if (activity instanceof Food) {
                netCalorie += activity.calories;
            } else if (activity instanceof Exercise) {
                netCalorie -= activity.calories;
            }
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public int getNetCalorie() {
        return netCalorie;
    }

    public Activity getActivity(int index) throws IndexOutOfBoundsException {
        if (isValidIndex(index)) {
            return activities.get(index);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * Removes an activity from the list via index.
     *
     * @param index index of acitivty in list
     */
    public void removeActivity(int index) throws IndexOutOfBoundsException {
        if (isValidIndex(index)) {
            Activity activityToRemove = activities.get(index);
            if (activityToRemove instanceof Food) {
                netCalorie -= activityToRemove.calories;
            } else if (activityToRemove instanceof Exercise) {
                netCalorie += activityToRemove.calories;
            }
            activities.remove(index);
            System.out.println("Activity pointed to is: " + activityToRemove.toString());
            activityCounter--;
            System.out.print("Activity removed!\n");
        } else {
            System.out.println("Please make sure index is within range");
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * Prints the list of activities.
     */
    public void printList() {
        if (activityCounter == 0) {
            displayEmptyActivityCounterMessage();
        } else {
            for (int i = 0; i < activityCounter; i++) {
                System.out.println((i + 1) + ". " + getActivity(i).toString());
            }
        }
    }

    /**
     * Checks if the index is valid.
     *
     * @param index index of acitvity in list
     * @return true if index is within range, else false
     */
    public boolean isValidIndex(int index) {
        if ((index >= 0) && (index < activityCounter)) {
            return true;
        }
        return false;
    }

    /**
     * Clears the list of activites.
     */
    public void clearList() {
        activities.clear();
        activityCounter = 0;
        netCalorie = 0;
    }

    /**
     * Sets the activities as a string.
     * For e.g, [F] | apple | 50, [F] | banana | 100, [E] | pushup | 10, [E] | jogging | 60
     * @return activities as a string
     */
    @Override
    public String toString() {
        String activitiesString = Arrays.toString(activities.toArray());
        activitiesString = activitiesString.substring(1, activitiesString.length() - 1);
        return (activitiesString);
    }
}
