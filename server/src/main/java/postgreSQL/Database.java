package postgreSQL;

import org.apache.logging.log4j.core.Logger;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Class for holding database tables for working with them
 */
public class Database {
    private final UsersRepo usersTable;
    private final DragonRepo dragonRepo;
    private final Logger logger;
    //тут все окей, я использую это для того, чтобы получать в различных местах текущий класс для управления конкретной таблицей, это лучше чем каждый раз это создавать
    public Database(Connection connection, Logger logger) {
        this.dragonRepo = new DragonRepo(connection);
        this.usersTable = new UsersRepo(connection);
        this.logger = logger;

        try {
            initTables();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void initTables() throws SQLException {
        dragonRepo.init();
        usersTable.init();
    }

    public DragonRepo getDragonTable() {
        return dragonRepo;
    }

    public UsersRepo getUsersTable() {
        return usersTable;
    }
}