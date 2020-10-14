import java.lang.StringBuilder;

public class Euler4 {

	/*
	 * A palindromic number reads the same both ways. The largest palindrome
	 * made from the product of two 2-digit numbers is 9009 = 91 × 99.
	 *
	 * Find the largest palindrome made from the product of two 3-digit numbers.
     */

    public static void main(String[] args) {
        Euler4 euler = new Euler4();
        System.out.println("Test starting:\n");
            long time = System.currentTimeMillis();
            euler.solveEfficient();
            System.out.printf("Efficient algorithm took:  ~ %5s ms for input n = %10s\n",
                    (System.currentTimeMillis() - time), 2);

    }

	/*
	 * This solution simply creates any product of two 3-digit numbers going from biggest to smallest.
	 * Could be improved to rather create palindroms and then test if they got a product of two 3-digit integers. 
	 */
	public String solveEfficient() {
		int max = 0;
		for (int i = 999; i * i > max && i >= 100; i--) { // Stop when the maximum product of i and max(j) = i is smaller than the biggest palindrome found.
			for (int j = i; i * j > max && j >= 100; j--) { // Stop when the maximum product of i and j = i is smaller than the biggest palindrome found.
				final int product = i * j;
				if (isPalindrom(product)) {
					max = Integer.max(max, product); // Simple way to update a maximum
					break;
				}
			}
		}
		return Integer.toString(max);
    }
	
	/*
	 * This function simply uses the build in functionality of the StringBuilder class.
	 */
    private boolean isPalindrom(int n){
        StringBuilder sb = new StringBuilder(""+n);
        return ("" + n).equals(sb.reverse().toString());
    }
}