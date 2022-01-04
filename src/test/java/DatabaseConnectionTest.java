import assets.FinExTicker;
import assets.VTBTicker;
import database.DatabaseConnection;
import diversificationCriterion.Country;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;

public class DatabaseConnectionTest {
    public static DatabaseConnection databaseConnection;

    @BeforeClass
    public static void setDatabaseConnection() throws SQLException {
        databaseConnection = DatabaseConnection.getInstance();
        databaseConnection.connect("jdbc:mysql://localhost:3306/diversification_database",
                    "root","asd123LOLsql");
    }

    @Test
    public void shouldGetResultSetFromDatabase() throws SQLException {
        databaseConnection.getResultSetFromTable("country_shares");
    }

    @Test
    public void shouldGetAllDoubleValuesFromDatabase() throws SQLException {
        for (var country : Country.values()) {
            for (var ticker : FinExTicker.values()) {
                databaseConnection.getDouble("country_shares", country.toString(),
                        "Ticker = '" + ticker + "'");
            }
            for (var ticker : VTBTicker.values()) {
                databaseConnection.getDouble("country_shares", country.toString(),
                        "Ticker = '" + ticker + "'");
            }
        }
    }

    @AfterClass
    public static void disconnectFromDatabase() throws SQLException {
        databaseConnection.disconnect();
    }
}
