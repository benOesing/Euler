import java.util.List;
import java.util.ArrayList;

public class Euler6 {

	/*
	 * The sum of the squares of the first ten natural numbers is,
	 * 
	 * The square of the sum of the first ten natural numbers is,
	 * 
	 * Hence the difference between the sum of the squares of the first ten natural
	 * numbers and the square of the sum is .
	 * 
	 * Find the difference between the sum of the squares of the first one hundred
	 * natural numbers and the square of the sum.
	 */

	public static void main(String[] args) {
		Euler6 euler = new Euler6();
		final List<Integer> testCases = new ArrayList<>();
		testCases.add(10);
		testCases.add(20);
		testCases.add(25);
		testCases.add(100);
		testCases.add(1000);

		for (final Integer n : testCases) {
			System.out.println("Test starting:\n");
			long time = System.currentTimeMillis();
			euler.solveEfficient(n);
			System.out.printf("Efficient algorithm took:  ~ %5s ms for input n = %10s\n",
					(System.currentTimeMillis() - time), n);
		}
	}

	/*
	 * This solution uses the gaussian summation formula (n * n+1)/2) from Problem 1
	 * to calculate the sum of all numbers up to n. And the summation formula for
	 * squares (n(n+1)(2n+1))/6.
	 */
	public String solveEfficient(int n) {
		// Use the small gaussian to sum up all necessary multiples
		int sum = n * (n + 1) / 2;
		int sumOfSquares = (n * (n + 1) * (2 * n + 1)) / 6;
		return Long.toString((long) Math.pow(sum, 2) - sumOfSquares);
	}

}