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
 * @version 1.3
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

    public void showExtended() {
        var USAmap = new LinkedHashMap<Country, Double>();
        var developedMarketMap = new LinkedHashMap<Country, Double>();
        var emergencyMarketMap = new LinkedHashMap<Country, Double>();

        var developedMarketSum = 0.;
        var emergencyMarketSum = 0.;

        for (var entry : map.entrySet()) {
            var country = entry.getKey();
            var shareRub = entry.getValue();
            if (country.equals(Country.USA)) {
                USAmap.put(country, shareRub);
            } else if (country.isDevelopedCountry()) {
                developedMarketMap.put(country, shareRub);
                developedMarketSum += shareRub;
            } else {
                emergencyMarketMap.put(country, shareRub);
                emergencyMarketSum += shareRub;
            }
        }

        System.out.println("\n\t\tDiversification you have:");

        if (!USAmap.isEmpty()) {
            System.out.printf("%-15s %13.2f ₽ (%6.2f %%)\n", "USA" + ":", USAmap.get(Country.USA),
                    USAmap.get(Country.USA) / investmentPortfolio.getSum() * 100);
        }

        if (!developedMarketMap.isEmpty()) {
            System.out.println("\n\t\tDeveloped Market:");

            for (var entry : developedMarketMap.entrySet())
                System.out.printf("%-15s %13.2f ₽ (%6.2f %%)\n", entry.getKey() + ":", entry.getValue(),
                        entry.getValue() / investmentPortfolio.getSum() * 100);

            System.out.println("------------------------------------------");
            System.out.printf("%-15s %13.2f ₽ (%6.2f %%)\n", "Total sum: ", developedMarketSum,
                    developedMarketSum / investmentPortfolio.getSum() * 100);
        }

        if (!emergencyMarketMap.isEmpty()) {
            System.out.println("\n\t\tEmergency Market:");

            for (var entry : emergencyMarketMap.entrySet())
                System.out.printf("%-15s %13.2f ₽ (%6.2f %%)\n", entry.getKey() + ":", entry.getValue(),
                        entry.getValue() / investmentPortfolio.getSum() * 100);

            System.out.println("------------------------------------------");
            System.out.printf("%-15s %13.2f ₽ (%6.2f %%)\n", "Total sum: ", emergencyMarketSum,
                    emergencyMarketSum / investmentPortfolio.getSum() * 100);
        }
    }
}
