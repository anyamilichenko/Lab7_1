package commands;

import data.Dragon;
import dataTransmission.CommandResult;
import util.DataManager;

public class RemoveGreater extends AbstractCommand {
    private final Dragon arg;

    public RemoveGreater(Dragon arg) {
        super("remove_greater");
        this.arg = arg;
    }

    @Override
    public CommandResult execute(
            DataManager dataManager,
            String username
    ) {

        dataManager.removeGreaterIfOwned(arg, username);

        return new CommandResult("Successfully removed greater elements", true);
    }
}