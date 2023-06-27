package commands;

import dataTransmission.CommandResult;
import util.DataManager;

public class HelpCommand extends AbstractCommand {
    public HelpCommand() {
        super("help");
    }

    @Override
    public CommandResult execute(
            DataManager dataManager,
            String username
    ) {
        // stream api would not help
        return new CommandResult(
                "help : gives information about available commands\n"
                        + "info : gives information about collection\n"
                        + "show : shows every element in collection with string\n"
                        + "add {element} : adds new element to collection\n"
                        + "update id {element} : update element info by it's id\n"
                        + "remove_by_id id : delete element by it's id\n"
                        + "clear : clears collection\n"
                        + "execute_script file_name : executes script entered in a file\n"
                        + "exit : exits the program (!!!does not save data!!!)\n"
                        + "remove_last : remove the last element from the collection\n"
                        + "remove_greater {element} : deletes every element in the collection with value more than entered element's value\n"
                        + "reorder : sort the collection in the reverse order of the current\n"
                        + "remove_all_by_weight weight : remove from the collection all elements whose weight field value is equivalent to the specified\n"
                        + "count_greater_than_age age : print the number of elements whose age field value is greater than the specified\n"
                        + "print_ascending : prints every element in ascending order", true);
    }
}
