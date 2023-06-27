package commands;

import dataTransmission.CommandResult;
import util.DataManager;

import java.io.Serializable;

public class InfoCommand extends AbstractCommand {

    public InfoCommand() {
        super("info");
    }

    @Override
    public CommandResult execute(
            DataManager dataManager,
            String username
    ) {

        return new CommandResult(dataManager.getCollectionInfo(), true);
    }

    public static final class InfoCommandResult implements Serializable {
        private final int numberOfElements;
        private final int biggestStudentsCount;

        public InfoCommandResult(Integer numberOfElements, int biggestStudentsCount) {
            this.numberOfElements = numberOfElements;
            this.biggestStudentsCount = biggestStudentsCount;
        }

        @Override
        public String toString() {
            return "InfoCommandResult{"
                    + "numberOfElements='" + numberOfElements + '\''
                    + ", biggestStudentsCount=" + biggestStudentsCount
                    + '}';
        }
    }
}