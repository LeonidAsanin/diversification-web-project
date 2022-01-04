import assets.FinExTicker;
import assets.VTBTicker;
import org.junit.Assert;
import org.junit.Test;
import price.PriceGetter;

public class PriceGetterTest {

    @Test(timeout = 60_000L)
    public void shouldGetAllValues() {
        for (var ticker : FinExTicker.values()) {
            try {
                Assert.assertNotEquals(0, PriceGetter.get(ticker), 0.01);
            } catch (Exception e) {
                System.out.println("Exception was caught! Second attempt to get price of " + ticker);
                Assert.assertNotEquals(0, PriceGetter.get(ticker), 0.01);
            }
        }
        for (var ticker : VTBTicker.values()) {
            try {
                Assert.assertNotEquals(0, PriceGetter.get(ticker), 0.01);
            } catch (Exception e) {
                System.out.println("Exception was caught! Second attempt to get price of " + ticker);
                Assert.assertNotEquals(0, PriceGetter.get(ticker), 0.01);
            }
        }
    }
}
