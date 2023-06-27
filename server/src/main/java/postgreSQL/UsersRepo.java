package postgreSQL;

import data.User;

import java.sql.*;
import java.util.LinkedList;

public class UsersRepo implements Repo<User> {
    private final Connection connection;

    public UsersRepo(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void init() throws SQLException {
        try (
                Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS users ("
                    + "    id serial PRIMARY KEY,"
                    + "    login varchar(100) NOT NULL UNIQUE,"
                    + "    password varchar(100) NOT NULL)");
        }
    }


    @Override
    public User mapRowToObject(ResultSet resultSet) throws SQLException {

        return new User(
                resultSet.getLong("id"),
                resultSet.getString("password"),
                resultSet.getString("login")
        );

    }

    @Override
    public long add(User element) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO users VALUES (default, ?, ?) RETURNING id"
        )) {
            makePreparedStatement(preparedStatement, element);
            try (
                    ResultSet resultSet = preparedStatement.executeQuery()
            ) {
                resultSet.next();
                return resultSet.getInt("id");
            }
        }
    }


    public LinkedList<User> getUserCollection() throws SQLException {
        final LinkedList<User> newCollection = new LinkedList<>();

        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM users");
        ) {

            while (resultSet.next()) {
                User user = mapRowToObject(resultSet);
                newCollection.add(user);

            }
        }


        return newCollection;
    }

    private void makePreparedStatement(PreparedStatement preparedStatement, User user) throws SQLException {
        int currentParameterOffset = 0;
        preparedStatement.setString(++currentParameterOffset, user.getName());
        preparedStatement.setString(++currentParameterOffset, user.getPassword());
    }
}