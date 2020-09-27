import java.math.BigInteger;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class Euler3 {

	/*
	 * The prime factors of 13195 are 5, 7, 13 and 29.
	 * 
	 * What is the largest prime factor of the number 600851475143?.
	 */
	public static void main(String[] args) {
		Euler3 euler = new Euler3();
		List<Long> testCases = new ArrayList<>();
		testCases.add(137L); // prime
		testCases.add(600851475143L);

		System.out.println("Test starting:\n");
		for (final Long n : testCases) {
			long time = System.currentTimeMillis();
			euler.solveEfficient(n);
			System.out.printf("Efficient algorithm took:  ~ %5s ms for input n = %10s\n",
					(System.currentTimeMillis() - time), n);
		}
	}

	/*
	 * This solution determines first if n is prime. If it is not it uses the sieve
	 * of eratosthenes to generate primes. If any primefactor is found, we take it
	 * as often as we can, until we find the last prime factor, therefore the
	 * biggest.
	 */
	private long solveEfficient(Long n) {
		// If n is prime, n is also its biggest prime factor.
		if (isPrime(n)) {
			return n;
		}
		// It is sufficient to check up to limit = sqrt(n), because any factor X =>
		// limit, is multiplied by a factor Y, with X*Y = n. That follows that Y <=
		// limit.
		int limit = (int) Math.sqrt(n);
		BitSet sieve = new BitSet(limit); // BitSet is space efficient

		sieve.set(2); // set first prime
		while (n / 2 + n / 2 == n) { // check if division is integer solution
			n /= 2; // take the prime factor 2 as often as possible
		}
		for (int i = 2; i < sieve.length(); i += 2) {
			sieve.set(i);
		}

		for (int i = 3; i < sieve.size(); i += 2) {
			if (!sieve.get(i)) { // not set (therefore prime)
				if (i == n) {
					return i; // found largest prime factor
				}
				while (n / i * i == n) { // check if division is integer solution
					n /= i; // take the prime factor i as often as possible
				}
				for (int j = i; j < sieve.length(); j += i) {
					sieve.set(j);
				}
			}
		}
		return -1;
	}

	/**
	 * Using Miller-Rabin primality test in the deterministic version for all Long.
	 *
	 * @param n Long value to check if prime.
	 * @return True if its a prime.
	 */
	private boolean isPrime(final long n) {
		final int[] toTest = new int[] { 2, 3, 5, 7, 11, 13, 17, 19, 23 };
		if (n <= 0) { // No negative primes.
			return false;
		}
		if (n <= 3) {
			return true;
		}
		if (n % 2 == 0) {
			return false;
		}
		int r = 0;
		long m = n - 1;
		while (m % 2 == 0) {
			r++;
			m /= 2;
		}
		for (int i = 0; i < toTest.length && toTest[i] < n; i++) {
			final long a = toTest[i];
			BigInteger x = BigInteger.valueOf(a);
			x = x.modPow(BigInteger.valueOf(m), BigInteger.valueOf(n));
			if (x.equals(BigInteger.ONE) || x.equals(BigInteger.valueOf(n - 1))) {
				continue;
			}
			int j = 0;
			for (; j < r - 1; j++) {
				x = x.modPow(BigInteger.valueOf(2), BigInteger.valueOf(n));
				if (x.equals(BigInteger.ONE)) {
					return false;
				}
				if (x.equals(BigInteger.valueOf(n - 1))) {
					break;
				}
			}
			if (j == r - 1) {
				return false;
			}
		}
		return true;
	}
}