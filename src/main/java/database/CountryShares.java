package database;

import assets.FinExTicker;
import assets.Ticker;
import assets.VTBTicker;
import diversificationCriterion.Country;

import java.sql.SQLException;
import java.util.*;

/**
 * This class is trying to connect to remote database to obtain information about shares of countries in the specific
 * asset from the table that contains "Ticker" as first column and different countries as the other columns. <br><br>
 *
 * Example of table:<br>
 * <table>
 *     <tr>
 *         <td>Ticker</td>
 *         <td>Australia</td>
 *         <td>Belgium</td>
 *         <td>...</td>
 *     </tr>
 *     <tr>
 *         <td>FXDM</td>
 *         <td>0.0780</td>
 *         <td>0.0040</td>
 *         <td>...</td>
 *     </tr>
 *     <tr>
 *         <td>FXRW</td>
 *         <td>0.0550</td>
 *         <td>0.0000</td>
 *         <td>...</td>
 *     </tr>
 * </table>
 *  ... <br><br>
 *
 * If connection has not been established or another exception occurred then default values are used.
 *
 * @author lennardjones
 * @version 1.3
 * @since 1.0
 */
public class CountryShares {
    private static final Map<Ticker, Double[]> COEFFICIENT_MAP = new HashMap<>();

    private CountryShares() {
    }

    public static double get(Country country, Ticker ticker) {
        return COEFFICIENT_MAP.get(ticker)[country.getIndex()];
    }

    /**
     * Gets information from MySQL database
     *
     * @return true if succeed and false otherwise
     */
    public static boolean getAllValuesFromDatabase() {
        var databaseConnection = DatabaseConnection.getInstance();
        try {
            databaseConnection.connect("jdbc:mysql://localhost:3306/diversification_database",
                    "user","password");
            var resultSet = databaseConnection.getResultSetFromTable("country_shares");

            var arraySize = Country.values().length;

            while (resultSet.next()) {
                var coefficientArray = new Double[arraySize];
                Arrays.fill(coefficientArray, 0.);
                for (var country : Country.values()) {
                    coefficientArray[country.getIndex()] = resultSet.getDouble(country.toString());
                }
                try {
                    COEFFICIENT_MAP.put(FinExTicker.valueOf(resultSet.getString("Ticker")), coefficientArray);
                } catch (IllegalArgumentException ignored) {}
                try {
                    COEFFICIENT_MAP.put(VTBTicker.valueOf(resultSet.getString("Ticker")), coefficientArray);
                } catch (IllegalArgumentException ignored) {}
            }
        } catch (SQLException e) {
            System.out.println("\nCannot get actual information about country shares: DEFAULT VALUES WERE USED");
            return false;
        }
        return true;
    }

