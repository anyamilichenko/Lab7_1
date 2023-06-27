package commands;

/**
 * A mark that indicates that this command should only be executed
 * if the executor is the owner of a dragon
 */
public interface PrivateAccessedDragonCommand {
    long getDragonId();
}