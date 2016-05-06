package de.syscy.dispensertweaker.util;

public class NumberUtil {
	public static boolean isNumber(String string) {
		try {
			Integer.parseInt(string);

			return true;
		} catch(NumberFormatException ex) {
			return false;
		}
	}
}