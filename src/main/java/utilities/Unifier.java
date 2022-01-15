package utilities;


/**
 * Utility class which is supposed to convert different values to a unified format.
 * 
 * @author lennardjones
 * @version 1.3
 * @since 1.0
 */
public class Unifier {
	
	/**
	 * Convert double value to String with 2 decimal places.
	 * 
	 * @param value Can be positive or negative
	 * @return String Represents double value in money format
	 */
	public static String doubleToMoney(double value) {
		var money = String.valueOf(value);
		
		if (Math.abs(value) >= 1e7) return money;
		if (Math.abs(value) <=  0.01 && value != 0) return "<0.01";
		
		money += "0";
		var pointIndex = money.indexOf('.');
		return money.substring(0, pointIndex + 3);
	}
}
