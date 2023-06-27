package commands;

import dataTransmission.CommandResult;
import util.DataManager;

public class RemoveAllByWeight extends AbstractCommand {
    private final String arg;

    public RemoveAllByWeight(String arg) {
        super("remove_all_by_weight");
        System.out.println(arg);
        this.arg = arg;
    }

    @Override
    public CommandResult execute(DataManager dataManager, String username) {
        System.out.println("weight");
        System.out.println(this.arg);
        Double weightValue = Double.parseDouble(arg); // Преобразуем строку в целое число
        dataManager.removeAllByWeight(weightValue, username);
        return new CommandResult("Successfully removed all dragons with weight: " + weightValue, true);
    }
}


