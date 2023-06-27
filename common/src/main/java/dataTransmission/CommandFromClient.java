package dataTransmission;

import commands.AbstractCommand;

import java.io.Serializable;
import java.util.Objects;

public class CommandFromClient implements Serializable {
    private final AbstractCommand command;
    private final String login;
    private final String password;


    public CommandFromClient(AbstractCommand command, String login, String password) {
        this.command = command;
        this.login = login;
        this.password = password;
    }

    public CommandFromClient(AbstractCommand command) {
        this.command = command;
        this.login = "";
        this.password = "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CommandFromClient that = (CommandFromClient) o;
        return Objects.equals(command, that.command) && Objects.equals(login, that.login) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(command, login, password);
    }

    public AbstractCommand getCommand() {
        return command;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}