package postgreSQL;

import data.*;

import java.sql.*;
import java.util.LinkedList;

public class DragonRepo implements Repo<Dragon> {
    private static final int PARAMETER_OFFSET_NAME = 1;
    private static final int PARAMETER_OFFSET_COORDINATES_X = 2;
    private static final int PARAMETER_OFFSET_COORDINATES_Y = 3;
    private static final int PARAMETER_OFFSET_AGE = 4;
    private static final int PARAMETER_OFFSET_DRAGON_CHARACTER = 5;
    private static final int PARAMETER_OFFSET_WINGSPAN = 6;
    private static final int PARAMETER_OFFSET_WEIGHT = 7;
    private static final int PARAMETER_OFFSET_DEPTH = 8;
    private static final int PARAMETER_OFFSET_NUMBER_OF_TREASURES = 9;
    private static final int PARAMETER_OFFSET_AUTHOR = 10;
    private final Connection connection;


    public DragonRepo(Connection connection) {
        this.connection = connection;
    }


    @Override
    public void init() throws SQLException {
        try (
                Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS dragon ("
                    + "id serial PRIMARY KEY,"
                    + "name varchar(100) NOT NULL,"
                    + "coordinates_x double precision NOT NULL,"
                    + "coordinates_y double precision NOT NULL,"
                    + "dragons_age bigint,"
                    + "dragon_character varchar(100) NOT NULL,"
                    + "depth_of_cave double precision,"
                    + "number_of_treasures float NOT NULL,"
                    + "wingspan bigint NOT NULL,"
                    + "weight double precision NOT NULL,"
                    + "author_username varchar(100) NOT NULL)");

        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    @Override
    public Dragon mapRowToObject(ResultSet resultSet) throws SQLException {
        final Dragon dragon = new Dragon(
                resultSet.getString("name"),
                new Coordinates(
                        resultSet.getDouble("coordinates_x"),
                        resultSet.getDouble("coordinates_y")
                ),
                resultSet.getLong("dragons_age"),
                DragonCharacter.valueOf(resultSet.getString("dragon_character")),
                new DragonCave(
                        resultSet.getDouble("depth_of_cave"),
                        resultSet.getFloat("number_of_treasures")
                ),
                resultSet.getInt("wingspan"),
                resultSet.getDouble("weight"),
                resultSet.getString("author_username")
        );

        dragon.setId(resultSet.getInt("id"));

        return dragon;
    }

    @Override
    public long add(Dragon element) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO dragon VALUES (default, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id"
        )) {
            makePreparedStatementOfDragon(preparedStatement, element);
            try (
                    ResultSet resultSet = preparedStatement.executeQuery()
            ) {
                resultSet.next();
                return resultSet.getLong("id");
            }
        }catch (SQLException e){
            e.printStackTrace();
            return -1;
        }
    }

    private void makePreparedStatementOfDragon(PreparedStatement preparedStatement, Dragon dragon) throws SQLException {
        preparedStatement.setString(PARAMETER_OFFSET_NAME, dragon.getName());
        preparedStatement.setDouble (PARAMETER_OFFSET_COORDINATES_X, dragon.getCoordinates().getX());
        preparedStatement.setDouble(PARAMETER_OFFSET_COORDINATES_Y, dragon.getCoordinates().getY());
        preparedStatement.setLong(PARAMETER_OFFSET_AGE, dragon.getAge());
        preparedStatement.setString(PARAMETER_OFFSET_DRAGON_CHARACTER, dragon.getCharacter().toString());
        preparedStatement.setInt(PARAMETER_OFFSET_WINGSPAN, dragon.getWingspan());
        preparedStatement.setDouble(PARAMETER_OFFSET_WEIGHT, dragon.getWeight());
        if (dragon.getCave().getDepth() != null){
            preparedStatement.setDouble(PARAMETER_OFFSET_DEPTH, dragon.getCave().getDepth());
        } else {
            preparedStatement.setNull(PARAMETER_OFFSET_DEPTH, Types.VARCHAR);
        }
        preparedStatement.setFloat(PARAMETER_OFFSET_NUMBER_OF_TREASURES, dragon.getCave().getNumberOfTreasures());
        preparedStatement.setString(PARAMETER_OFFSET_AUTHOR, dragon.getAuthorName());
    }



    public LinkedList<Dragon> getDragonCollection() throws SQLException {
        final LinkedList<Dragon> newCollection = new LinkedList<>();

        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM dragon")

        ) {

            while (resultSet.next()) {
                Dragon dragon = mapRowToObject(resultSet);
                newCollection.add(dragon);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }

        return newCollection;
    }

    public void clearOwnedData(String username) throws SQLException {

        String query = "DELETE FROM dragon WHERE author_username=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.executeUpdate();
        }

    }

    public void removeById(long id) throws SQLException {
        try (
                Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate("DELETE FROM dragon WHERE id=" + id);

        }
    }

    public void removeLast(String username) throws SQLException {
        try (
                Statement statement = connection.createStatement()
        ) {
            ResultSet resultSet = statement.executeQuery("SELECT MAX(id) FROM dragon where author_username = \"" + username + "\";");
            if (resultSet.next()) {
                long lastId = resultSet.getInt(1);
                statement.executeUpdate("DELETE FROM dragon WHERE id=" + lastId);
            }
        }
    }

    public void updateById(long id, Dragon dragon) throws SQLException {


        String query = "UPDATE dragon SET "
                + "name=?"
                + ",coordinates_x=?"
                + ",coordinates_y=?"
                + ",creation_date=?"
                + ",dragons_age=?"
                + ",dragon_character=?"
                + ",depth_of_cave=?"
                + ",number_of_treasures=?"
                + ",wingspan=?"
                + ",weight=?"
                + ",author_username=? "
                + "WHERE id =" + id;


        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            makePreparedStatementOfDragon(preparedStatement, dragon);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeByWeight(Double weight, String username) throws SQLException {
        String query = "DELETE dragon where weight=" + weight + " author_username=\"" + username + "\";";

        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}