package de.oesing.all;

import java.awt.Color;

public class OesingLibImage {

	/**
	 * @param c1
	 *            First color.
	 * @param c2
	 *            Second color.
	 * @return returns the euclidian distance between two rgb colors.
	 */
	public static int colorDistance(final Color c1, final Color c2) {
		int difference = 0;
		int r1, g1, b1, r2, g2, b2;
		r1 = c1.getRed();
		g1 = c1.getGreen();
		b1 = c1.getBlue();

		r2 = c2.getRed();
		g2 = c2.getGreen();
		b2 = c2.getBlue();

		difference = Math.abs(r1 - r2) + Math.abs(g1 - g2) + Math.abs(b1 - b2);
		return difference;
	}
}
