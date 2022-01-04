package database;

import assets.FinExTicker;
import assets.VTBTicker;
import diversificationCriterion.Country;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Class that is supposed to calculate and show country diversification in descending order.
 *
 * @author lennardjones
 * @version 1.2
 * @since 1.0
 */
public class CountryDiversification {
    private Map<Country, Double> map = new HashMap<>();
    private final InvestmentPortfolio investmentPortfolio;

    public CountryDiversification(InvestmentPortfolio investmentPortfolio) {
        this.investmentPortfolio = investmentPortfolio;
        calculate();
        sort();
    }

    public Set<Map.Entry<Country, Double>> getEntrySet() {
        return map.entrySet();
    }

    private void calculate() {
        CountryShares.getAllValuesFromDatabase();
        for (var country : Country.values()) {
            var totalShareInRubles = 0.;

            for (var ticker : FinExTicker.values())
                totalShareInRubles += CountryShares.get(country, ticker) *
                        investmentPortfolio.getTotalPriceByTicker(ticker);
            for (var ticker : VTBTicker.values())
                totalShareInRubles += CountryShares.get(country, ticker) *
                        investmentPortfolio.getTotalPriceByTicker(ticker);

            if (totalShareInRubles < 0.01) continue;

            map.put(country, totalShareInRubles);
        }
    }

    private void sort() {
        map = map.entrySet().stream().sorted(Map.Entry.<Country, Double>comparingByValue().reversed()).
                collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    public void show() {
        System.out.println("\nDiversification you have:");

        for (var entry : map.entrySet())
            System.out.printf("%-15s %13.2f ₽ (%6.2f %%)\n", entry.getKey() + ":", entry.getValue(),
                              entry.getValue() / investmentPortfolio.getSum() * 100);
    }
}
