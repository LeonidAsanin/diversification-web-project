package database;

import assets.FinExTicker;
import assets.Ticker;
import assets.VTBTicker;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that is supposed to calculate and show user's investment portfolio
 *
 * @author lennardjones
 * @version 1.2
 * @since 1.0
 */
public class InvestmentPortfolio {
    private final Map<Ticker, Double> STOCKS_MAP = new HashMap<>();
    private double sum = 0;
    private volatile boolean calculationCompleted = false;
    private final StockQuantity stockQuantity;

    public InvestmentPortfolio(StockQuantity stockQuantity) {
        this.stockQuantity = stockQuantity;

        /*
         * Thread to perform calculateInvestmentPortfolio() method separately
         * in order to other calculations can be performed independently and concurrently.
         */
        new Thread(this::calculateInvestmentPortfolio).start();
    }

    private double calculateTotalPriceByTicker(Ticker ticker) {
        var quantity = stockQuantity.get(ticker);
        if (quantity == 0) return 0;

        return quantity * StockPrices.get(ticker);
    }

    private void calculateInvestmentPortfolio() {
        for (var ticker : FinExTicker.values()) {
            var assetTotalPrice = calculateTotalPriceByTicker(ticker);
            sum += assetTotalPrice;
            STOCKS_MAP.put(ticker, assetTotalPrice);
        }
        for (var ticker : VTBTicker.values()) {
            var assetTotalPrice = calculateTotalPriceByTicker(ticker);
            sum += assetTotalPrice;
            STOCKS_MAP.put(ticker, assetTotalPrice);
        }
        calculationCompleted = true;
    }

    /* Waits for calculateInvestmentPortfolio() method to complete */
    public double getTotalPriceByTicker(Ticker ticker) {
        while (!calculationCompleted) Thread.onSpinWait();
        return STOCKS_MAP.getOrDefault(ticker, 0.);
    }

    /* Waits for calculateInvestmentPortfolio() method to complete */
    public double getSum() {
        while (!calculationCompleted) Thread.onSpinWait();
        return sum;
    }

    public void showTotalPriceByTicker(Ticker ticker) {
        System.out.printf("%s: %12.2f ₽/share", ticker, StockPrices.get(ticker));
        System.out.printf("%14.2f ₽\n", calculateTotalPriceByTicker(ticker));
    }

    /**
     * Shows user's investment portfolio (including only FinEx and VTB funds) that is quantity and
     * total price of the specific asset and the sum of all assets in the end.
     */
    public void show() {
        System.out.println("\nYour investment portfolio: ");

        for (var ticker : FinExTicker.values())
            if (stockQuantity.get(ticker) > 0) showTotalPriceByTicker(ticker);
        for (var ticker : VTBTicker.values())
            if (stockQuantity.get(ticker) > 0) showTotalPriceByTicker(ticker);

        System.out.println("------------------------------------------");
        System.out.print("Sum of all assets:");
        System.out.printf("%22.2f ₽\n", getSum());// getSum() returns value after calculation completion
    }
}
