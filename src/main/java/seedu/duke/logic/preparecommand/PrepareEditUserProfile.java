package seedu.duke.logic.preparecommand;

import seedu.duke.command.Command;
import seedu.duke.command.EditUserProfileCommand;
import seedu.duke.userprofile.UserProfile;

import java.io.IOException;

/**
 * Prepares editing of user profile.
 */
public class PrepareEditUserProfile extends PrepareCommand {

    public PrepareEditUserProfile(String[] description) {
        super(description);
    }

    /**
     * Prepares to edit user profile.
     *
     * @return EditUserProfileCommand
     * @throws IOException when there is an error
     */
    public Command prepareCommand() throws IOException {
        UserProfile.edit(description[1]);
        return new EditUserProfileCommand();
    }

}