package de.oesing.all;

import java.util.Arrays;

public class OesingLibString {

	private static boolean debug = false;

	/**
	 * @param number The number that gets turned into roman digits.
	 * @return Returns the smallest correct roman representation of the given number. So (IIII is not allowed while IV is)
	 */
	public static String asRomanNumber(int number) {
		final String[] order = new String[] { "I", "IV", "V", "IX", "X", "XL", "L", "XC", "C", "CD", "D", "CM", "M" };
		final int[] value = new int[] { 1, 4, 5, 9, 10, 40, 50, 90, 100, 400, 500, 900, 1000 };
		final StringBuilder out = new StringBuilder();
		int j = value.length - 1;
		while (number != 0) {
			for (int i = j; i >= 0; i--) {
				if (number >= value[i]) {
					number -= value[i];
					out.append(order[i]);
					break;
				} else {
					j = Math.min(j, i);
				}
			}
		}
		return out.toString();
	}

	/**
	 * @param s1
	 *            The first String.
	 * @param s2
	 *            The second String.
	 * @return Calculates the edit-Distance from first string to second string.
	 *         Using equal costs of one for deletion insertion and substitution.
	 *         Also known as Levenshtein distance.
	 *         {@link #OesingLibString()#editDistance(String, String, int, int,
	 *         int)}
	 */
	public static int editDistance(final String s1, final String s2) {
		return editDistance(s1, s2, 1, 1, 1);
	}

	/**
	 * This implementation is optimized to only use 2 rows which doesnt reduce
	 * the time complexity, but wont allow to reconstruct the strings.
	 *
	 * @param s1
	 *            First String
	 * @param s2
	 *            Second String
	 * @param deletion
	 *            Cost for a deletion.
	 * @param insertion
	 *            Cost for insertion.
	 * @param substitution
	 *            Cost for substitution.
	 * @return Edit-Distance from first string to second string.
	 *         {@link #OesingLibString()#editDistance(String, String)}
	 */
	public static int editDistance(final String s1, final String s2, final int deletion, final int insertion,
			final int substitution) {
		int[] v1 = new int[s2.length() + 1];
		int[] v2 = new int[s2.length() + 1];
		for (int i = 0; i <= s2.length(); i++) {
			v1[i] = i;
		}
		for (int i = 0; i < s1.length(); i++) {
			v2[0] = i + 1;
			for (int j = 0; j < s2.length(); j++) {
				if (s1.charAt(i) == s2.charAt(j)) {
					v2[j + 1] = Math.min(Math.min(v1[j + 1] + deletion, v2[j] + insertion), v1[j]);
				} else {
					v2[j + 1] = Math.min(Math.min(v1[j + 1] + deletion, v2[j] + insertion), v1[j] + substitution);
				}
			}
			if (debug) {
				System.out.println(Arrays.toString(v1));
				System.out.println(Arrays.toString(v2));
				System.out.println("");
			}
			final int[] temp = v1;
			v1 = v2;
			v2 = temp;
		}
		return v1[s2.length()];
	}

	public static void main(final String[] args) {
	}
}
