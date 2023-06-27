package executing;

import org.apache.logging.log4j.core.Logger;
import util.State;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Console {
    private final State<Boolean> serverIsRunningState;
    private final Logger logger;
    private final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    public Console(State<Boolean> serverIsRunningState, Logger logger) {
        this.serverIsRunningState = serverIsRunningState;
        this.logger = logger;
    }

    public void start() {
        while (serverIsRunningState.getValue()) {
            final String input;
            try {
                input = bufferedReader.readLine();
            } catch (IOException e) {
                logger.error("An unexpected IO exception occurred");
                serverIsRunningState.setValue(false);
                break;
            }
            if ("exit".equals(input)) {
                serverIsRunningState.setValue(false);
                logger.info("Closing server...");
            }
        }
    }
}