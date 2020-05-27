package utility;

public class Utils {
	public static int NUM_STRINGS = 2;
	
	public static String removeEnding(String str, String ending) {
		String[] s = new String[NUM_STRINGS];
		s = str.split(ending, Utils.NUM_STRINGS);
		return s[0];
	}
}
