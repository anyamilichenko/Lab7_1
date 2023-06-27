package commands;

import dataTransmission.CommandResult;
import util.DataManager;

public class ShowCommand extends AbstractCommand {


    public ShowCommand() {
        super("show");
    }

    @Override
    public CommandResult execute(
            DataManager dataManager,
            String username
    ) {

        return new CommandResult(dataManager.showSortedByName(), true);
    }
}