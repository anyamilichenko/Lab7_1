package commands;

import data.Dragon;
import dataTransmission.CommandResult;
import util.DataManager;

import java.util.Collections;
import java.util.LinkedList;

public class Reorder extends AbstractCommand{
    public Reorder() {
        super("reorder");
    }

    @Override
    public CommandResult execute(

            DataManager dataManager,
            String username) {
        if (dataManager.getCollectionSize() == 0) return new CommandResult("Your collection is empty. The command was not executed.", true);
        LinkedList<Dragon> dragons = dataManager.getMainData();
        Collections.reverse(dragons);
        dataManager.setCollection(dragons);

        return new CommandResult("The collection has been successfully flipped", true);
    }
}
