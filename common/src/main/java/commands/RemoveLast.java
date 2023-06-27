package commands;

import dataTransmission.CommandResult;
import util.DataManager;


public class RemoveLast extends AbstractCommand {

    public RemoveLast() {
        super("remove_last");
    }

    @Override
    public CommandResult execute(
            DataManager dataManager,
            String username
    ) {
        try {
            dataManager.getCollectionSize();
        } catch (NumberFormatException e) {
            return new CommandResult("Your collection is empty. The command was not executed.", true);
        }

        dataManager.removeLast(username);

        return new CommandResult("The element was removed if it was in the data", true);
    }
}