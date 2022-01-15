package diversificationCriterion;

/**
 * Enumeration of all countries whose company's stocks are present in different funds.
 *
 * @author lennardjones
 * @version 1.3
 * @since 1.0
 */
public enum Country {
    Australia(0, true), Belgium(1, true),
    Brazil(2, false), Canada(3, true),
    China(4, false), Denmark(5, true),
    Finland(6, true), France(7, true),
    Germany(8, true), Great_Britain(9, true),
    Hong_Kong(10, true), India(11, false),
    Indonesia(12, false), Ireland(13, true),
    Italy(14, true), Japan(15, true),
    Kazakhstan(16, false), Luxembourg(17, true),
    Malaysia(18, false), Mexico(19, false),
    Netherlands(20, true), Poland(21, true),
    Russia(22, false), Saudi_Arabia(23, false),
    Singapore(24, true), South_Africa(25, false),
    South_Korea(26, false), Spain(27, true),
    Sweden(28, true), Switzerland(29, true),
    Taiwan(30, false), Thailand(31, false),
    USA(32, true), Philippines(33, false),
    Turkey(34, false), Chile(35, false),
    Greece(36, false), Hungary(37, false),
    Colombia(38, false), Czech_Republic(39, false),
    Egypt(40, false), Peru(41, false),
    UAE(42, false);

    int index;
    boolean developedCountry;

    Country(int index, boolean developedCountry) {
        this.index = index;
        this.developedCountry = developedCountry;
    }

    public int getIndex() {
        return index;
    }

    public boolean isDevelopedCountry() {
        return developedCountry;
    }
}
