package commands;

import dataTransmission.CommandResult;
import util.DataManager;

public class RemoveById extends AbstractCommand implements PrivateAccessedDragonCommand {
    private final String arg;

    public RemoveById(String arg) {
        super("remove_by_id");
        this.arg = arg;
    }

    @Override
    public CommandResult execute(
            DataManager dataManager,
            String username
    ) {


        int intArg;
        try {
            intArg = Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            return new CommandResult("Your argument was incorrect. The command was not executed.", true);
        }

        dataManager.removeDragonById(intArg);

        return new CommandResult("The element was removed if it was in the data", true);
    }

    @Override
    public long getDragonId() {
        try {
            return Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}