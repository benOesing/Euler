package euler;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Library {

	/**
	 * Using the Sieve of Eratosthenes to generate all primes to a specified limit.
	 *
	 * @param limit    The biggest possible prime that gets returned, excluded limit
	 *                 exactly.
	 * @param smallest The smallest prime that gets added to the list.
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

	/**
	 * @param number The number that gets turned into english
	 * @return British english representation of a number.
	 */
	public static String numberToEnglish(final long number) {
		if (number == 0) {
			return "zero";
		}
		if (number == 10) {
			return "ten";
		}
		if (number == 11) {
			return "eleven";
		}
		if (number == 12) {
			return "twelve";
		}
		if (number == 13) {
			return "thirteen";
		}
		if (number == 14) {
			return "fourteen";
		}
		if (number == 15) {
			return "fifteen";
		}
		if (number == 16) {
			return "sixteen";
		}
		if (number == 17) {
			return "seventeen";
		}
		if (number == 18) {
			return "eighteen";
		}
		if (number == 19) {
			return "nineteen";
		}
		final int length = Long.toString(number).length();
		String s = "";
		switch (length) {
			case 9:
			case 8:
			case 7:
				final long millions = Math.floorDiv(number, 1000000);
				if (number % 1000000 != 0) {
					s = numberToEnglish(millions) + " million " + numberToEnglish(number % 1000000);
				} else {
					s = numberToEnglish(millions) + " million ";
				}
				break;
			case 6:
			case 5:
			case 4:
				final long thousands = Math.floorDiv(number, 1000);
				if (number % 1000 != 0) {
					s = numberToEnglish(thousands) + " thousand, " + numberToEnglish(number % 1000);
				} else {
					s = numberToEnglish(thousands) + " thousand, ";
				}
				break;
			case 3:
				final long hundreds = Math.floorDiv(number, 100);
				if (number % 100 != 0) {
					s = numberToEnglish(hundreds) + " hundred and " + numberToEnglish(number % 100);
				} else {
					s = numberToEnglish(hundreds) + " hundred";
				}
				break;
			case 2:
				final long tens = Math.floorDiv(number, 10);
				if (tens == 2) {
					s = "twenty";
					break;
				}
				if (tens == 3) {
					s = "thirty";
					break;
				}
				if (tens == 4) {
					s = "forty";
					break;
				}
				if (tens == 5) {
					s = "fifty";
					break;
				}
				if (tens == 6) {
					s = "sixty";
					break;
				}
				if (tens == 7) {
					s = "seventy";
					break;
				}
				if (tens == 8) {
					s = "eighty";
					break;
				}
				if (tens == 9) {
					s = "ninety";
					break;
				}
				if (number % 10 != 0) {
					s += "-" + numberToEnglish(number % 10);
				}
				break;
			case 1:
				if (number == 1) {
					s = "one";
					break;
				}
				if (number == 2) {
					s = "two";
					break;
				}
				if (number == 3) {
					s = "three";
					break;
				}
				if (number == 4) {
					s = "four";
					break;
				}
				if (number == 5) {
					s = "five";
					break;
				}
				if (number == 6) {
					s = "six";
					break;
				}
				if (number == 7) {
					s = "seven";
					break;
				}
				if (number == 8) {
					s = "eight";
					break;
				}
				if (number == 9) {
					s = "nine";
					break;
				}
				break;
			default:
				System.out.println("Number to big.");
				break;
		}
		return s;
	}

	/**
	 * @param number The number that gets turned into roman digits.
	 * @return Returns the smallest correct roman representation of the given
	 *         number. So (IIII is not allowed while IV is)
	 */
	public static String asRomanNumber(int number) {
		final String[] order = new String[] { "I", "IV", "V", "IX", "X", "XL", "L", "XC", "C", "CD", "D", "CM", "M" };
		final int[] value = new int[] { 1, 4, 5, 9, 10, 40, 50, 90, 100, 400, 500, 900, 1000 };
		final StringBuilder out = new StringBuilder();
		int j = value.length - 1;
		while (number != 0) {
			for (int i = j; i >= 0; i--) {
				if (number >= value[i]) {
					number -= value[i];
					out.append(order[i]);
					break;
				} else {
					j = Math.min(j, i);
				}
			}
		}
		return out.toString();
	}

	/**
	 * @param n The upper value.
	 * @param k The lower value.
	 * @return Returns the binomial coefficient with n over k.
	 */
	public static BigInteger binomialCoefficient(final int n, final int k) {
		if (k > n) {
			return BigInteger.ZERO;
		}
		BigInteger x, fd, fn = null, fk = null;
		fd = BigInteger.ONE;
		x = BigInteger.ONE;
		for (int i = 1; i <= n; i++) {
			x = x.multiply(BigInteger.valueOf(i));
			if (i == k) {
				fk = x;
			}
			if (i == (n - k)) {
				fd = x;
			}
			if (i == n) {
				fn = x;
			}
		}
		fk = fk.multiply(fd);
		fn = fn.divide(fk);
		return fn;
	}

	/**
	 * @param a The first number.
	 * @param b The second number.
	 * @return Returns ab.
	 */
	public static long concatLongs(long a, final long b) {
		if (a < 0 || b < 0) {
			throw new IllegalArgumentException("Both long values have to be positive to be concatenated.");
		}
		if (digitsOfLong(a) + digitsOfLong(b) > 19) {
			throw new IllegalArgumentException(
					"The long values are to big to be concatenated and returned as long: " + a + "," + b + ".");
		}
		long c = b;
		while (c > 0) {
			a *= 10;
			c /= 10;
		}
		return a + b;
	}

	/**
	 * @param number The number to count digits from.
	 * @return Returns the amount of digits of the given number.
	 */
	public static int digitsOfLong(long number) {
		int digits = 1;
		if (number < 0) {
			number *= -1;
		}
		if (number >= 100000000) {
			digits += 8;
			number /= 100000000;
		}
		if (number >= 10000) {
			digits += 4;
			number /= 10000;
		}
		if (number >= 100) {
			digits += 2;
			number /= 100;
		}
		if (number >= 10) {
			digits += 1;
		}
		return digits;
	}

	public static int countDigits(BigInteger n) {
		if (n.equals(BigInteger.ZERO)) {
			return 1;
		}
		if (n.signum() == -1) {
			n = n.multiply(BigInteger.valueOf(-1));
		}
		final double baseConversion = Math.log(2) / Math.log(10);
		final int count = (int) (baseConversion * n.bitLength() + 1);
		if (BigInteger.TEN.pow(count - 1).compareTo(n) > 0) {
			return count - 1;
		}
		return count;
	}

	/**
	 * @see #digitSum(int)
	 * @see #digitSum(long)
	 * @param number BigInteger value to get the digit sum from.
	 * @return Sum of all individual integers forming a number.
	 */
	public static int digitSum(final BigInteger number) {
		return digitSum(number.toString());
	}

	/**
	 * @see #digitSum(int)
	 * @see #digitSum(BigInteger)
	 * @param number Long value to get the digit sum from.
	 * @return Sum of all individual integers forming a number.
	 */
	public static int digitSum(final long number) {
		return digitSum("" + number);
	}

	/**
	 * @see #digitSum(int)
	 * @see #digitSum(long)
	 * @see #digitSum(BigInteger)
	 * @param s String value to get the digit sum from.
	 * @return Sum of all individual integers forming a number.
	 */
	public static int digitSum(final String s) {
		int sum = 0;
		for (final Character c : s.toCharArray()) {
			sum += Character.getNumericValue(c);
		}
		return sum;
	}

	/**
	 * @param n Number that gets all proper divisors calculated
	 * @return Returns a list from 1 to n inclusive that contains all divisors.
	 */
	public static List<Integer> getDivisors(final int n) {
		if (n <= 0) {
			return new ArrayList<>();
		}
		long limit = (int) Math.sqrt(n);
		final List<Integer> divisor = new ArrayList<>();
		final double root = Math.sqrt(n);
		if ((root == Math.floor(root)) && !Double.isInfinite(root)) {
			divisor.add((int) root);
			limit--;
		}
		for (int i = 1; i <= limit; i++) {
			if (n % i == 0) {
				divisor.add(i);
				divisor.add(n / i);
			}
		}
		Collections.sort(divisor);
		return divisor;
	}

	/**
	 * @see #getInput(Scanner, String, List, int, int, boolean, String, double)
	 * @return Uses System.in to get input from console
	 * @throws IOException
	 */
	public static String getInput() throws IOException {
		final Scanner scanner = new Scanner(System.in);
		final String input = scanner.nextLine();
		scanner.close();
		return input;
	}

	/**
	 * @param s The string to get all permutations from.
	 * @return Returns all permutations of the given string.
	 */
	public static ArrayList<String> getPermutations(final String s) {
		return getPermutations("", s);
	}

	private static ArrayList<String> getPermutations(final String prefix, final String s) {
		final ArrayList<String> permutations = new ArrayList<>();
		final int n = s.length();
		if (n == 0) {
			permutations.add(prefix);
			return permutations;
		} else {
			for (int i = 0; i < n; i++) {
				permutations.addAll(getPermutations(prefix + s.charAt(i), s.substring(0, i) + s.substring(i + 1, n)));
			}
		}
		return permutations;
	}

	/**
	 * @param first  Number to compare.
	 * @param second Number to compare.
	 * @return Returns the greatest comman divisor.
	 * @see #ggT(BigInteger, BigInteger)
	 */
	public static long ggT(long first, long second) {
		// TODO MODULO STATT DIVISION.
		if (first == 0 || second == 0) {
			return first + second;
		}
		long rest;
		while (first % second != 0) {
			while (first >= second) {
				first -= second;
			}
			rest = first;
			first = second;
			second = rest;
		}
		return second;
	}

	/**
	 * @param number String to check.
	 * @return Boolean value representing if the input is a palindrom.
	 * @see #digitSum(long)
	 * @see #digitSum(BigInteger)
	 * @see #digitSum(String)
	 */
	public static boolean isPalindrom(final int number) {
		return isPalindrom("" + number);
	}

	/**
	 * @param number String to check.
	 * @return Boolean value representing if the input is a palindrom.
	 * @see #digitSum(int)
	 * @see #digitSum(BigInteger)
	 * @see #digitSum(String)
	 */
	public static boolean isPalindrom(final long number) {
		return isPalindrom("" + number);
	}

	/**
	 * @param s String to check.
	 * @return Boolean value representing if the input is a palindrom.
	 * @see #digitSum(int)
	 * @see #digitSum(long)
	 * @see #digitSum(BigInteger)
	 */
	public static boolean isPalindrom(final String s) {
		return s.equals(reverseString(s));
	}

	/**
	 * @param s String to reverse
	 * @return Reversed Input
	 */
	public static String reverseString(final String s) {
		return new StringBuilder(s).reverse().toString();
	}

	/**
	 * Calculates if a given string contains all numbers from 0-differentDigits.
	 *
	 * @param s
	 *            String to be checked
	 * @param withZero
	 *            Should the zero be considered.
	 * @return Is the Input a pandigital number.
	 */
	public static boolean isPandigital(final String s, final boolean withZero) {
		return isPandigital(Integer.parseInt(s), withZero);
	}

	/**
	 * Calculates if a given number contains all numbers from 0-differentDigits.
	 *
	 * @param s
	 *            String to be checked
	 * @param withZero
	 *            Should the zero be considered.
	 * @return Is the Input a pandigital number.
	 */
	public static boolean isPandigital(int n, final boolean withZero) {
		final boolean[] test = new boolean[10];
		int count = 0;
		if (!withZero) {
			test[0] = true;
		} else {
			count--;
		}
		while (n > 0) {
			count++;
			final int digit = n % 10;
			n /= 10;
			if (digit >= test.length || test[digit]) {
				return false;
			}
			test[digit] = true;
		}
		for (int i = 0; i <= count; i++) {
			if (!test[i]) {
				return false;
			}
		}
		return true;
	}

	public static boolean isPermutation(long n1, long n2) {
		final int[] digits = new int[10];
		while (n1 > 0) {
			digits[(int) (n1 % 10)]++;
			n1 /= 10;
		}
		while (n2 > 0) {
			digits[(int) (n2 % 10)]--;
			if (digits[(int) (n2 % 10)] < 0) {
				return false;
			}
			n2 /= 10;
		}
		return true;
	}

	/*
	 * http://www.mathblog.dk/files/euler/Problem60.cs
	 */
	public static boolean isPrime(final int n) {
		if (n <= 1) {
			return false;
		}
		if (n == 2) {
			return true;
		}
		if (n % 2 == 0) {
			return false;
		}
		if (n < 9) {
			return true;
		}
		if (n % 3 == 0) {
			return false;
		}
		if (n % 5 == 0) {
			return false;
		}

		final int[] ar = new int[] { 2, 3 };
		for (int i = 0; i < ar.length; i++) {
			if (witness(ar[i], n)) {
				return false;
			}
		}
		return true;
	}

		/*
	 * http://www.mathblog.dk/files/euler/Problem60.cs
	 */
	private static boolean witness(final int a, final int n) {
		int t = 0;
		int u = n - 1;
		while ((u & 1) == 0) {
			t++;
			u >>= 1;
		}
		long xi1 = modularExp(a, u, n);
		long xi2;

		for (int i = 0; i < t; i++) {
			xi2 = xi1 * xi1 % n;
			if ((xi2 == 1) && (xi1 != 1) && (xi1 != (n - 1))) {
				return true;
			}
			xi1 = xi2;
		}
		if (xi1 != 1) {
			return true;
		}
		return false;
	}

		/*
	 * http://www.mathblog.dk/files/euler/Problem60.cs
	 */
	private static long modularExp(final int a, final int b, final int n) {
		long d = 1;
		int k = 0;
		while ((b >> k) > 0) {
			k++;
		}
		for (int i = k - 1; i >= 0; i--) {
			d = d * d % n;
			if (((b >> i) & 1) > 0) {
				d = d * a % n;
			}
		}
		return d;
	}

	/**
	 * Uses a linearsearch to find the given Object in the array. The types have to be equal.
	 *
	 * @param array
	 *            The array that gets searched.
	 * @param obj
	 *            The object we want to find.
	 * @return Returns the position of obj in array, or -1 if not found.
	 */
	public static int linearSearch(final Object[] array, final Object obj) {
		for (int i = 0; i < array.length; i++) {
			final Object element = array[i];
			if (element.equals(obj)) {
				return i;
			}
		}
		return -1;
	}

	public static long[] listPhi(final int limit) {
		return listPhi(2, limit);
	}

	/**
	 * @param limit
	 *            The last value in the array is phi(limit).
	 * @param lowest
	 *            The first value in the array is phi(lowest)
	 * @return
	 */
	public static long[] listPhi(final int lowest, final int limit) {
		final long[] arr = new long[limit + 1];
		final List<Integer> primes = listPrimes((int) Math.sqrt(limit) + 1);
		for (int i = lowest; i < arr.length; i++) {
			if (arr[i] == 0) {
				final long[] factorization = primeFactorization(i, primes);
				long lastFactor = 0;
				final ArrayList<Long> uniqueFactors = new ArrayList<>();
				for (final Long l : factorization) {
					if (lastFactor != l) {
						uniqueFactors.add(l);
						lastFactor = l;
					}
				}
				arr[i] = i;
				for (final Long factor : uniqueFactors) {
					arr[i] *= (1 - 1.0 / factor);
				}
				final long x = arr[i];
				for (int j = 2; !uniqueFactors.isEmpty(); j++) {
					for (int k = 0; k < uniqueFactors.size(); k++) {
						final long multiple = (int) Math.pow(uniqueFactors.get(k), j);
						final long position = i * multiple;
						if (position > 0 && position < arr.length) {
							arr[(int) position] = x * multiple;
						} else {
							uniqueFactors.remove(k);
							k--;
						}
					}
				}
			}
		}
		return Arrays.copyOfRange(arr, lowest, arr.length);
	}

	/**
	 * @param n
	 * @return
	 */
	public static List<List<Long>> listPossibleMultiplicants(final int n) {
		final List<Long> factors = primeFactorizationAsList(n);
		return listPossibleMultiplicants(n, factors);
	}

		/**
	 * @param n
	 * @param primeFactors
	 * @return
	 */
	public static List<List<Long>> listPossibleMultiplicants(final int n, final List<Long> primeFactors) {
		final List<List<Long>> possibleMultiplicants = new ArrayList<>();
		possibleMultiplicants.add(primeFactors);
		final List<List<Long>> lastInsertedMultiplicants = new ArrayList<>();
		lastInsertedMultiplicants.add(primeFactors);
		int numberOfFactors = primeFactors.size();
		while (numberOfFactors > 2) {
			final Set<List<Long>> multiplicants = new HashSet<>();
			for (final List<Long> list : lastInsertedMultiplicants) {
				for (int i = 0; i < list.size(); i++) {
					final ArrayList<Long> copyList = new ArrayList<>(list);
					final long factor = copyList.remove(i);
					for (int j = 0; j < copyList.size(); j++) {
						final ArrayList<Long> multiplicant = new ArrayList<>(copyList);
						final long value = multiplicant.get(j);
						multiplicant.set(j, factor * value);
						Collections.sort(multiplicant);
						multiplicants.add(multiplicant);
					}
				}
			}
			lastInsertedMultiplicants.clear();
			lastInsertedMultiplicants.addAll(multiplicants);
			numberOfFactors--;
			possibleMultiplicants.addAll(multiplicants);
		}
		return possibleMultiplicants;
	}

	public static List<Long> primeFactorizationAsList(final long n) {
		final List<Integer> primes = listPrimes((int) (Math.sqrt(n) + 1));
		return primeFactorizationAsList(n, primes);
	}

		/**
	 *
	 * @param n
	 *            The number that gets factorized
	 * @param primes
	 *            An array of primes up to the square root of the number or
	 *            bigger.
	 * @return A list of the factors in increasing order.
	 */
	public static List<Long> primeFactorizationAsList(final long n, final List<Integer> primes) {
		final long[] factors = primeFactorization(n, primes);
		final List<Long> out = new ArrayList<>();
		for (final Long factor : factors) {
			out.add(factor);
		}
		return out;
	}

	/**
	 * Using the Sieve of Eratosthenes to generate all primes to a specified limit.
	 *
	 * @param limit The biggest possible prime that gets returned, excluded limit
	 *              exactly.
	 * @return A List of primes.
	 */
	public static List<Integer> listPrimes(final int limit) {
		return listPrimes(2, limit);
	}

	/**
	 * @param arr
	 *            The Array of objects that gets checked for duplicates. The
	 *            function checks the last index first.
	 * @return Returns true if there are no duplicates.
	 */
	public static boolean noDuplicates(final Object[] arr) {
		for (int i = arr.length - 1; i > 0; i--) {
			for (int j = i - 1; j >= 0; j--) {
				if (arr[i].equals(arr[j])) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Calculates The number of natural numbers that are relative prime to n.
	 *
	 * @param n
	 *            The Number to calculate phi for.
	 * @return {a € N|1 <= a <= n, ggT(a,n) = 1}
	 */
	public static long phi(int n) {
		final long[] factors = primeFactorization(n);
		long lastFactor = 0;
		for (final Long factor : factors) {
			if (lastFactor != factor) {
				n *= (1 - (1.0 / factor));
				lastFactor = factor;
			}
		}
		return n;
	}

	public static int pokerDecideWinner(final String string, final String string2, final String string3) {
		return 0;
	}

	/**
	 * Calculates the primefactorization of the input.
	 *
	 * @param n
	 *            The number that gets factorized
	 * @return An array of the factors in increasing order.
	 */
	public static long[] primeFactorization(final long n) {
		final List<Integer> primes = listPrimes((int) (Math.sqrt(n) + 1));
		return primeFactorization(n, primes);
	}

	

	/**
	 * Calculates the primefactorization of the input.
	 *
	 * @param n      The number that gets factorized
	 * @param primes An array of primes up to the square root of the number or
	 *               bigger.
	 * @return An array of the factors in increasing order.
	 */
	public static long[] primeFactorization(final long n, final List<Integer> primes) {
		final ArrayList<Integer> arrL = new ArrayList<>();
		long remain = n;
		for (int i = 0; i < primes.size() && remain > 1; i++) {
			while (remain % primes.get(i) == 0) {
				arrL.add(primes.get(i));
				remain = remain / primes.get(i);
			}
		}
		if (remain != 1) {
			arrL.add((int) remain);
		}
		if (arrL.size() == 0) {
			return new long[] { remain };
		}
		final long[] copy = new long[arrL.size()];
		for (int i = 0; i < copy.length; i++) {
			copy[i] = arrL.get(i);
		}
		return copy;
	}

	/**
	 * Solves a 9x9 sudoku by the traditional rules.
	 *
	 * @param board
	 *            A representation of the sudoku that is going to be solved.
	 * @see #solveSudoku(String)
	 * @return Returns a single solution to the given sudoku board.
	 */
	public static int[][] solveSudoku(final int[][] board) {
		final List<Integer> alreadyPlaced = new ArrayList<>();
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (board[i][j] != 0) {
					alreadyPlaced.add(9 * i + j);
				}
			}
		}
		// solveSudoku 031020000400060080000098040004001000710000052000600900040750000020030006000010370
		int filled = 0;
		while (filled >= 0 && filled < 81) {
			if (alreadyPlaced.contains(filled)) {
				filled++;
			} else {
				int h = Math.max(1, board[filled / 9][filled % 9]);
				while (h <= 9) {
					final boolean possibleToPlace = checkIfCorrect(board, filled, h);
					if (h <= 9 && possibleToPlace) {
						board[filled / 9][filled % 9] = h;
						filled++;
						break;
					}
					if (h == 9 && !possibleToPlace) {
						board[filled / 9][filled % 9] = 0;
						filled--;
						while (alreadyPlaced.contains(filled)) {
							filled--;
						}
						break;
					}
					h++;
				}
			}
		}
		return board;
	}

	/**
	 * Solves a 9x9 sudoku by the traditional rules.
	 *
	 * @see #solveSudoku(int[][] board)
	 * @param board
	 *            A String representation of the sudoku that is going to be
	 *            solved. Zeros should be placed in empty fields.
	 * @return Returns a single solution to the given sudoku board as a int
	 *         array 9x9.
	 */
	public static int[][] solveSudoku(final String board) {
		final int[][] newBoard = new int[9][9];
		char c;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				c = board.charAt(i * 9 + j);
				newBoard[i][j] = Character.getNumericValue(c);
			}
		}
		return solveSudoku(newBoard);
	}

		/**
	 * @param board
	 *            The Sodoku Board.
	 * @param pos
	 *            The position on the Board.
	 * @param value
	 *            The value (1-9) for given Position.
	 * @return Returns if the value can be inserted into given position.
	 */
	private static boolean checkIfCorrect(final int[][] board, final int pos, final int value) {
		for (int a = 0; a < 9; a++) {
			if (a != pos % 9 && board[pos / 9][a] == value) {
				return false;
			}
			if (9 * a + pos % 9 != pos && board[a][pos % 9] == value) {
				return false;
			}
		}
		return boxCheck(board, pos, value);
	}

		/**
	 * @param board
	 *            The Sodoku Board.
	 * @param pos
	 *            The position on the Board.
	 * @param value
	 *            The value (1-9) for given Position.
	 * @return Returns if the value already exists in the 3x3 box.
	 */
	private static boolean boxCheck(final int[][] board, final int pos, final int value) {
		int a, b;
		a = ((getBox(pos)) / 9);
		b = ((getBox(pos)) % 9);
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (board[a + i][b + j] == value) {
					return false;
				}
			}
		}
		return true;
	}

		/**
	 * @param pos
	 *            The position on the Board.
	 * @return The 3x3 box the value is in.
	 */
	private static int getBox(final int pos) {
		final int column = (pos / 9) / 3;
		final int row = (pos % 9) / 3;
		switch (column) {
		case 0: {
			switch (row) {
			case 0:
				return 0;

			case 1:
				return 3;

			case 2:
				return 6;
			}
		}
		case 1: {
			switch (row) {
			case 0:
				return 27;

			case 1:
				return 30;

			case 2:
				return 33;
			}
		}
		case 2: {
			switch (row) {
			case 0:
				return 54;

			case 1:
				return 57;

			case 2:
				return 60;
			}
		}
		}
		return 0;
	}

	/**
	 * @param n
	 *            BigDecimal to calculate the root.
	 * @param decimals
	 *            Number of decimals after the comma.
	 * @return BigDecimal approximation correct to given decimals.
	 */
	public static BigDecimal squareRoot(final BigDecimal n, final int decimals) {
		BigDecimal a = new BigDecimal(n.add(BigDecimal.valueOf(1)).divide(BigDecimal.valueOf(2)).toString());
		a.setScale(decimals + 5, RoundingMode.FLOOR);
		BigDecimal help = new BigDecimal(a.toString());
		do {
			help = new BigDecimal(a.toString());
			help.setScale(decimals + 5, RoundingMode.FLOOR);
			a = n.divide(help, decimals + 5, RoundingMode.FLOOR);
			a = a.add(help);
			a = a.divide(BigDecimal.valueOf(2), decimals + 5, RoundingMode.FLOOR);
		} while (!help.equals(a));
		return a.setScale(decimals, RoundingMode.FLOOR);
	}

}
