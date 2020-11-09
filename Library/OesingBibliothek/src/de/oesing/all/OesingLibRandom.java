package de.oesing.all;

import java.math.BigInteger;
import java.util.concurrent.ThreadLocalRandom;

public class OesingLibRandom {

	/**
	 * @param type
	 *            The classtype we want to create a random value of.
	 * @return Returns a random object of the given type.
	 *         {@link OesingLibRandom#createRandom(Class, Integer, Integer)}
	 */
	public static <T> T createRandom(final Class<T> type) {
		return createRandom(type, null, null);
	}

	/**
	 * @param type
	 *            The classtype we want to create a random value of.
	 * @param minValue
	 *            The lower bound for the creation of random objects. For
	 *            example an integer gets generated in the interval
	 *            [minValue,maxValue). Or a string with length in the same
	 *            interval. Will get ignored if null.
	 * @param maxValue
	 *            The upper bound for the creation of random objects. For
	 *            example an integer gets generated in the interval
	 *            [minValue,maxValue). Or a string with length in the
	 *            interval [minValue,maxValue). Will get ignored if null.
	 * @return Returns a random object of the given type.
	 *         {@link OesingLibRandom#createRandom(Class))}
	 */
	public static <T> T createRandom(final Class<T> type, final Integer minValue, final Integer maxValue) {
		switch (type.getName().toLowerCase()) {
		case "int":
		case "java.lang.integer":
			final int maxI = (maxValue == null) ? Integer.MAX_VALUE : maxValue;
			final int minI = (minValue == null) ? Integer.MAX_VALUE : minValue;
			final int randomInt = ThreadLocalRandom.current().nextInt(minI, maxI);
			return type.cast(randomInt);
		case "long":
		case "java.lang.long":
			final long maxL = (maxValue == null) ? Long.MAX_VALUE : maxValue;
			final long minL = (minValue == null) ? Long.MAX_VALUE : minValue;
			final long randomLong = ThreadLocalRandom.current().nextLong(minL, maxL);
			return type.cast(randomLong);
		case "float":
		case "java.lang.float":
			float randomFloat = ThreadLocalRandom.current().nextFloat();
			/*
			 * Float.MIN_VALUE is not the smallest float but the smallest
			 * difference between two floats. To behave like Integer.MIN_VALUE
			 * just take the negative MAX_VALUE of Float. But that introduces
			 * the problem that ~2 times the MAX_VALUE is obviously too big so
			 * the Max_Value is halfed.
			 */
			final float maxF = (maxValue == null) ? Float.MAX_VALUE / 2 - 1 : maxValue;
			final float minF = (minValue == null) ? -(Float.MAX_VALUE / 2 + 1) : minValue;
			randomFloat = minF + randomFloat * (maxF - minF);
			return type.cast(randomFloat);
		case "double":
		case "java.lang.double":
			Double randomDouble = ThreadLocalRandom.current().nextDouble();
			/*
			 * Double.MIN_VALUE is not the smallest double but the smallest
			 * difference between two doubles. To behave like Integer.MIN_VALUE
			 * just take the negative MAX_VALUE of Double. But that introduces
			 * the problem that ~2 times the MAX_VALUE is obviously too big so
			 * the Max_Value is halfed.
			 */
			final double maxD = (maxValue == null) ? Double.MAX_VALUE / 2 - 1 : maxValue;
			final double minD = (minValue == null) ? -(Double.MAX_VALUE / 2 + 1) : minValue;
			randomDouble = minD + randomDouble * (maxD - minD);
			return type.cast(randomDouble);
		case "byte":
		case "java.lang.byte":
			final int maxB = (maxValue == null) ? 1 : maxValue;
			final int minB = (minValue == null) ? 1 : minValue;
			ThreadLocalRandom.current().nextInt(minB, maxB);
			final byte[] randomBytes = new byte[ThreadLocalRandom.current().nextInt(minB, maxB)];
			/*
			 * This function allows to generate multiple Bytes at the same time,
			 * which is not consistent with the semantic, but is handy.
			 */
			ThreadLocalRandom.current().nextBytes(randomBytes);
			return type.cast(randomBytes[0]);
		case "java.lang.string": // Characters from ascii 33 "!" to 126 "~"
			final StringBuilder sb = new StringBuilder();
			final int maxS = maxValue == null ? 1000 : maxValue;
			final int minS = (minValue == null) ? 100 : minValue;
			int length = ThreadLocalRandom.current().nextInt(minS, maxS);
			while (length > 0) {
				final int y = ThreadLocalRandom.current().nextInt(0, 93 + 1);
				sb.append((char) (y + 33));
				length--;
			}
			return type.cast(sb.toString());
		case "biginteger":
		case "java.math.biginteger":
			final int maxBig = (maxValue == null) ? 600 : maxValue;
			final int minBig = (minValue == null) ? 10 : minValue;
			final int randomSize = ThreadLocalRandom.current().nextInt(minBig, maxBig);
			return type.cast(new BigInteger(randomSize, ThreadLocalRandom.current()));
		default:
			System.out.println("The creation of the object type " + type.getName() + " is not supported.");
			return null;
		}
	}
}
