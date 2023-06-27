package commands;

import dataTransmission.CommandResult;
import util.DataManager;

import java.io.Serializable;

public abstract class AbstractCommand implements Serializable {
    private final String name;

    protected AbstractCommand(String name) {
        this.name = name;
    }

    public abstract CommandResult execute(
            DataManager dataManager,
            String username
    );

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Command{"
                + "name='" + name + '\''
                + '}';
    }
}

