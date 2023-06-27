package commands;

import dataTransmission.CommandResult;
import util.InputManager;

import java.io.File;
import java.io.IOException;

public class ExecuteScriptCommand {

    private final String arg;

    public ExecuteScriptCommand(String arg) {
        this.arg = arg;
    }

    public void execute(InputManager inputManager) {
        try {
            inputManager.connectToFile(new File(arg));
            new CommandResult("Starting to execute script...", true);
        } catch (IOException e) {
            new CommandResult("There was a problem opening the file. Check if it is available and you have written it in the command arg correctly.", false);
        } catch (UnsupportedOperationException e) {
            new CommandResult(e.getMessage(), false);
        }
    }
}