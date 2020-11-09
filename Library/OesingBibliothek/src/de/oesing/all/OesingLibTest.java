package de.oesing.all;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author Naix
 *
 */
public class OesingLibTest {

	/**
	 * This function creates random input and tests if both functions have the
	 * same output, while also recording the time it took to execute all random
	 * test cases. Intend is to rewrite a function and automaticly compare the
	 * old and the new version.
	 *
	 * @param invokedFrom
	 *            The object the method will get invoked from.
	 * @param m1
	 *            The first method we want to compare.
	 * @param m2
	 *            The second method we want to compare.
	 *            {@link OesingLibRandom#createRandom(Class)}
	 *            {@link Method#invoke(Object, Object...)}
	 */
	public static void compareMethods(final Object invokedFrom, final Method m1, final Method m2) {
		// Setting up
		long timeSpent1 = 0;
		long timeSpent2 = 0;
		final Class<?>[] inputTypes = m1.getParameterTypes();
		final Object[] args = new Object[inputTypes.length];
		final Object[] result = new Object[2];
		for (int numberOfTests = 0; numberOfTests < 10000; numberOfTests++) {
			for (int j = 0; j < inputTypes.length; j++) {
				args[j] = OesingLibRandom.createRandom(inputTypes[j]);
			}
			long start = 0;
			try {
				start = System.nanoTime();
				result[0] = m1.invoke(invokedFrom, args);
				timeSpent1 += System.nanoTime() - start;
				start = System.nanoTime();
				result[1] = m2.invoke(invokedFrom, args);
				timeSpent2 += System.nanoTime() - start;
			} catch (final Exception e) {
				e.printStackTrace();
			}
			if (!result[0].equals(result[1])) {
				System.out.printf("Error for\n%s\n Result was = (%s,%s)\n", Arrays.toString(args), result[0],
						result[1]);
				break;
			}
		}
		System.out.printf(
				"Method 1 did all tests in : %s milliseconds \n" + "Method 2 did all tests in : %s milliseconds\n",
				timeSpent1 / 1000000f, timeSpent2 / 1000000f);
	}

}
