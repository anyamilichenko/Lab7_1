package postgreSQL;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface Repo<T> {
    void init() throws SQLException;

    T mapRowToObject(ResultSet resultSet) throws SQLException;

    long add(T element) throws SQLException;
}