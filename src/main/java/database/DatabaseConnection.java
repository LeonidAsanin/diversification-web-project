package database;

import java.sql.*;
import java.util.Objects;

/**
 * Singleton class for connection to the remote database purposes.
 *
 * @author lennardjones
 * @version 1.3
 * @since 1.1
 */
public class DatabaseConnection {
    private static DatabaseConnection databaseConnection;
    private Connection connection;
    private Statement statement;

    private DatabaseConnection() {
    }

    public static DatabaseConnection getInstance() {
        return Objects.requireNonNullElseGet(databaseConnection, () -> databaseConnection = new DatabaseConnection());
    }

    public void connect(String url, String user, String password) throws SQLException {
        if (connection != null) {
            try {
                disconnect();
            } catch (SQLException e) {
                throw new SQLException("Can't disconnect");
            }
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
        } catch (ClassNotFoundException e) {
            throw new SQLException("Can't load Driver for MySQL");
        } catch (SQLException e) {
            throw new SQLException("Can't connect");
        }
    }

    public void disconnect() throws SQLException {
        statement.close();
        connection.close();
    }

    public double getDouble(String table, String column, String findingCondition) throws SQLException {
        try {
            var resultSet = statement
                    .executeQuery("SELECT " + column + " FROM " + table + " WHERE " + findingCondition + ";");
            resultSet.next();
            return resultSet.getDouble(column);
        } catch (SQLException e) {
            throw new SQLException("Unable to get value from database");
        }
    }

    public ResultSet getResultSetFromTable(String table) throws SQLException {
        try {
            return statement.executeQuery("SELECT * FROM " + table + ";");
        } catch (SQLException e) {
            throw new SQLException("Unable to get values from database");
        }
    }
}
