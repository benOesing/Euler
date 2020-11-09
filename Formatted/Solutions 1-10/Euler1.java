import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Euler1 {

	/**
	 * If we list all the natural numbers below 10 that are multiples of 3 or 5, we
	 * get 3, 5, 6 and 9. The sum of these multiples is 23.
	 *
	 * Find the sum of all the multiples of 3 or 5 below 1000.
	 */

	public static void main(final String[] args) {
		final Euler1 euler = new Euler1();

		final List<Integer> testCases = new ArrayList<>();
		testCases.add(10);
		testCases.add(100);
		testCases.add(1000);
		testCases.add(10000);
		testCases.add(100000);
		testCases.add(1000000);
		testCases.add(10000000);
		testCases.add(Integer.MAX_VALUE);

		final List<Integer> divisors = new ArrayList<>(Arrays.asList(3, 5, 15));
		System.out.println("Test starting:\n");
		for (final Integer n : testCases) {
			long time = System.currentTimeMillis();
			euler.solveNaive(n);
			System.out.printf("Naive algorithm took:      ~ %5s ms for input n = %10s\n",
					(System.currentTimeMillis() - time), n);

			time = System.currentTimeMillis();
			euler.solveEfficient(n);
			System.out.printf("Efficient algorithm took:  ~ %5s ms for input n = %10s\n",
					(System.currentTimeMillis() - time), n);

			time = System.currentTimeMillis();
			euler.solveExtendable(n, divisors);
			System.out.printf("Extandable algorithm took: ~ %5s ms for input n = %10s\n",
					(System.currentTimeMillis() - time), n);

			System.out.println();
		}
	}

	/*
	 * This solution is efficient enough for any real usecase. It is trivial to
	 * understand. To extend it to any set of numbers the logic stays the same. See
	 * {@link #solveExtandable()}.
	 */
	public long solveNaive(final int n) {
		long sum = 0;
		for (int i = 1; i < n; i++) {
			if (i % 3 == 0 || i % 5 == 0) {
				sum += i;
			}
		}
		return sum;
	}

	/*
	 * This is the most efficient solution possible. It uses the gaussian summation
	 * formula (sum i=1 to n = (n * n+1)/2) to calculate all the multiples in O(n).
	 * To do that it uses the fact that the sum of all multiples of an integer X < n
	 * can be rewritten as the sum from 1 to n/X times X. This algorithm doesnt like
	 * to be extended due to the fact, that multiples have to be counted exactly
	 * once. For our example we count the multiples of 15 twice so we need to
	 * subtract them. For any set of divisors of size m, that would result in a
	 * subtraction and addition loop.
	 */
	public long solveEfficient(int n) {
		n--; // The Question asks for < n.
		long sum = 0;
		// Calculate the limits
		final long limit3 = n / 3;
		final long limit5 = n / 5;
		final long limit15 = n / 15;
		// Use the small gaussian to sum up all necessary multiples
		final long threes = 3 * (limit3 * (limit3 + 1)) / 2;
		final long fives = 5 * (limit5 * (limit5 + 1)) / 2;
		final long fifteens = 15 * (limit15 * (limit15 + 1)) / 2;
		// Its necessary to remove all multiples of fifteen because they got counted
		// twice.
		sum = threes + fives - fifteens;
		return sum;
	}

	/*
	 * This is inefficient, if time is the important factor then the solution from
	 * {@link #solveExtendable()} should be extended. Without the first optimization
	 * step its trivial to understand and the optimization is easy to understand
	 * anyway.
	 */
	public long solveExtendable(final int n, final List<Integer> divisors) {
		// Remove any divisor that is overshadowed by a smaller divisor. If X > Y and X
		// % Y == 0, then any multiple of X is obviously divisible by Y, so X can be
		// removed from the list of divisors.
		for (int i = 0; i < divisors.size(); i++) {
			for (int j = i + 1; j < divisors.size(); j++) {
				if (divisors.get(j) % divisors.get(i) == 0) {
					divisors.remove(j);
					i--;
					break;
				}
			}
		}

		long sum = 0;
		for (int i = 1; i < n; i++) {
			for (final Integer possibleDivisor : divisors) {
				if (i % possibleDivisor == 0) {
					sum += i;
					break;
				}
			}
		}
		return sum;
	}

}
