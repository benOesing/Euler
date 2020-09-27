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

    */
	public String solveEfficient() {
		int max = 0;
		for (int i = 999; i * 999 > max && i > 100; i--) {
			for (int j = 999; i * j > max && j > 100; j--) {
				final int product = i * j;
				if (isPalindrom(product)) {
					max = Integer.max(max, product);
					break;
				}
			}
		}
		return Integer.toString(max);
    }
    
    private boolean isPalindrom(int n){
        StringBuilder sb = new StringBuilder(""+n);
        return ("" + n).equals(sb.reverse().toString());
    }
}