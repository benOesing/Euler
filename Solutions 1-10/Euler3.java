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
    
     */
    private int solveEfficient(Long n) {
        // generate primes with the Sieve of Eratosthenes

        // It is sufficient to check up to limit = sqrt(n), because any factor X =>
        // limit, is multiplied by a factor Y, with X*Y = n. That follows that Y <=
        // limit.
        int limit = (int) Math.sqrt(n);
        BitSet sieve = new BitSet(limit); //BitSet is space efficient
        sieve.set(2); // set first prime
        while(n/2 + n/2 == n){ // check if division is integer solution
            n /= 2; // take the prime factor 2 as often as possible
            System.out.println(n);
        }
        for(int i=2;i<sieve.length();i+=2){
            sieve.set(i);
        }
        for(int i=3;i<sieve.size();i+=2){
            if(!sieve.get(i)){ // not set (therefore prime)
                if(i == n){
                    return i; // found largest prime factor
                }
                while(n/i *i == n){ // check if division is integer solution
                    n /= i; // take the prime factor i as often as possible
                }
                for(int j=i;j<sieve.length();j+=i){
                    sieve.set(j);
                }
            }
        }
        return -1;
    }
    //TODO: Optimize the bitset (resize? only represent uneven numbers).
    //TODO: Add Miller-Raban primability test.
    //TODO: The big loop can terminate as soon as n/ i < last found prime
}