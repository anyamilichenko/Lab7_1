package commands;

import data.Dragon;
import dataTransmission.CommandResult;
import util.DataManager;

public class UpdateCommand extends AbstractCommand implements PrivateAccessedDragonCommand {
    private final String idArg;
    private final Dragon dragon;

    public UpdateCommand(Dragon dragon, String id) {
        super("update");
        this.idArg = id;
        this.dragon = dragon;
    }

    @Override
    public CommandResult execute(
            DataManager dataManager,
            String username
    ) {
        int intArg;
        try {
            intArg = Integer.parseInt(idArg);
        } catch (NumberFormatException e) {
            return new CommandResult("Your argument was incorrect. The command was not executed.", true);
        }
        dataManager.updateDragonById(intArg, dragon);
        return new CommandResult("Element was updated if it was in the table", true);
    }

    public long getDragonId() {
        try {
            return Integer.parseInt(idArg);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}




