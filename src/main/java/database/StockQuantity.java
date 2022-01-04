package database;

import assets.Ticker;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that stores information about user's each stock quantity.
 *
 * @author lennardjones
 * @version 1.2
 * @since 1.0
 */
public class StockQuantity {
    private final Map<Ticker, Integer> MAP = new HashMap<>();

    public StockQuantity() {
    }

    public void put(Ticker ticker, Integer quantity) {
        MAP.put(ticker, quantity);
    }

    public int get(Ticker ticker) {
        return MAP.get(ticker);
    }
}
