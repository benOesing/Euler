public class Library{

    //TODO: Refactor to use efficient BitSet

    /**
	 * Using the Sieve of Eratosthenes to generate all primes to a specified
	 * limit.
	 *
	 * @param limit
	 *            The biggest possible prime that gets returned, excluded limit
	 *            exactly.
	 * @return A List of primes.
	 */
	public static List<Integer> listPrimes(final int limit) {
		return listPrimes(2, limit);
	}

	/**
	 * Using the Sieve of Eratosthenes to generate all primes to a specified
	 * limit.
	 *
	 * @param limit
	 *            The biggest possible prime that gets returned, excluded limit
	 *            exactly.
	 * @param smallest
	 *            The smallest prime that gets added to the list.
	 * @return A List of primes.
	 */
	public static List<Integer> listPrimes(final int smallest, final int limit) {
		if (limit < 2) {
			return new ArrayList<>();
		}
		List<Integer> primes = null;
		final int maxSize = 50000000; // ~200MB
		int i = maxSize;
		do {
			primes = listPrimes(i - maxSize, Math.min(i, limit), primes);
			i += maxSize;
		} while (i < limit);
		for (int j = 0; primes.get(j) < smallest; j++) {
			primes.remove(primes.get(j));
			j--;
		}
		return primes;
	}

	/**
	 * @param lowerBound
	 * @param upperBound
	 * @param primes
	 * @see #listPrimes(int, int)
	 * @return
	 */
	private static List<Integer> listPrimes(int lowerBound, final int upperBound, List<Integer> primes) {
		lowerBound = Math.max(lowerBound, 3);
		if (primes == null) {
			primes = new ArrayList<>();
		}
		if (!primes.contains(2)) {
			primes.add(2);
		}
		if (lowerBound % 2 == 0) {
			lowerBound++;
		}
		BitSet bits;
		bits = new BitSet();
		for (int i = 1; i < primes.size(); i++) { // Skip 2
			final int prime = primes.get(i);
			for (int j = prime; j <= upperBound; j += prime) {
				bits.set(j);
			}
		}
		for (int i = lowerBound; i <= upperBound; i += 2) {
			if (bits.get(i) == false) {
				primes.add(i);
				for (int j = 0; j <= upperBound; j += i) {
					bits.set(j);
				}
			}
		}
		return primes;
	}
}