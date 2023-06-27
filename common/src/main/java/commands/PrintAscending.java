package commands;


import dataTransmission.CommandResult;
import util.DataManager;

public class PrintAscending extends AbstractCommand {

    public PrintAscending() {
        super("print_ascending");
    }

    @Override
    public CommandResult execute(
            DataManager dataManager,
            String username
    ) {

        return new CommandResult(dataManager.ascendingDataToString(), true);
    }
}