    /* Defining default values for COEFFICIENT_MAP */
    static {
        var arraySize = Country.values().length;
        var coefficientArray = new Double[arraySize];
        Arrays.fill(coefficientArray, 0.);
        coefficientArray[Country.China.getIndex()] = 1.;
        COEFFICIENT_MAP.put(FinExTicker.FXCN, coefficientArray);

        coefficientArray = new Double[arraySize];
        Arrays.fill(coefficientArray, 0.);
        coefficientArray[Country.Germany.getIndex()] = 1.;
        COEFFICIENT_MAP.put(FinExTicker.FXDE, coefficientArray);

        coefficientArray = new Double[arraySize];
        Arrays.fill(coefficientArray, 0.);
        coefficientArray[Country.Japan.getIndex()] = .1755;
        coefficientArray[Country.France.getIndex()] = .1175;
        coefficientArray[Country.Great_Britain.getIndex()] = .1165;
        coefficientArray[Country.Canada.getIndex()] = .1165;
        coefficientArray[Country.Switzerland.getIndex()] = .112;
        coefficientArray[Country.Germany.getIndex()] = .086;
        coefficientArray[Country.Australia.getIndex()] = .078;
        coefficientArray[Country.Netherlands.getIndex()] = .076;
        coefficientArray[Country.Denmark.getIndex()] = .024;
        coefficientArray[Country.Spain.getIndex()] = .023;
        coefficientArray[Country.Hong_Kong.getIndex()] = .017;
        coefficientArray[Country.Sweden.getIndex()] = .017;
        coefficientArray[Country.Italy.getIndex()] = .012;
        coefficientArray[Country.Finland.getIndex()] = .01;
        coefficientArray[Country.Singapore.getIndex()] = .009;
        coefficientArray[Country.Ireland.getIndex()] = .006;
        coefficientArray[Country.Belgium.getIndex()] = .004;
        COEFFICIENT_MAP.put(FinExTicker.FXDM, coefficientArray);

        coefficientArray = new Double[arraySize];
        Arrays.fill(coefficientArray, 0.);
        coefficientArray[Country.Taiwan.getIndex()] = .1665;
        coefficientArray[Country.South_Korea.getIndex()] = .1548;
        coefficientArray[Country.Brazil.getIndex()] = .1376;
        coefficientArray[Country.Russia.getIndex()] = .1305;
        coefficientArray[Country.South_Africa.getIndex()] = .0969;
        coefficientArray[Country.Mexico.getIndex()] = .0688;
        coefficientArray[Country.Thailand.getIndex()] = .0686;
        coefficientArray[Country.Indonesia.getIndex()] = .0553;
        coefficientArray[Country.Malaysia.getIndex()] = .0353;
        coefficientArray[Country.Philippines.getIndex()] = .025;
        coefficientArray[Country.Turkey.getIndex()] = .0188;
        coefficientArray[Country.Chile.getIndex()] = .0149;
        coefficientArray[Country.Greece.getIndex()] = .0115;
        coefficientArray[Country.Hungary.getIndex()] = .0089;
        coefficientArray[Country.Colombia.getIndex()] = .0057;
        COEFFICIENT_MAP.put(FinExTicker.FXEM, coefficientArray);

        coefficientArray = new Double[arraySize];
        Arrays.fill(coefficientArray, 0.);
        coefficientArray[Country.USA.getIndex()] = .457;
        coefficientArray[Country.Japan.getIndex()] = .181;
        coefficientArray[Country.China.getIndex()] = .18;
        coefficientArray[Country.Taiwan.getIndex()] = .077;
        coefficientArray[Country.South_Korea.getIndex()] = .047;
        coefficientArray[Country.Sweden.getIndex()] = .024;
        coefficientArray[Country.France.getIndex()] = .021;
        coefficientArray[Country.Poland.getIndex()] = .013;
        COEFFICIENT_MAP.put(FinExTicker.FXES, coefficientArray);

        coefficientArray = new Double[arraySize];
        Arrays.fill(coefficientArray, 0.);
        coefficientArray[Country.USA.getIndex()] = .957;
        coefficientArray[Country.Luxembourg.getIndex()] = .043;
        COEFFICIENT_MAP.put(FinExTicker.FXFA, coefficientArray);

        coefficientArray = new Double[arraySize];
        Arrays.fill(coefficientArray, 0.);
        COEFFICIENT_MAP.put(FinExTicker.FXGD, coefficientArray);

        coefficientArray = new Double[arraySize];
        Arrays.fill(coefficientArray, 0.);
        coefficientArray[Country.USA.getIndex()] = 1.;
        COEFFICIENT_MAP.put(FinExTicker.FXIM, coefficientArray);

        coefficientArray = new Double[arraySize];
        Arrays.fill(coefficientArray, 0.);
        coefficientArray[Country.USA.getIndex()] = 1.;
        COEFFICIENT_MAP.put(FinExTicker.FXIP, coefficientArray);

        coefficientArray = new Double[arraySize];
        Arrays.fill(coefficientArray, 0.);
        coefficientArray[Country.USA.getIndex()] = 1.;
        COEFFICIENT_MAP.put(FinExTicker.FXIT, coefficientArray);

        coefficientArray = new Double[arraySize];
        Arrays.fill(coefficientArray, 0.);
        coefficientArray[Country.Kazakhstan.getIndex()] = 1.;
        COEFFICIENT_MAP.put(FinExTicker.FXKZ, coefficientArray);

        coefficientArray = new Double[arraySize];
        Arrays.fill(coefficientArray, 0.);
        coefficientArray[Country.USA.getIndex()] = 1.;
        COEFFICIENT_MAP.put(FinExTicker.FXMM, coefficientArray);

        coefficientArray = new Double[arraySize];
        Arrays.fill(coefficientArray, 0.);
        coefficientArray[Country.Russia.getIndex()] = 1.;
        COEFFICIENT_MAP.put(FinExTicker.FXRB, coefficientArray);

        coefficientArray = new Double[arraySize];
        Arrays.fill(coefficientArray, 0.);
        coefficientArray[Country.USA.getIndex()] = .957;
        coefficientArray[Country.Luxembourg.getIndex()] = .043;
        COEFFICIENT_MAP.put(FinExTicker.FXRD, coefficientArray);

        coefficientArray = new Double[arraySize];
        Arrays.fill(coefficientArray, 0.);
        coefficientArray[Country.USA.getIndex()] = 1.;
        COEFFICIENT_MAP.put(FinExTicker.FXRE, coefficientArray);

        coefficientArray = new Double[arraySize];
        Arrays.fill(coefficientArray, 0.);
        coefficientArray[Country.Russia.getIndex()] = 1.;
        COEFFICIENT_MAP.put(FinExTicker.FXRL, coefficientArray);

        coefficientArray = new Double[arraySize];
        Arrays.fill(coefficientArray, 0.);
        coefficientArray[Country.Russia.getIndex()] = 1.;
        COEFFICIENT_MAP.put(FinExTicker.FXRU, coefficientArray);

        coefficientArray = new Double[arraySize];
        Arrays.fill(coefficientArray, 0.);
        coefficientArray[Country.USA.getIndex()] = .4;
        coefficientArray[Country.Japan.getIndex()] = .2;
        coefficientArray[Country.China.getIndex()] = .115;
        coefficientArray[Country.Great_Britain.getIndex()] = .113;
        coefficientArray[Country.Germany.getIndex()] = .086;
        coefficientArray[Country.Australia.getIndex()] = .055;
        coefficientArray[Country.Russia.getIndex()] = .031;
        COEFFICIENT_MAP.put(FinExTicker.FXRW, coefficientArray);

        coefficientArray = new Double[arraySize];
        Arrays.fill(coefficientArray, 0.);
        coefficientArray[Country.USA.getIndex()] = 1.;
        COEFFICIENT_MAP.put(FinExTicker.FXTB, coefficientArray);

        coefficientArray = new Double[arraySize];
        Arrays.fill(coefficientArray, 0.);
        coefficientArray[Country.USA.getIndex()] = 1.;
        COEFFICIENT_MAP.put(FinExTicker.FXTP, coefficientArray);

        coefficientArray = new Double[arraySize];
        Arrays.fill(coefficientArray, 0.);
        coefficientArray[Country.USA.getIndex()] = 1.;
        COEFFICIENT_MAP.put(FinExTicker.FXUS, coefficientArray);

        coefficientArray = new Double[arraySize];
        Arrays.fill(coefficientArray, 0.);
        coefficientArray[Country.USA.getIndex()] = .4;
        coefficientArray[Country.Japan.getIndex()] = .2;
        coefficientArray[Country.China.getIndex()] = .115;
        coefficientArray[Country.Great_Britain.getIndex()] = .113;
        coefficientArray[Country.Germany.getIndex()] = .086;
        coefficientArray[Country.Australia.getIndex()] = .055;
        coefficientArray[Country.Russia.getIndex()] = .031;
        COEFFICIENT_MAP.put(FinExTicker.FXWO, coefficientArray);

        coefficientArray = new Double[arraySize];
        Arrays.fill(coefficientArray, 0.);
        coefficientArray[Country.USA.getIndex()] = 1.;
        COEFFICIENT_MAP.put(VTBTicker.VTBA, coefficientArray);

        coefficientArray = new Double[arraySize];
        Arrays.fill(coefficientArray, 0.);
        coefficientArray[Country.Russia.getIndex()] = 1.;
        COEFFICIENT_MAP.put(VTBTicker.VTBB, coefficientArray);

        coefficientArray = new Double[arraySize];
        Arrays.fill(coefficientArray, 0.);
        coefficientArray[Country.China.getIndex()] = .3101;
        coefficientArray[Country.Taiwan.getIndex()] = .1616;
        coefficientArray[Country.India.getIndex()] = .1318;
        coefficientArray[Country.South_Korea.getIndex()] = .1257;
        coefficientArray[Country.Brazil.getIndex()] = .0369;
        coefficientArray[Country.Russia.getIndex()] = .0344;
        coefficientArray[Country.South_Africa.getIndex()] = .0317;
        coefficientArray[Country.Saudi_Arabia.getIndex()] = .0293;
        coefficientArray[Country.Thailand.getIndex()] = .0192;
        coefficientArray[Country.Mexico.getIndex()] = .0181;
        coefficientArray[Country.Malaysia.getIndex()] = .015;
        coefficientArray[Country.Indonesia.getIndex()] = .0145;
        COEFFICIENT_MAP.put(VTBTicker.VTBE, coefficientArray);//add

        coefficientArray = new Double[arraySize];
        Arrays.fill(coefficientArray, 0.);
        COEFFICIENT_MAP.put(VTBTicker.VTBG, coefficientArray);

        coefficientArray = new Double[arraySize];
        Arrays.fill(coefficientArray, 0.);
        coefficientArray[Country.USA.getIndex()] = 1.;
        COEFFICIENT_MAP.put(VTBTicker.VTBH, coefficientArray);

        coefficientArray = new Double[arraySize];
        Arrays.fill(coefficientArray, 0.);
        coefficientArray[Country.Russia.getIndex()] = 1.;
        COEFFICIENT_MAP.put(VTBTicker.VTBM, coefficientArray);

        coefficientArray = new Double[arraySize];
        Arrays.fill(coefficientArray, 0.);
        coefficientArray[Country.Russia.getIndex()] = 1.;
        COEFFICIENT_MAP.put(VTBTicker.VTBU, coefficientArray);

        coefficientArray = new Double[arraySize];
        Arrays.fill(coefficientArray, 0.);
        coefficientArray[Country.Russia.getIndex()] = 1.;
        COEFFICIENT_MAP.put(VTBTicker.VTBX, coefficientArray);

        coefficientArray = new Double[arraySize];
        Arrays.fill(coefficientArray, 0.);
        coefficientArray[Country.Russia.getIndex()] = .5;
        coefficientArray[Country.Mexico.getIndex()] = .1;
        coefficientArray[Country.Indonesia.getIndex()] = .06;
        coefficientArray[Country.Greece.getIndex()] = .05;
        coefficientArray[Country.Hungary.getIndex()] = .046;
        coefficientArray[Country.Chile.getIndex()] = .046;
        coefficientArray[Country.China.getIndex()] = .042;
        coefficientArray[Country.Turkey.getIndex()] = .029;
        coefficientArray[Country.Czech_Republic.getIndex()] = .025;
        coefficientArray[Country.Egypt.getIndex()] = .024;
        coefficientArray[Country.Saudi_Arabia.getIndex()] = .019;
        coefficientArray[Country.Peru.getIndex()] = .015;
        coefficientArray[Country.UAE.getIndex()] = .013;
        coefficientArray[Country.Poland.getIndex()] = .01;
        coefficientArray[Country.Colombia.getIndex()] = .01;
        coefficientArray[Country.Philippines.getIndex()] = .005;
        COEFFICIENT_MAP.put(VTBTicker.VTBY, coefficientArray);
    }
}
