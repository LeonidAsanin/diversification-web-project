package database;

import assets.FinExTicker;
import assets.Ticker;
import assets.VTBTicker;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that is supposed to calculate, retrieve and show user's investment portfolio.
 *
 * @author lennardjones
 * @version 1.2
 * @since 1.0
 */
public class InvestmentPortfolio {
    private final Map<Ticker, Double> STOCKS_MAP = new HashMap<>();
    private final StockQuantity stockQuantity;
    private double sum = 0;
    private boolean calculationCompleted = false;

    public InvestmentPortfolio(StockQuantity stockQuantity) {
        this.stockQuantity = stockQuantity;
        calculateInvestmentPortfolio();
    }

    public InvestmentPortfolio(StockQuantity stockQuantity, boolean calculateAsSeparateThread) {
        this.stockQuantity = stockQuantity;

        if (calculateAsSeparateThread)
            new Thread(this::calculateInvestmentPortfolio).start();
        else
            calculateInvestmentPortfolio();
    }

    private double calculateTotalPriceByTicker(Ticker ticker) {
        var quantity = stockQuantity.get(ticker);
        if (quantity == 0) return 0;

        return quantity * StockPrices.get(ticker);
    }

    private synchronized void calculateInvestmentPortfolio() {
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
        notifyAll();
    }

    public synchronized double getTotalPriceByTicker(Ticker ticker) {
        while (!calculationCompleted) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return STOCKS_MAP.getOrDefault(ticker, 0.);
    }

    public synchronized double getSum() {
        while (!calculationCompleted) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return sum;
    }

    /**
     * Shows total price of a certain asset by its ticker. Works properly for FinEx and VTB ETF funds only.
     */
    public void showTotalPriceByTicker(Ticker ticker) {
        System.out.printf("%s: %12.2f ₽/share", ticker, StockPrices.get(ticker));
        System.out.printf("%14.2f ₽\n", calculateTotalPriceByTicker(ticker));
    }

    /**
     * Shows the user's investment portfolio (including only FinEx and VTB ETF funds), that is list of tickers, prices
     * and total prices of certain assets and the size of the entire portfolio in the end.
     */
    public void show() {
        System.out.println("\nYour investment portfolio: ");

        for (var ticker : FinExTicker.values())
            if (stockQuantity.get(ticker) > 0) showTotalPriceByTicker(ticker);
        for (var ticker : VTBTicker.values())
            if (stockQuantity.get(ticker) > 0) showTotalPriceByTicker(ticker);

        System.out.println("------------------------------------------");
        System.out.print("Sum of all assets:");
        System.out.printf("%22.2f ₽\n", getSum());
    }
}
