package logic;

import commands.*;
import connection.ConnectionManager;
import data.Dragon;
import dataTransmission.CommandFromClient;
import dataTransmission.CommandResult;
import exceptions.DataCantBeSentException;
import util.DataObjectsMaker;
import util.InputManager;
import util.OutputManager;

import java.io.IOException;
import java.io.Serializable;
import java.nio.channels.UnresolvedAddressException;
import java.util.Collection;
import java.util.NoSuchElementException;


public class CommandHandler {
    private static final int MAX_STRING_LENGTH = 100;
    private final OutputManager outputManager;
    private final InputManager inputManager;
    private final ConnectionManager connectionManager;
    private final Collection<String> listOfCommands;
    private String username;
    private String password;
    private String passwordToRegister;


    public CommandHandler(
            OutputManager outputManager,
            InputManager inputManager,
            ConnectionManager connectionManager,
            Collection<String> listOfCommands
    ) {
        this.outputManager = outputManager;
        this.inputManager = inputManager;
        this.connectionManager = connectionManager;
        this.listOfCommands = listOfCommands;
    }

    public void start() throws ClassNotFoundException, IOException, DataCantBeSentException, UnresolvedAddressException {
        System.out.println("Client is working");
        initUsernameAndPassword();
        DataObjectsMaker dataObjectsMaker = new DataObjectsMaker(inputManager, outputManager, username);
        String input;
        do {
            input = readNextCommand();
            if ("exit".equals(input)) {
                break;
            }
            final String[] parsedInp = parseToNameAndArg(input);
            final String commandName = parsedInp[0];
            Serializable commandArg = parsedInp[1];
            String commandArg2 = ""; // only for update command in this case
            if (listOfCommands.contains(commandName)) {
                if ("add".equals(commandName) || "add_if_min".equals(commandName) || "remove_greater".equals(commandName)) {
                    commandArg = dataObjectsMaker.makeDragon();
                }
                if ("update".equals(commandName)) {
                    commandArg2 = (String) commandArg;
                    commandArg = dataObjectsMaker.makeDragon();
                }
                if ("register".equals(commandName)) {
                    commandArg = dataObjectsMaker.makeLoginAndPassword();
                }
                if ("execute_script".equals(commandName)) {
                    new ExecuteScriptCommand((String) commandArg).execute(inputManager);
                } else {
                    try {
                        outputManager.println(
                                connectionManager.sendCommand(new CommandFromClient(getCommandObjectByName(commandName, commandArg, commandArg2), username, password)).getOutput().toString());
                    } catch (DataCantBeSentException e) {
                        outputManager.println("Could not send a command");
                    }
                }
            } else {
                outputManager.println("The command was not found. Please use \"help\" to know about commands.");
            }

        } while (true);
    }

    private void initUsernameAndPassword() throws IOException, DataCantBeSentException, UnresolvedAddressException {
        outputManager.println("Would you like to register first? (type \"yes\" to register or something else to continue with your own password+login).");
        final String answer = inputManager.nextLine();
        if ("yes".equals(answer)) {
            outputManager.println("Enter your new login");
            final String loginToRegister = inputManager.nextLine();
            outputManager.println("Enter new password");
            final String passwordToRegister = inputManager.nextLine();

            if (loginToRegister.length() > MAX_STRING_LENGTH || passwordToRegister.length() > MAX_STRING_LENGTH) {
                outputManager.println("Your password or login was too long");
                return;
            }

            CommandResult registerCommandResult = connectionManager.sendCommand(new CommandFromClient(new RegisterCommand(new String[]{loginToRegister, passwordToRegister})));
            if (registerCommandResult.isWasExecutedCorrectly()) {
                if (!((RegisterCommand.RegisterCommandResult) registerCommandResult).isWasRegistered()) {
                    outputManager.println("User was not registered because the username was not unique.");
                    initUsernameAndPassword();
                } else {
                    password = passwordToRegister;
                    username = loginToRegister;
                }
            } else {
                throw new DataCantBeSentException();
            }
        } else {
            outputManager.println("Enter login");
            username = inputManager.nextLine();
            outputManager.println("Enter password");
            password = inputManager.nextLine();
        }
    }

    private String[] parseToNameAndArg(String input) {
        String[] arrayInput = input.split(" ");
        String inputCommand = arrayInput[0];
        String inputArg = "";

        if (arrayInput.length >= 2) {
            inputArg = arrayInput[1];
        }

        return new String[]{inputCommand, inputArg};
    }

    private String readNextCommand() throws IOException {
        outputManager.print(">>>");
        try {
            return inputManager.nextLine();
        } catch (NoSuchElementException e) {
            return "exit";
        }
    }

    private AbstractCommand getCommandObjectByName(String commandName, Serializable arg, String arg2) {
        AbstractCommand command = null;
        switch (commandName) {
            case "add": command = new AddCommand((Dragon) arg);
                break;
            case "info": command = new InfoCommand();
                break;
            case "show": command = new ShowCommand();
                break;
            case "update": command = new UpdateCommand((Dragon) arg, arg2);
                break;
            case "remove_greater": command = new RemoveGreater((Dragon) arg);
                break;
            case "clear": command = new ClearCommand();
                break;
            case "count_greater_than_age": command = new CountGreater(arg.toString());
                break;
            case "reorder": command = new Reorder();
                break;
            case "remove_last": command = new RemoveLast();
                break;
            case "remove_by_id": command = new RemoveById(arg2);
                break;
            case "remove_all_by_weight": command = new RemoveAllByWeight(arg.toString());
                break;
            case "print_ascending": command = new PrintAscending();
                break;
            case "help": command = new HelpCommand();
                break;
            default: System.out.println("The command was not found. Enter help for help");
                break;
        }
        return command;
    }
}