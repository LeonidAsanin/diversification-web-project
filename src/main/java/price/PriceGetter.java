package price;

import assets.Ticker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Class for obtaining price information about FinEx and VTB funds.
 * Static getters of this class are based on obtaining data from
 *  site.
 * Thus, when the site structure is changed, then respective getter should be updated.
 *
 * @author lennardjones
 * @version 1.3
 * @since 1.0
 */
public class PriceGetter {
    private PriceGetter() {
    }

    public static double get(Ticker ticker) {
        String currentPrice = "";
        URLConnection urlConnection;

        try {
            var url = new URL("https://www.tinkoff.ru/invest/etfs/" + ticker);
            urlConnection = url.openConnection();
        } catch (MalformedURLException e) {
            System.err.println("Error in the URL address");
            e.printStackTrace();
            return 0;
        } catch (IOException e) {
            System.err.println("Error of Input/Output");
            e.printStackTrace();
            return 0;
        }

        try (var inputStream = urlConnection.getInputStream();
             var bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {

            String infoString;

            /* marker substring for search required string with price data */
            String requiredString = "стоимость одной акции фонда";

            while ((infoString = bufferedReader.readLine()) != null) {
                if (infoString.contains(requiredString)) {
                    int requiredIndex = infoString.indexOf(requiredString);

                    /* search for substring that contains price information excluding thousands */
                    int end = infoString.indexOf(". Сделка", requiredIndex);
                    while (infoString.charAt(end) != ' ')
                        end--;
                    int start = end - 1;
                    while (infoString.charAt(start) != ' ')
                        start--;
                    currentPrice = infoString.substring(start + 1, end);
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("IOException from BufferedReader");
            e.printStackTrace();
        }

        return Double.parseDouble(currentPrice);
    }
}