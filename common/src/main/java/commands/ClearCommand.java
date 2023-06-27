package commands;

import dataTransmission.CommandResult;
import util.DataManager;


public class ClearCommand extends AbstractCommand {
    public ClearCommand() {
        super("clear");
    }

    @Override
    public CommandResult execute(
            DataManager dataManager,
            String username
    ) {
        dataManager.clearOwnedData(username);
        return new CommandResult("The data you owned was cleared successfully.", true);
    }
}