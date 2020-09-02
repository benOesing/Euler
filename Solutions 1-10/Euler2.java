import java.util.ArrayList;
import java.util.List;

public class Euler2 {

    /*
     * Each new term in the Fibonacci sequence is generated by adding the previous
     * two terms. By starting with 1 and 2, the first 10 terms will be:
     * 
     * 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, ...
     * 
     * By considering the terms in the Fibonacci sequence whose values do not exceed
     * four million, find the sum of the even-valued terms.
     */

    public static void main(String[] args) {
        Euler2 euler = new Euler2();
        List<Integer> testCases = new ArrayList<>();
        testCases.add(10);
        testCases.add(100);
        testCases.add(1000);
        testCases.add(10000);
        testCases.add(100000);
        testCases.add(1000000);
        testCases.add(4000000);
        testCases.add(10000000);
        testCases.add(100000000);
        System.out.println("Test starting:\n");
        for (final Integer n : testCases) {
            long time = System.currentTimeMillis();
            euler.solveEfficient(n);
            System.out.printf("Efficient algorithm took:  ~ %5s ms for input n = %10s\n",
                    (System.currentTimeMillis() - time), n);

        }
    }

    /*
     * This solution is easy to understand, as it only generates one fibonacci
     * number after another and checks for even´es. Its lightning fast, while taking
     * the minimal necessary space.
     */
    private int solveEfficient(Integer n) {
        int sum = 0;
        int a = 1; // fib(0)
        int b = 1; // fib(1)

        while (b <= n) {
            int tmp = b;
            b += a;
            a = tmp; // guarantee that a = fib(x), b= fib(x+1)
            if (b % 2 == 0) {
                sum += b;
            }
        }
        return sum;
    }
}