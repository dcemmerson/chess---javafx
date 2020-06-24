/*	filename: Utils.java
 * 	last modified: 06/24/2020
 * 	description: Helper static function defined in a separate class. Utils should
 * 					be instantiated, and only static methods should be defined in
 * 					Utils, in which there does not seem to be a more logical place
 * 					to define these methods.
 */

package utility;

public class Utils {
	public static int NUM_STRINGS = 2;
	
	public static String removeEnding(String str, String ending) {
		String[] s = new String[NUM_STRINGS];
		s = str.split(ending, Utils.NUM_STRINGS);
		return s[0];
	}
}
