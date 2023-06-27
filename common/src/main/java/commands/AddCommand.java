package commands;

import data.Dragon;
import dataTransmission.CommandResult;
import util.DataManager;

public class AddCommand extends AbstractCommand {
    private final Dragon arg;

    public AddCommand(Dragon arg) {
        super("add");
        this.arg = arg;
    }

    @Override
    public CommandResult execute(
            DataManager dataManager,
            String username
    ) {
        Dragon dragon = arg;
        dragon.setId(-1);
        dataManager.addDragon(dragon);
        return new CommandResult("The element was added successfully", true);
    }
}