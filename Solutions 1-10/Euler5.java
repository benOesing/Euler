import java.math.BigInteger;

import java.util.List;
import java.util.ArrayList;

public class Euler5 {

	/*
	 * 2520 is the smallest number that can be divided by each of the numbers from 1
	 * to 10 without any remainder.
	 *
	 * What is the smallest positive number that is evenly divisible by all of the
	 * numbers from 1 to 20?
	 */

    public static void main(String[] args) {
		Euler5 euler = new Euler5();
		final List<Integer> testCases = new ArrayList<>();
		testCases.add(10);
		testCases.add(20);
		testCases.add(25);

		for(final Integer n: testCases){
			System.out.println("Test starting:\n");
				long time = System.currentTimeMillis();
				euler.solveEfficient(n);
				System.out.printf("Efficient algorithm took:  ~ %5s ms for input n = %10s\n",
						(System.currentTimeMillis() - time), n);
		}
    }

	/*
	 * The problem is equivalent to solving the following: Find the primes p_1,..,p_m, so they can construct the numbers 1-n
	 */ 
	public String solveEfficient(int n) {
		final ArrayList<Integer> factors = new ArrayList<>();
		for (int i = 2; i <= n; i++) { // ignore one
			int x = i;
			if (isPrime(x)) { // Any prime has its unique factorization (itself and one)
				factors.add(x);
			} else {
				for (int j = 0; j < factors.size() && x != 1; j++) { // prime factorization of x
					if (x % factors.get(j) == 0) {
						x /= factors.get(j);
					}
				}
				if (x != 1) { // A factor is missing to represent i, namely x
					factors.add(x);
				}
			}
		}
		long result = 1;
		for(final Integer factor : factors) {
			result *= factor;
		}
		return Long.toString(result);
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