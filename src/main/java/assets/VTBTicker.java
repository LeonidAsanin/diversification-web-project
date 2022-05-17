package assets;

/**
 * Enumeration of VTB funds tickers.
 *
 * @author lennardjones
 * @version 1.3.1
 * @since 1.0
 */
public enum VTBTicker implements Ticker {
     VTBB("vtbrcbsb"), VTBG("vtbfzb"), VTBM("vtbfl"), VTBX("vtbfimb");

    /**
     * To visit the official site for the specific fund we need its page name:<br>
     * https://www.vtbcapital-am.ru/products/bpif/...page_name.../investment_strategy/<br>
     * For example: for VTBE its page is "vtbfars":<br>
     * https://www.vtbcapital-am.ru/products/bpif/vtbfars/investment_strategy/
     */
    String fundOfficialSitePage;

    VTBTicker(String mark) {
        fundOfficialSitePage = mark;
    }

    public String getFundOfficialSitePage() {
        return fundOfficialSitePage;
    }
}
