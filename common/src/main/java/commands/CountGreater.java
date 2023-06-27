package commands;

import data.Dragon;
import dataTransmission.CommandResult;
import util.DataManager;

public class CountGreater extends AbstractCommand{


    private final String arg;

    public CountGreater(String arg) {
        super("count_greater");
        this.arg = arg;
    }

    @Override
    public CommandResult execute(
            DataManager dataManager,
            String username
    ) {
        int intArg;
        intArg = Integer.parseInt(arg);
        int count = dataManager.countGreater(intArg);

        return new CommandResult("Count of greater elements: " + count, true);
    }
}
