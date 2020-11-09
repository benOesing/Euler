package euler;

import java.awt.Point;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Benedikt[at]Oesing.de
 *
 */
public class Euler {

	public static void main(final String[] args)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
		System.out.println("Problem number please:");
		final String input = Library.getInput();
		final Euler e = new Euler();
		final Method[] methods = e.getClass().getMethods();
		if (input.equals("all") || input.equals("alle")) {
			Arrays.sort(methods, new Comparator<Method>() {
				@Override
				public int compare(final Method o1, final Method o2) {
					String s1 = o1.getName().replaceAll("\\D", "");
					if (s1.equals("")) {
						s1 = "0";
					}
					String s2 = o2.getName().replaceAll("\\D", "");
					if (s2.equals("")) {
						s2 = "0";
					}
					final int n1 = Integer.parseInt(s1);
					final int n2 = Integer.parseInt(s2);
					return Integer.compare(n1, n2);
				}
			});
			for (final Method m : methods) {
				if (!m.getName().contains("euler")) {
					continue;
				}
				final long time = System.currentTimeMillis();
				try{
					System.out.format("%8s%32s%14s%n", m.getName(), m.invoke(e), System.currentTimeMillis() - time + " ms");
				} catch(Exception ignore){
					System.out.println(" Exception thrown, probably file not found.");
				}
			}
		} else {
			Method f = null;
			for (final Method m : methods) {
				if (m.getName().equals("euler" + input)) {
					f = m;
					break;
				}
			}
			if (f == null) {
				System.out.println("Problem not solved yet.");
			} else {
				final long time = System.currentTimeMillis();
				try{
				System.out.println(f.invoke(e));
				} catch(Exception ignore){
					System.out.println(" Exception thrown, probably file not found.");
				}
				System.out.println("Algorithm took: " + (System.currentTimeMillis() - time) + " ms");
			}
		}
	}

	/**
	 * If we list all the natural numbers below 10 that are multiples of 3 or 5, we
	 * get 3, 5, 6 and 9. The sum of these multiples is 23.
	 *
	 * Find the sum of all the multiples of 3 or 5 below 1000.
	 */
	public String euler1() {
		long sum = 0;
		final long n = 999;
		final long limit3 = n / 3;
		final long limit5 = n / 5;
		final long limit15 = n / 15;
		final long threes = 3 * (limit3 * (limit3 + 1)) / 2;
		final long fives = 5 * (limit5 * (limit5 + 1)) / 2;
		final long both = 15 * (limit15 * (limit15 + 1)) / 2;
		sum = threes + fives - both;
		return Long.toString(sum);
	}

	/*
	 * The sum of the primes below 10 is 2 + 3 + 5 + 7 = 17.
	 *
	 * Find the sum of all the primes below two million.
	 */
	public String euler10() {
		final List<Integer> list = Library.listPrimes(2000000);
		long sum = 0;
		for (final Integer i : list) {
			sum += i;
		}
		return Long.toString(sum);
	}

	/*
	 * If a box contains twenty-one coloured discs, composed of fifteen blue discs
	 * and six red discs, and two discs were taken at random, it can be seen that
	 * the probability of taking two blue discs, P(BB) = (15/21)×(14/20) = 1/2.
	 *
	 * The next such arrangement, for which there is exactly 50% chance of taking
	 * two blue discs at random, is a box containing eighty-five blue discs and
	 * thirty-five red discs.
	 *
	 * By finding the first arrangement to contain over 10^12 = 1,000,000,000,000
	 * discs in total, determine the number of blue discs that the box would
	 * contain.
	 */
	public String euler100() {
		final long limit = 1000000000000L;
		long blue = 1;
		long combined = 1;
		while (combined < limit) {
			final long b = 3 * blue + 2 * combined - 2;
			final long n = 4 * blue + 3 * combined - 3;
			blue = b;
			combined = n;
		}
		return Long.toString(blue);
	}

	/*
	 * In the 20×20 grid below, four numbers along a diagonal line have been marked
	 * in red.
	 *
	 * The product of these numbers is 26 × 63 × 78 × 14 = 1788696.
	 *
	 * What is the greatest product of four adjacent numbers in the same direction
	 * (up, down, left, right, or diagonally) in the 20×20 grid?
	 */
	public String euler11() throws IOException {
		final List<String> lines = Files.readAllLines(Paths.get("11.txt"));
		final String s = lines.get(0);
		final String[] numbers = s.split(" ");
		final int[][] grid = new int[26][26];
		long max = 0;
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 20; j++) {
				grid[i + 3][j + 3] = Integer.parseInt(numbers[i * 20 + j]);
			}
		}
		for (int i = 3; i < 23; i++) {
			for (int j = 3; j < 23; j++) {
				final long up = grid[i][j] * grid[i - 1][j] * grid[i - 2][j] * grid[i - 3][j];
				final long down = grid[i][j] * grid[i + 1][j] * grid[i + 2][j] * grid[i + 3][j];
				final long left = grid[i][j] * grid[i][j - 1] * grid[i][j - 2] * grid[i][j - 3];
				final long right = grid[i][j] * grid[i][j + 1] * grid[i][j + 2] * grid[i][j + 3];
				final long diagonally1 = grid[i][j] * grid[i - 1][j - 1] * grid[i - 2][j - 2] * grid[i - 3][j - 3];
				final long diagonally2 = grid[i][j] * grid[i + 1][j + 1] * grid[i + 2][j + 2] * grid[i + 3][j + 3];
				final long diagonally3 = grid[i][j] * grid[i - 1][j + 1] * grid[i - 2][j + 2] * grid[i - 3][j + 3];
				final long diagonally4 = grid[i][j] * grid[i + 1][j - 1] * grid[i + 2][j - 2] * grid[i + 3][j - 3];
				max = Long.max(max, Long.max(up, Long.max(down, Long.max(left, Long.max(right,
						Long.max(diagonally1, Long.max(diagonally2, Long.max(diagonally3, diagonally4))))))));
			}
		}
		return Long.toString(max);
	}

	/*
	 * The sequence of triangle numbers is generated by adding the natural numbers.
	 * So the 7th triangle number would be 1 + 2 + 3 + 4 + 5 + 6 + 7 = 28. The first
	 * ten terms would be:
	 *
	 * 1, 3, 6, 10, 15, 21, 28, 36, 45, 55, ...
	 *
	 * Let us list the factors of the first seven triangle numbers:
	 *
	 * 1: 1 3: 1,3 6: 1,2,3,6 10: 1,2,5,10 15: 1,3,5,15 21: 1,3,7,21 28:
	 * 1,2,4,7,14,28 We can see that 28 is the first triangle number to have over
	 * five divisors.
	 *
	 * What is the value of the first triangle number to have over five hundred
	 * divisors?
	 */
	public String euler12() {
		int n = 0;
		final List<Integer> primes = Library.listPrimes(100000);
		for (int i = 1; i < Long.MAX_VALUE; i++) {
			n += i;
			final long[] factors = Library.primeFactorization(n, primes);
			int divisor = 1;
			long x = factors[0];
			int count = 1;
			for (int j = 1; j < factors.length; j++) {
				if (factors[j] == x) {
					count++;
				} else {
					divisor *= (count + 1);
					x = factors[j];
					count = 1;
				}
			}
			divisor *= (count + 1);
			if (divisor > 500) {
				return Integer.toString(n);
			}
		}
		return "Fail";
	}

	/*
	 * Work out the first ten digits of the sum of the following one-hundred
	 * 50-digit numbers.
	 */
	public String euler13() throws IOException {
		BigInteger x = new BigInteger("0");
		for (final String line : Files.readAllLines(Paths.get("./13.txt"))) {
			x = x.add(new BigInteger(line));
		}
		return x.toString().substring(0, 10);
	}

	/*
	 * The following iterative sequence is defined for the set of positive integers:
	 *
	 * n → n/2 (n is even) n → 3n + 1 (n is odd)
	 *
	 * Using the rule above and starting with 13, we generate the following
	 * sequence:
	 *
	 * 13 → 40 → 20 → 10 → 5 → 16 → 8 → 4 → 2 → 1 It can be seen that this sequence
	 * (starting at 13 and finishing at 1) contains 10 terms. Although it has not
	 * been proved yet (Collatz Problem), it is thought that all starting numbers
	 * finish at 1.
	 *
	 * Which starting number, under one million, produces the longest chain?
	 *
	 * NOTE: Once the chain starts the terms are allowed to go above one million.
	 */
	public String euler14() {
		final int limit = 1000000;
		final int[] steps = new int[limit];
		for (long i = 2; i < limit; i++) {
			final List<Long> sequence = new ArrayList<>();
			if (steps[Math.toIntExact(i)] == 0) {
				sequence.add(i);
				long n = i;
				while (n != 1) {
					if (n < steps.length && steps[Math.toIntExact(n)] != 0) {
						break;
					}
					if (n % 2 == 0) {
						n = n / 2;
						sequence.add(n);
					} else {
						n = 3 * n + 1;
						sequence.add(n);
					}
				}
				final int x = steps[Math.toIntExact(n)];
				int count = 0;
				while (!sequence.isEmpty()) {
					final long tail = sequence.get(sequence.size() - 1);
					sequence.remove(sequence.size() - 1);
					if (tail < steps.length) {
						steps[Math.toIntExact(tail)] = x + count;
					}
					count++;
				}
			}
		}
		long maxIndex = 0;
		long max = 0;
		for (int i = 0; i < steps.length; i++) {
			if (steps[i] > max) {
				max = steps[i];
				maxIndex = i;
			}
		}
		return Long.toString(maxIndex);
	}

	/*
	 * Starting in the top left corner of a 2×2 grid, and only being able to move to
	 * the right and down, there are exactly 6 routes to the bottom right corner.
	 *
	 *
	 * How many such routes are there through a 20×20 grid?
	 */
	public String euler15() {
		final int dimension = 20;
		final long[][] grid = new long[dimension + 1][dimension + 1];
		for (int i = 0; i < dimension + 1; i++) {
			for (int j = 0; j < dimension + 1; j++) {
				if (i == 0 && j == 0) {
					grid[i][j] = 1;
				} else if (i == 0) {
					grid[i][j] = grid[i][j - 1];
				} else if (j == 0) {
					grid[i][j] = grid[i - 1][j];
				} else {
					grid[i][j] = grid[i - 1][j] + grid[i][j - 1];
				}
			}
		}
		return Long.toString(grid[dimension][dimension]);
	}

	/*
	 * 2^15 = 32768 and the sum of its digits is 3 + 2 + 7 + 6 + 8 = 26.
	 *
	 * What is the sum of the digits of the number 2^1000?
	 */
	public String euler16() {
		BigInteger x = new BigInteger("2");
		x = x.pow(1000);
		return Integer.toString(Library.digitSum(x));
	}

	/*
	 * If the numbers 1 to 5 are written out in words: one, two, three, four, five,
	 * then there are 3 + 3 + 5 + 4 + 4 = 19 letters used in total.
	 *
	 * If all the numbers from 1 to 1000 (one thousand) inclusive were written out
	 * in words, how many letters would be used?
	 *
	 *
	 * NOTE: Do not count spaces or hyphens. For example, 342 (three hundred and
	 * forty-two) contains 23 letters and 115 (one hundred and fifteen) contains 20
	 * letters. The use of "and" when writing out numbers is in compliance with
	 * British usage.
	 */
	public String euler17() {
		int letters = 0;
		for (int i = 1; i <= 1000; i++) { 
			String s = Library.numberToEnglish(i);
			s = s.replaceAll("[ -]", "");
			letters += s.length();
		}
		return Integer.toString(letters);
	}

	/*
	 * By starting at the top of the triangle below and moving to adjacent numbers
	 * on the row below, the maximum total from top to bottom is 23.
	 *
	 * That is, 3 + 7 + 4 + 9 = 23.
	 *
	 * Find the maximum total from top to bottom of the triangle below:
	 *
	 */
	public String euler18() throws IOException {
		final List<String> lines = Files.readAllLines(Paths.get("./18.txt"));
		final int size = lines.size();
		final long[][] grid = new long[size][size];
		for (int i = 0; i < size; i++) {
			final String[] numbers = lines.get(i).split(" ");
			for (int j = 0; j < numbers.length; j++) {
				grid[i][j] += Integer.parseInt(numbers[j]);
				if (i != 0) {
					if (j == 0) {
						grid[i][j] += grid[i - 1][j];
					} else {
						grid[i][j] += Long.max(grid[i - 1][j], grid[i - 1][j - 1]);
					}
				}
			}
		}
		long max = 0;
		for (int i = 0; i < size; i++) {
			max = Long.max(max, grid[size - 1][i]);
		}
		return Long.toString(max);
	}

	/*
	 * You are given the following information, but you may prefer to do some
	 * research for yourself.
	 *
	 * 1 Jan 1900 was a Monday. Thirty days has September, April, June and November.
	 * All the rest have thirty-one, Saving February alone, Which has twenty-eight,
	 * rain or shine. And on leap years, twenty-nine. A leap year occurs on any year
	 * evenly divisible by 4, but not on a century unless it is divisible by 400.
	 * How many Sundays fell on the first of the month during the twentieth century
	 * (1 Jan 1901 to 31 Dec 2000)?
	 */
	public String euler19() {
		int count = 0;
		for (int year = 1901; year <= 2000; year++) {
			for (int month = 1; month <= 12; month++) {
				final LocalDate date = LocalDate.of(year, month, 1);
				if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
					count++;
				}
			}
		}
		return Integer.toString(count);
	}

	/**
	 * Each new term in the Fibonacci sequence is generated by adding the previous
	 * two terms. By starting with 1 and 2, the first 10 terms will be:
	 *
	 * 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, ...
	 *
	 * By considering the terms in the Fibonacci sequence whose values do not exceed
	 * four million, find the sum of the even-valued terms.
	 */
	public String euler2() {
		long max = 4000000;
		max = 100;
		long a = 1;
		long b = 2;
		long sum = 2;
		while (a + b <= max) {
			if (a < b) {
				a = a + b;
				if (a % 2 == 0) {
					sum += a;
				}
			} else {
				b = b + a;
				if (b % 2 == 0) {
					sum += b;
				}
			}
		}
		return Long.toString(sum);
	}

	/*
	 * n! means n × (n − 1) × ... × 3 × 2 × 1
	 *
	 * For example, 10! = 10 × 9 × ... × 3 × 2 × 1 = 3628800, and the sum of the
	 * digits in the number 10! is 3 + 6 + 2 + 8 + 8 + 0 + 0 = 27.
	 *
	 * Find the sum of the digits in the number 100!
	 */
	public String euler20() {
		BigInteger x = new BigInteger("1");
		for (long i = 2; i <= 100; i++) {
			x = x.multiply(BigInteger.valueOf(i));
		}
		return Integer.toString(Library.digitSum(x));
	}

	/*
	 * Let d(n) be defined as the sum of proper divisors of n (numbers less than n
	 * which divide evenly into n). If d(a) = b and d(b) = a, where a ≠ b, then a
	 * and b are an amicable pair and each of a and b are called amicable numbers.
	 *
	 * For example, the proper divisors of 220 are 1, 2, 4, 5, 10, 11, 20, 22, 44,
	 * 55 and 110; therefore d(220) = 284. The proper divisors of 284 are 1, 2, 4,
	 * 71 and 142; so d(284) = 220.
	 *
	 * Evaluate the sum of all the amicable numbers under 10000.
	 */
	public String euler21() {
		final int[] divisorSum = new int[10000];
		int sum = 0;
		for (int i = 1; i < 10000; i++) {
			final ArrayList<Integer> divisor = new ArrayList<>();
			for (int j = 1; j < i / 2 + 1; j++) {
				if (i % j == 0) {
					divisor.add(j);
				}
			}
			divisorSum[i] = Integer.min(10000 - 1, divisor.stream().mapToInt(Integer::intValue).sum());
		}
		for (int i = 0; i < divisorSum.length; i++) {
			final int x = divisorSum[i];
			if (divisorSum[x] == i && i != divisorSum[i]) {
				sum += x;
				sum += divisorSum[x];
				divisorSum[x] = 0;
			}
		}
		return Integer.toString(sum);
	}

	/*
	 * Using names.txt (right click and 'Save Link/Target As...'), a 46K text file
	 * containing over five-thousand first names, begin by sorting it into
	 * alphabetical order. Then working out the alphabetical value for each name,
	 * multiply this value by its alphabetical position in the list to obtain a name
	 * score.
	 *
	 * For example, when the list is sorted into alphabetical order, COLIN, which is
	 * worth 3 + 15 + 12 + 9 + 14 = 53, is the 938th name in the list. So, COLIN
	 * would obtain a score of 938 × 53 = 49714.
	 *
	 * What is the total of all the name scores in the file?
	 */
	public String euler22() throws IOException {
		long sum = 0;
		final String s = Files.readAllLines(Paths.get("./22.txt")).get(0);
		final String[] names = s.replaceAll("\"", "").split(",");
		Arrays.sort(names);
		for (int i = 0; i < names.length; i++) {
			int score = 0;
			final String name = names[i];
			final char[] chars = name.toCharArray();
			for (final Character c : chars) {
				score += (int) c - 64;
			}
			sum += (i + 1) * score;
		}
		return Long.toString(sum);
	}

	/*
	 * A perfect number is a number for which the sum of its proper divisors is
	 * exactly equal to the number. For example, the sum of the proper divisors of
	 * 28 would be 1 + 2 + 4 + 7 + 14 = 28, which means that 28 is a perfect number.
	 *
	 * A number n is called deficient if the sum of its proper divisors is less than
	 * n and it is called abundant if this sum exceeds n.
	 *
	 * As 12 is the smallest abundant number, 1 + 2 + 3 + 4 + 6 = 16, the smallest
	 * number that can be written as the sum of two abundant numbers is 24. By
	 * mathematical analysis, it can be shown that all integers greater than 28123
	 * can be written as the sum of two abundant numbers. However, this upper limit
	 * cannot be reduced any further by analysis even though it is known that the
	 * greatest number that cannot be expressed as the sum of two abundant numbers
	 * is less than this limit.
	 *
	 * Find the sum of all the positive integers which cannot be written as the sum
	 * of two abundant numbers.
	 */
	public String euler23() {
		final List<Integer> abundant = new ArrayList<>();
		final int limit = 28123;
		for (int i = 1; i <= limit; i++) {
			int sum = 0;
			for (int j = 1; j < i / 2 + 1 && sum <= i; j++) {
				if (i % j == 0) {
					sum += j;
					if (sum > i) {
						abundant.add(i);
					}
				}
			}
		}
		final Set<Integer> set = new HashSet<>();
		for (int i = 0; i < abundant.size(); i++) {
			for (int j = i; j < abundant.size(); j++) {
				final int x = abundant.get(i);
				final int y = abundant.get(j);
				if (x + y <= limit) {
					set.add(x + y);
				}
			}
		}
		long sum = 0;
		for (int i = 1; i <= limit; i++) {
			if (!set.contains(i)) {
				sum += i;
			}
		}
		return Long.toString(sum);
	}

	/*
	 * A permutation is an ordered arrangement of objects. For example, 3124 is one
	 * possible permutation of the digits 1, 2, 3 and 4. If all of the permutations
	 * are listed numerically or alphabetically, we call it lexicographic order. The
	 * lexicographic permutations of 0, 1 and 2 are:
	 *
	 * 012 021 102 120 201 210
	 *
	 * What is the millionth lexicographic permutation of the digits 0, 1, 2, 3, 4,
	 * 5, 6, 7, 8 and 9?
	 */
	public String euler24() {
		int count = 0;
		for (int a = 0; a <= 9; a++) {
			for (int b = 0; b <= 9; b++) {
				if (b == a) {
					continue;
				}
				for (int c = 0; c <= 9; c++) {
					if (c == b || c == a) {
						continue;
					}
					for (int d = 0; d <= 9; d++) {
						if (d == c || d == b || d == a) {
							continue;
						}
						for (int e = 0; e <= 9; e++) {
							if (e == d || e == c || e == b || e == a) {
								continue;
							}
							for (int f = 0; f <= 9; f++) {
								if (f == e || f == d || f == c || f == b || f == a) {
									continue;
								}
								for (int g = 0; g <= 9; g++) {
									if (g == f || g == e || g == d || g == c || g == b || g == a) {
										continue;
									}
									for (int h = 0; h <= 9; h++) {
										if (h == g || h == f || h == e || h == d || h == c || h == b || h == a) {
											continue;
										}
										for (int i = 0; i <= 9; i++) {
											if (i == h || i == g || i == f || i == e || i == d || i == c || i == b
													|| i == a) {
												continue;
											}
											for (int j = 0; j <= 9; j++) {
												if (j == i || j == h || j == g || j == f || j == e || j == d || j == c
														|| j == b || j == a) {
													continue;
												}
												count++;
												if (count == 1000000) {
													return "" + a + b + c + d + e + f + g + h + i + j;
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return "Fail";
	}

	/*
	 * What is the index of the first term in the Fibonacci sequence to contain 1000
	 * digits?
	 */
	public String euler25() {
		long count = 3;
		BigInteger a = new BigInteger("1");
		BigInteger b = new BigInteger("2");
		for (;;) {
			if (a.compareTo(b) < 0) {
				a = a.add(b);
				count++;
				if (a.toString().length() == 1000) {
					return Long.toString(count);
				}

			} else {
				count++;
				b = b.add(a);
			}
		}
	}

	/*
	 * A unit fraction contains 1 in the numerator. The decimal representation of
	 * the unit fractions with denominators 2 to 10 are given:
	 *
	 * 1/2 = 0.5 1/3 = 0.(3) 1/4 = 0.25 1/5 = 0.2 1/6 = 0.1(6) 1/7 = 0.(142857) 1/8
	 * = 0.125 1/9 = 0.(1) 1/10 = 0.1 Where 0.1(6) means 0.166666..., and has a
	 * 1-digit recurring cycle. It can be seen that 1/7 has a 6-digit recurring
	 * cycle.
	 *
	 * Find the value of d < 1000 for which 1/d contains the longest recurring cycle
	 * in its decimal fraction part.
	 */
	public String euler26() throws IOException {
		int sequenceLength = 0;

		for (int i = 1000; i > 1; i--) {
			final int[] foundRemainders = new int[i];
			int value = 1;
			int position = 0;
			while (foundRemainders[value] == 0 && value != 0) {
				foundRemainders[value] = position;
				value *= 10;
				value %= i;
				position++;
			}
			if (position - foundRemainders[value] > sequenceLength) {
				sequenceLength = position - foundRemainders[value];
			}
		}
		return Integer.toString(sequenceLength);
	}

	/*
	 * Euler discovered the remarkable quadratic formula:
	 *
	 * n2+n+41n2+n+41 It turns out that the formula will produce 40 primes for the
	 * consecutive integer values 0≤n≤390≤n≤39. However, when
	 * n=40,402+40+41=40(40+1)+41n=40,402+40+41=40(40+1)+41 is divisible by 41, and
	 * certainly when n=41,412+41+41n=41,412+41+41 is clearly divisible by 41.
	 *
	 * The incredible formula n2−79n+1601n2−79n+1601 was discovered, which produces
	 * 80 primes for the consecutive values 0≤n≤790≤n≤79. The product of the
	 * coefficients, −79 and 1601, is −126479.
	 *
	 * Considering quadratics of the form:
	 *
	 * n2+an+bn2+an+b, where |a|<1000|a|<1000 and |b|≤1000|b|≤1000
	 *
	 * where |n||n| is the modulus/absolute value of nn e.g. |11|=11|11|=11 and
	 * |−4|=4|−4|=4 Find the product of the coefficients, aa and bb, for the
	 * quadratic expression that produces the maximum number of primes for
	 * consecutive values of nn, starting with n=0n=0.
	 */
	public String euler27() {
		int max = 0;
		int product = 0;
		for (int a = -999; a < 1000; a++) {
			for (int b = -1000; b < 1000; b++) {
				int n = 0;
				int count = 0;
				while ((n * n + a * n + b) > 0 && Library.isPrime(n * n + a * n + b)) {
					n++;
					count++;
				}
				if (count > max) {
					max = count;
					product = a * b;
				}
			}
		}
		return Integer.toString(product);
	}

	/*
	 * Starting with the number 1 and moving to the right in a clockwise direction a
	 * 5 by 5 spiral is formed as follows:
	 *
	 * It can be verified that the sum of the numbers on the diagonals is 101.
	 *
	 * What is the sum of the numbers on the diagonals in a 1001 by 1001 spiral
	 * formed in the same way?
	 */
	public String euler28() {
		final int size = 1001;
		int result = 1;
		for (int i = 3; i <= size; i += 2) {
			final int x = i * i;
			result += x;
			result += x - (i - 1);
			result += x - 2 * (i - 1);
			result += x - 3 * (i - 1);
		}
		return Integer.toString(result);
	}

	/*
	 * Consider all integer combinations of ab for 2 ≤ a ≤ 5 and 2 ≤ b ≤ 5:
	 *
	 * 22=4, 23=8, 24=16, 25=32 32=9, 33=27, 34=81, 35=243 42=16, 43=64, 44=256,
	 * 45=1024 52=25, 53=125, 54=625, 55=3125 If they are then placed in numerical
	 * order, with any repeats removed, we get the following sequence of 15 distinct
	 * terms:
	 *
	 * 4, 8, 9, 16, 25, 27, 32, 64, 81, 125, 243, 256, 625, 1024, 3125
	 *
	 * How many distinct terms are in the sequence generated by ab for 2 ≤ a ≤ 100
	 * and 2 ≤ b ≤ 100?
	 */
	public String euler29() {
		final Set<BigInteger> set = new HashSet<>();
		for (int a = 2; a <= 100; a++) {
			for (int b = 2; b <= 100; b++) {
				final BigInteger x = new BigInteger("" + a);
				set.add(x.pow(b));
			}
		}
		return Integer.toString(set.size());
	}

	/*
	 * The prime factors of 13195 are 5, 7, 13 and 29.
	 *
	 * What is the largest prime factor of the number 600851475143 ?
	 */
	public String euler3() {
		final long n = 600851475143l;
		final long[] factors = Library.primeFactorization(n);
		return Long.toString(factors[factors.length - 1]);
	}

	/*
	 * Find the sum of all the numbers that can be written as the sum of fifth
	 * powers of their digits.
	 */
	public String euler30() {
		long sum = 0;
		final int exponent = 5;
		for (int i = 2; i < 1000000; i++) {
			long digitSum = 0;
			final char[] digits = Integer.toString(i).toCharArray();
			for (int j = 0; j < digits.length && digitSum <= i; j++) {
				digitSum += Math.pow(Character.digit(digits[j], 10), exponent);
			}
			if (digitSum == i) {
				sum += i;
			}
		}
		return Long.toString(sum);
	}

	/*
	 * In England the currency is made up of pound, £, and pence, p, and there are
	 * eight coins in general circulation:
	 *
	 * 1p, 2p, 5p, 10p, 20p, 50p, £1 (100p) and £2 (200p). It is possible to make £2
	 * in the following way:
	 *
	 * 1×£1 + 1×50p + 2×20p + 1×5p + 1×2p + 3×1p How many different ways can £2 be
	 * made using any number of coins?
	 */
	public String euler31() {
		int count = 0;
		final int target = 200;
		for (int a = 0; a * 1 <= target; a++) {
			for (int b = 0; b * 2 + a <= target; b++) {
				for (int c = 0; c * 5 + b * 2 + a <= target; c++) {
					for (int d = 0; d * 10 + c * 5 + b * 2 + a <= target; d++) {
						for (int e = 0; e * 20 + d * 10 + c * 5 + b * 2 + a <= target; e++) {
							for (int f = 0; f * 50 + e * 20 + d * 10 + c * 5 + b * 2 + a <= target; f++) {
								for (int g = 0; g * 100 + f * 50 + e * 20 + d * 10 + c * 5 + b * 2 + a <= target; g++) {
									for (int h = 0; h * 200 + g * 100 + f * 50 + e * 20 + d * 10 + c * 5 + b * 2
											+ a <= target; h++) {
										if (a * 1 + 2 * b + 5 * c + 10 * d + 20 * e + 50 * f + 100 * g
												+ 200 * h == target) {
											count++;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return Integer.toString(count);
	}

	/*
	 * We shall say that an n-digit number is pandigital if it makes use of all the
	 * digits 1 to n exactly once; for example, the 5-digit number, 15234, is 1
	 * through 5 pandigital.
	 *
	 * The product 7254 is unusual, as the identity, 39 × 186 = 7254, containing
	 * multiplicand, multiplier, and product is 1 through 9 pandigital.
	 *
	 * Find the sum of all products whose multiplicand/multiplier/product identity
	 * can be written as a 1 through 9 pandigital.
	 *
	 * HINT: Some products can be obtained in more than one way so be sure to only
	 * include it once in your sum.
	 */
	public String euler32() {
		final Set<Long> products = new HashSet<>();
		for (int i = 1; i < 100; i++) {
			for (int j = i + 1; i * j < 10000; j++) {
				final long x = i * j;
				final String s = "" + i + j + x;
				if (!s.contains("0") && Library.isPandigital(s, false)) {
					products.add(x);
				}
			}
		}
		return Long.toString(products.stream().mapToLong(Long::longValue).sum());
	}

	/*
	 * The fraction 49/98 is a curious fraction, as an inexperienced mathematician
	 * in attempting to simplify it may incorrectly believe that 49/98 = 4/8, which
	 * is correct, is obtained by cancelling the 9s.
	 *
	 * We shall consider fractions like, 30/50 = 3/5, to be trivial examples.
	 *
	 * There are exactly four non-trivial examples of this type of fraction, less
	 * than one in value, and containing two digits in the numerator and
	 * denominator.
	 *
	 * If the product of these four fractions is given in its lowest common terms,
	 * find the value of the denominator.
	 */
	public String euler33() {
		double product = 1.0;
		for (int nominator = 10; nominator < 100; nominator++) {
			for (int denominator = nominator + 1; denominator < 100; denominator++) {
				final double x = (1.0 * nominator) / (1.0 * denominator);
				final String n = Integer.toString(nominator);
				String d = Integer.toString(denominator);
				if (d.contains("0") || n.contains("0")) {
					continue;
				}
				if (d.contains("" + n.charAt(0))) {
					d = d.replaceFirst("" + n.charAt(0), "");
					final double y = Character.digit(n.charAt(1), 10) / Integer.parseInt(d);
					if (x == y) {
						product *= y;
					}
				} else if (d.contains("" + n.charAt(1))) {
					d = d.replaceFirst("" + n.charAt(1), "");
					final double y = (1.0 * Character.digit(n.charAt(0), 10)) / (1.0 * Integer.parseInt(d));
					if (x == y) {
						product *= y;
					}
				}
			}
		}
		return Double.toString(Math.round(1.0 / product));
	}

	public String euler34() {
		final long upperBound = 2540160; // x*9! < 10^7 Integer solution
		final int[] facs = new int[10];
		facs[0] = 1;
		for (int i = 1; i < facs.length; i++) {
			facs[i] = facs[i - 1] * i;
		}
		long sum = 0;
		for (int i = 3; i < upperBound; i++) {
			final char[] digits = Integer.toString(i).toCharArray();
			long factSum = 0;
			for (final Character digit : digits) {
				final int x = Character.digit(digit, 10);
				factSum += facs[x];
				if (factSum > i) {
					break;
				}
			}
			if (factSum == i) {
				sum += i;
			}
		}
		return Long.toString(sum);
	}

	/*
	 * The number, 197, is called a circular prime because all rotations of the
	 * digits: 197, 971, and 719, are themselves prime.
	 *
	 * There are thirteen such primes below 100: 2, 3, 5, 7, 11, 13, 17, 31, 37, 71,
	 * 73, 79, and 97.
	 *
	 * How many circular primes are there below one million?
	 */
	public String euler35() {
		final int limit = 1000000;
		int count = 0;
		final List<Integer> primes = Library.listPrimes(limit);
		for (Integer prime : primes) {
			boolean isCircular = true;
			final int digits = prime.toString().length();
			for (int i = 0; i < digits - 1; i++) {
				final long last = prime % 10;
				prime = prime / 10;
				prime = (int) (prime + last * Math.pow(10, digits - 1));
				if (Collections.binarySearch(primes, prime) <= 0) {
					isCircular = false;
					break;
				}
			}
			if (isCircular) {
				count++;
			}
		}
		return Integer.toString(count);
	}

	/*
	 * The decimal number, 585 = 10010010012 (binary), is palindromic in both bases.
	 *
	 * Find the sum of all numbers, less than one million, which are palindromic in
	 * base 10 and base 2.
	 *
	 * (Please note that the palindromic number, in either base, may not include
	 * leading zeros.)
	 */
	public String euler36() {
		long sum = 0;
		for (int i = 1; i < 1000000; i += 2) {
			if (Library.isPalindrom(i) && Library.isPalindrom(Integer.toString(i, 2))) {
				sum += i;
			}
		}
		return Long.toString(sum);
	}

	/*
	 * The number 3797 has an interesting property. Being prime itself, it is
	 * possible to continuously remove digits from left to right, and remain prime
	 * at each stage: 3797, 797, 97, and 7. Similarly we can work from right to
	 * left: 3797, 379, 37, and 3.
	 *
	 * Find the sum of the only eleven primes that are both truncatable from left to
	 * right and right to left.
	 *
	 * NOTE: 2, 3, 5, and 7 are not considered to be truncatable primes.
	 */
	public String euler37() {
		long sum = 0;
		final List<Integer> primes = Library.listPrimes(1000000);
		for (int i = primes.size() - 1; i > 3; i--) {
			final int prime = primes.get(i);
			boolean truncatable = true;
			if (prime > 10) {
				for (int digits = Long.toString(prime).length() - 1; digits > 0; digits--) {
					final int x = Collections.binarySearch(primes, prime % (int) Math.pow(10, digits));
					final int y = Collections.binarySearch(primes, prime / (int) Math.pow(10, digits));
					if (x < 0 || y < 0) {
						truncatable = false;
						break;
					}
				}
				if (truncatable) {
					sum += prime;
				}
			}
		}
		return Long.toString(sum);
	}

	/*
	 * Take the number 192 and multiply it by each of 1, 2, and 3:
	 *
	 * 192 × 1 = 192 192 × 2 = 384 192 × 3 = 576 By concatenating each product we
	 * get the 1 to 9 pandigital, 192384576. We will call 192384576 the concatenated
	 * product of 192 and (1,2,3)
	 *
	 * The same can be achieved by starting with 9 and multiplying by 1, 2, 3, 4,
	 * and 5, giving the pandigital, 918273645, which is the concatenated product of
	 * 9 and (1,2,3,4,5).
	 *
	 * What is the largest 1 to 9 pandigital 9-digit number that can be formed as
	 * the concatenated product of an integer with (1,2, ... , n) where n > 1?
	 */
	public String euler38() {
		long max = 0;
		for (int big = 1; big < 27161; big++) {
			String s = "";
			for (int small = 1;; small++) {
				if (s.length() > 9) {
					break;
				} else if (s.length() < 9) {
					s += Integer.toString(big * small);
				} else if (s.length() == 9) {
					if (Library.isPandigital(s, false)) {
						max = Long.max(max, Long.parseLong(s));
					}
					break;
				}
			}
		}
		return Long.toString(max);
	}

	/*
	 * If p is the perimeter of a right angle triangle with integral length sides,
	 * {a,b,c}, there are exactly three solutions for p = 120.
	 *
	 * {20,48,52}, {24,45,51}, {30,40,50}
	 *
	 * For which value of p ≤ 1000, is the number of solutions maximised? X=u*u
	 * -v*v,Y=2uv,Z=u*u+v*v => p = 2u(u+v)
	 * https://de.wikipedia.org/wiki/Pythagoreisches_Tripel
	 */
	public String euler39() {
		long max = 0;
		int bestIndex = 0;
		for (int p = 1; p <= 1000; p++) {
			int count = 0;
			for (int a = 1; a < p / 3; a++) {
				for (int b = a + 1; b < p - a; b++) {
					final int c = p - a - b;
					if (a * a + b * b == c * c) {
						count++;
					}
				}
			}
			if (count > max) {
				max = count;
				bestIndex = p;
			}
		}
		return Long.toString(bestIndex);
	}

	/*
	 * A palindromic number reads the same both ways. The largest palindrome made
	 * from the product of two 2-digit numbers is 9009 = 91 × 99.
	 *
	 * Find the largest palindrome made from the product of two 3-digit numbers.
	 */
	public String euler4() {
		long max = 0;
		for (int i = 999; i * 999 > max && i > 100; i--) {
			for (int j = 999; i * j > max && j > 100; j--) {
				final long product = i * j;
				if (Library.isPalindrom(product)) {
					max = Long.max(max, product);
					break;
				}
			}
		}
		return Long.toString(max);
	}

	/*
	 * An irrational decimal fraction is created by concatenating the positive
	 * integers:
	 *
	 * 0.123456789101112131415161718192021...
	 *
	 * It can be seen that the 12th digit of the fractional part is 1.
	 *
	 * If dn represents the nth digit of the fractional part, find the value of the
	 * following expression.
	 *
	 * d1 × d10 × d100 × d1000 × d10000 × d100000 × d1000000
	 */
	public String euler40() {
		long product = 1;
		final StringBuilder sb = new StringBuilder();
		int n = 1;
		while (sb.length() < 1000000) {
			sb.append(n);
			n++;
		}
		final String s = sb.toString();
		for (int i = 0; i <= 6; i++) {
			final int x = Character.digit(s.charAt((int) Math.pow(10, i) - 1), 10);
			product *= x;
		}
		return Long.toString(product);
	}

	/*
	 * We shall say that an n-digit number is pandigital if it makes use of all the
	 * digits 1 to n exactly once. For example, 2143 is a 4-digit pandigital and is
	 * also prime.
	 *
	 * What is the largest n-digit pandigital prime that exists?
	 */
	public String euler41() {
		final int limit = 987654321;
		String s = Integer.toString(limit);
		while (s.length() > 0) {
			final ArrayList<String> permutations = Library.getPermutations(s);
			for (final String permutation : permutations) {
				final int n = Integer.parseInt(permutation);
				if (Library.isPrime(n)) {
					return Integer.toString(n);
				}
			}
			s = s.substring(1);
		}
		return "";
	}

	/*
	 * The nth term of the sequence of triangle numbers is given by, tn = ½n(n+1);
	 * so the first ten triangle numbers are:
	 *
	 * 1, 3, 6, 10, 15, 21, 28, 36, 45, 55, ...
	 *
	 * By converting each letter in a word to a number corresponding to its
	 * alphabetical position and adding these values we form a word value. For
	 * example, the word value for SKY is 19 + 11 + 25 = 55 = t10. If the word value
	 * is a triangle number then we shall call the word a triangle word.
	 *
	 * Using words.txt (right click and 'Save Link/Target As...'), a 16K text file
	 * containing nearly two-thousand common English words, how many are triangle
	 * words?
	 */
	public String euler42() throws IOException {
		final int[] triangle = new int[10000];
		for (int i = 1; i < triangle.length; i++) {
			triangle[i] = (int) (0.5 * i * (i + 1));
		}
		int count = 0;
		final String[] words = Files.readAllLines(Paths.get("./42.txt")).get(0).replaceAll("\"", "").split(",");
		Arrays.sort(words);
		for (int i = 0; i < words.length; i++) {
			int score = 0;
			final String name = words[i];
			final char[] chars = name.toCharArray();
			for (final Character c : chars) {
				score += (int) c - 64;
			}
			if (Arrays.binarySearch(triangle, score) > 0) {
				count++;
			}
		}
		return Integer.toString(count);
	}

	public String euler43() {
		@SuppressWarnings("unchecked")
		final List<String>[] lists = new ArrayList[7];
		final int[] divisor = new int[] { 17, 13, 11, 7, 5, 3, 2 };
		for (int i = 0; i < lists.length; i++) {
			lists[i] = new ArrayList<>();
		}
		for (int i = 10; i < 987; i++) {
			if (i % divisor[0] == 0) {
				final String s = "" + i;
				if (i < 100 && s.charAt(0) != s.charAt(1)) {
					lists[0].add("0" + i);
				} else if (s.charAt(0) != s.charAt(1) && s.charAt(0) != s.charAt(2)) {
					lists[0].add("" + i);
				}
			}
		}
		for (int i = 0; i < lists.length - 1; i++) {
			for (final String s : lists[i]) {
				final String prefix = s.substring(0, 2);
				for (int j = 0; j < 10; j++) {
					final String digit = "" + j;
					if (!s.contains(digit)) {
						final String test = digit + prefix;
						if (Integer.parseInt(test) % divisor[i + 1] == 0) {
							final String insert = digit + s;
							lists[i + 1].add(insert);
						}
					}
				}
			}
		}
		long sum = 0;
		for (final String s : lists[6]) {
			long n = Long.parseLong(s);
			final int insert = 45 - Library.digitSum(n);
			n += 1000000000l * insert;
			sum += n;
		}
		return Long.toString(sum);
	}

	/*
	 * The number, 1406357289, is a 0 to 9 pandigital number because it is made up
	 * of each of the digits 0 to 9 in some order, but it also has a rather
	 * interesting sub-string divisibility property.
	 *
	 * Let d1 be the 1st digit, d2 be the 2nd digit, and so on. In this way, we note
	 * the following:
	 *
	 * d2d3d4=406 is divisible by 2 d3d4d5=063 is divisible by 3 d4d5d6=635 is
	 * divisible by 5 d5d6d7=357 is divisible by 7 d6d7d8=572 is divisible by 11
	 * d7d8d9=728 is divisible by 13 d8d9d10=289 is divisible by 17 Find the sum of
	 * all 0 to 9 pandigital numbers with this property.
	 */

	/*
	 * Pentagonal numbers are generated by the formula, Pn=n(3n−1)/2. The first ten
	 * pentagonal numbers are:
	 *
	 * 1, 5, 12, 22, 35, 51, 70, 92, 117, 145, ...
	 *
	 * It can be seen that P4 + P7 = 22 + 70 = 92 = P8. However, their difference,
	 * 70 − 22 = 48, is not pentagonal.
	 *
	 * Find the pair of pentagonal numbers, Pj and Pk, for which their sum and
	 * difference are pentagonal and D = |Pk − Pj| is minimised; what is the value
	 * of D?
	 */
	public String euler44() {
		int min = Integer.MAX_VALUE;
		final int limit = 10000000;
		final long[] pentagonals = new long[limit];
		for (int i = 0; i < pentagonals.length; i++) {
			pentagonals[i] = (long) (0.5 * i * (3 * i - 1));
		}
		final long highestValue = pentagonals[limit - 1];
		for (int j = 2; j < pentagonals.length; j++) {
			if (2 * pentagonals[j] > highestValue) {
				break;
			}
			for (int k = j - 1; k > 0; k--) {
				final long x = pentagonals[j];
				final long y = pentagonals[k];
				if (x - y > min || x + y > highestValue) {
					break;
				}
				if (Arrays.binarySearch(pentagonals, x + y) > 0 && Arrays.binarySearch(pentagonals, x - y) > 0) {
					min = (int) Long.min(min, Math.abs(x - y));
				}
			}
		}
		return Integer.toString(min);
	}

	/*
	 * Triangle, pentagonal, and hexagonal numbers are generated by the following
	 * formulae:
	 *
	 * Triangle Tn=n(n+1)/2 1, 3, 6, 10, 15, ... Pentagonal Pn=n(3n−1)/2 1, 5, 12,
	 * 22, 35, ... Hexagonal Hn=n(2n−1) 1, 6, 15, 28, 45, ... It can be verified
	 * that T285 = P165 = H143 = 40755.
	 *
	 * Find the next triangle number that is also pentagonal and hexagonal.
	 */
	public String euler45() {
		for (int i = 533;; i++) {
			final long hex = i * (2 * i - 1);
			// final double n_p_1 = 1.0/6.0 * (1-Math.sqrt(24*hex+1));
			final double n_p_2 = 1.0 / 6.0 * (1 + Math.sqrt(24 * hex + 1));
			if (n_p_2 % 1 == 0) {
				// final double n_t_1 = 1.0/2.0 * (-1 * Math.sqrt(8*hex+1)-1);
				final double n_t_2 = 1.0 / 2.0 * (Math.sqrt(8 * hex + 1) - 1);
				if (n_t_2 % 1 == 0) {
					return Long.toString(hex);
				}
			}
		}
	}

	/*
	 * What is the smallest odd composite that cannot be written as the sum of a
	 * prime and twice a square?
	 */
	public String euler46() {
		final int limit = 10000;
		final int limitSqr = (int) Math.floor(Math.sqrt(limit));
		final int[] squares = new int[limitSqr];
		for (int i = 1; i < squares.length; i++) {
			squares[i] = i * i;
		}
		for (int i = 3; i < limit; i += 2) {
			if (!Library.isPrime(i)) {
				boolean found = false;
				for (int j = 1; j < squares.length && squares[j] / 2 < i && !found; j++) {
					final int x = i - 2 * squares[j];
					if (Library.isPrime(x)) {
						found = true;
					}
				}
				if (!found) {
					return Integer.toString(i);
				}
			}
		}
		return "Fail";
	}

	/*
	 * Find the first four consecutive integers to have four distinct prime factors
	 * each. What is the first of these numbers?
	 */
	public String euler47() {
		final List<Integer> primes = Library.listPrimes(1000);
		final int target = 4;
		int count = 0;
		for (int n = 1;; n++) {
			int digits = 1;
			final long[] factors = Library.primeFactorization(n, primes);
			long last = factors[0];
			for (int i = 1; i < factors.length; i++) {
				final long factor = factors[i];
				if (factor != last) {
					last = factor;
					digits++;
				}
			}
			if (digits == target) {
				count++;
				if (count == target) {
					return Integer.toString((n - (target - 1)));
				}
			} else {
				count = 0;
			}
		}
	}

	/*
	 * Find the last ten digits of the series, 1^1 + 2^2 + 3^3 + ... + 1000^1000.
	 */
	public String euler48() {
		long sum = 0;
		final int digits = 10;
		final long mod = (long) Math.pow(10, digits);
		for (int i = 1; i <= 1000; i++) {
			long x = 1;
			for (int j = 0; j < i; j++) {
				x = (x * i) % mod;
			}
			sum = (sum + x) % mod;
		}
		return Long.toString(sum);
	}


	/*
	 * The arithmetic sequence, 1487, 4817, 8147, in which each of the terms
	 * increases by 3330, is unusual in two ways: (i) each of the three terms are
	 * prime, and, (ii) each of the 4-digit numbers are permutations of one another.
	 *
	 * There are no arithmetic sequences made up of three 1-, 2-, or 3-digit primes,
	 * exhibiting this property, but there is one other 4-digit increasing sequence.
	 *
	 * What 12-digit number do you form by concatenating the three terms in this
	 * sequence?
	 */
	public String euler49() {
		final List<Integer> primes = Library.listPrimes(1000, 10000);
		for (int a = 0; a < primes.size(); a++) {
			final int primeA = primes.get(a);
			if (primeA == 1487) {
				continue; // Skip
			}
			final int[] digitsA = new int[10];
			final String sA = Integer.toString(primeA);
			for (int i = 0; i < sA.length(); i++) {
				final int x = Character.digit(sA.charAt(i), 10);
				digitsA[x]++;
			}
			for (int b = a + 1; b < primes.size(); b++) {
				final int primeB = primes.get(b);
				final int[] digitsB = new int[10];
				final String sB = Integer.toString(primeB);
				for (int i = 0; i < sB.length(); i++) {
					final int x = Character.digit(sB.charAt(i), 10);
					digitsB[x]++;
				}
				if (!Arrays.equals(digitsA, digitsB)) {
					continue;
				}
				final int primeC = primeB + (primeB - primeA);
				if (Collections.binarySearch(primes, primeC) >= 0) {
					final int[] digitsC = new int[10];
					final String sC = Integer.toString(primeC);
					for (int i = 0; i < sC.length(); i++) {
						final int x = Character.digit(sC.charAt(i), 10);
						digitsC[x]++;
					}
					if (Arrays.equals(digitsA, digitsC)) {
						return Integer.toString(primeA) + primeB + primeC;
					}
				}
			}
		}
		return "Fail";
	}

	/*
	 * 70 colored balls are placed in an urn, 10 for each of the seven rainbow
	 * colors.
	 *
	 * What is the expected number of distinct colors in 20 randomly picked balls?
	 *
	 * Give your answer with nine digits after the decimal point (a.bcdefghij).
	 */
	public String euler493() {
		final long denominator = Library.binomialCoefficient(70, 20).longValueExact();
		final long[] binomial = new long[10 + 1];
		long sumOfColors = 0;
		binomial[0] = 1;
		for (int i = 1; i < binomial.length; i++) {
			binomial[i] = Library.binomialCoefficient(10, i).longValueExact();
		}
		for (int a = 0; a <= 10; a++) {
			for (int b = 0; b <= 10; b++) {
				for (int c = 0; c <= 10 && (a + b + c) <= 20; c++) {
					for (int d = 0; d <= 10 && (a + b + c + d) <= 20; d++) {
						for (int e = 0; e <= 10 && (a + b + c + d + e) <= 20; e++) {
							for (int f = 0; f <= 10 && (a + b + c + d + e + f) <= 20; f++) {
								final int g = 20 - a - b - c - d - e - f;
								if (g <= 10) {
									final long nominator = binomial[a] * binomial[b] * binomial[c] * binomial[d]
											* binomial[e] * binomial[f] * binomial[g];
									int colors = 0;
									colors += a != 0 ? 1 : 0;
									colors += b != 0 ? 1 : 0;
									colors += c != 0 ? 1 : 0;
									colors += d != 0 ? 1 : 0;
									colors += e != 0 ? 1 : 0;
									colors += f != 0 ? 1 : 0;
									colors += g != 0 ? 1 : 0;
									sumOfColors += colors * nominator;
								}
							}
						}
					}
				}
			}
		}
		BigDecimal n = BigDecimal.valueOf(sumOfColors);
		n = n.divide(BigDecimal.valueOf(denominator), 12, RoundingMode.HALF_EVEN);
		return n.toPlainString().substring(0, 11);
	}

	/*
	 * 2520 is the smallest number that can be divided by each of the numbers from 1
	 * to 10 without any remainder.
	 *
	 * What is the smallest positive number that is evenly divisible by all of the
	 * numbers from 1 to 20?
	 */
	public String euler5() {
		final ArrayList<Integer> factors = new ArrayList<>();
		for (int i = 2; i <= 20; i++) { // ignore 1
			int x = i;
			if (Library.isPrime(x)) {
				factors.add(x);
			} else {
				for (int j = 0; j < factors.size() && x != 1; j++) {
					if (x % factors.get(j) == 0) {
						x /= factors.get(j);
					}
				}
				if (x != 1) {
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

	/*
	 * The prime 41, can be written as the sum of six consecutive primes:
	 *
	 * 41 = 2 + 3 + 5 + 7 + 11 + 13 This is the longest sum of consecutive primes
	 * that adds to a prime below one-hundred.
	 *
	 * The longest sum of consecutive primes below one-thousand that adds to a
	 * prime, contains 21 terms, and is equal to 953.
	 *
	 * Which prime, below one-million, can be written as the sum of the most
	 * consecutive primes?
	 */
	public String euler50() {
		final int limit = 1000000;
		int maxValue = 0;
		final List<Integer> primes = Library.listPrimes(limit);
		for (int length = 1;; length++) {
			boolean primeFound = false;
			int sum = 0;
			for (int i = 0; i < length; i++) {
				sum += primes.get(i);
			}
			if (sum >= limit) {
				return Integer.toString(maxValue);
			}
			if (sum < limit && Library.isPrime(sum)) {
				maxValue = sum;
				continue;
			}
			for (int i = 0; i + length < primes.size() && !primeFound; i++) {
				sum -= primes.get(i);
				sum += primes.get(i + length);
				if (sum < limit && Library.isPrime(sum)) {
					maxValue = sum;
					primeFound = true;
					break;
				}
			}
		}
	}

	/*
	 * The file, poker.txt, contains one-thousand random hands dealt to two players.
	 * Each line of the file contains ten cards (separated by a single space): the
	 * first five are Player 1's cards and the last five are Player 2's cards. You
	 * can assume that all hands are valid (no invalid characters or repeated
	 * cards), each player's hand is in no specific order, and in each hand there is
	 * a clear winner.
	 *
	 * How many hands does Player 1 win?
	 */
	public String euler54() throws IOException, InterruptedException {
		final List<String> lines = Files.readAllLines(Paths.get("./54.txt"));
		int counter = 0;
		for (final String line : lines) {
			final String[] cards = line.split(" ");
			if (Library.pokerDecideWinner(cards[0] + cards[1] + cards[2] + cards[3] + cards[4],
					cards[5] + cards[6] + cards[7] + cards[8] + cards[9], "") == 1) {
				counter++;
			}
		}
		return Integer.toString(counter);
	}

	/*
	 * The sum of the squares of the first ten natural numbers is,
	 *
	 * 12 + 22 + ... + 102 = 385 The square of the sum of the first ten natural
	 * numbers is,
	 *
	 * (1 + 2 + ... + 10)2 = 552 = 3025 Hence the difference between the sum of the
	 * squares of the first ten natural numbers and the square of the sum is 3025 −
	 * 385 = 2640.
	 *
	 * Find the difference between the sum of the squares of the first one hundred
	 * natural numbers and the square of the sum.
	 */
	public String euler6() {
		long a = 0;
		long b = 0;
		for (int i = 1; i <= 100; i++) {
			a += Math.pow(i, 2);
			b += i;
		}
		return Long.toString((long) Math.pow(b, 2) - a);
	}

	/*
	 * The primes 3, 7, 109, and 673, are quite remarkable. By taking any two primes
	 * and concatenating them in any order the result will always be prime. For
	 * example, taking 7 and 109, both 7109 and 1097 are prime. The sum of these
	 * four primes, 792, represents the lowest sum for a set of four primes with
	 * this property.
	 *
	 * Find the lowest sum for a set of five primes for which any two primes
	 * concatenate to produce another prime.
	 */
	public String euler60() {
		final int limit = 50000;
		long smallestSum = Long.MAX_VALUE;
		final List<Integer> primes = Library.listPrimes(3, limit);
		final ArrayList<LinkedHashSet<Integer>> memory = new ArrayList<>(primes.size());
		while (memory.size() < primes.size()) {
			memory.add(null);
		}
		final Function<Integer, LinkedHashSet<Integer>> makePairs = new Function<Integer, LinkedHashSet<Integer>>() {
			@Override
			public LinkedHashSet<Integer> apply(final Integer x) {
				final LinkedHashSet<Integer> insert = new LinkedHashSet<>();
				final int prime = primes.get(x);
				for (int i = x + 1; i < primes.size(); i++) {
					final int y = primes.get(i);
					if (Library.isPrime((int) Library.concatLongs(prime, y))
							&& Library.isPrime((int) Library.concatLongs(y, prime))) {
						insert.add(i);
					}
				}
				return insert;
			}
		};
		for (int first = 0; first < primes.size() && primes.get(first) * 5 < limit; first++) {
			if (memory.get(first) == null) {
				memory.set(first, makePairs.apply(first));
			}
			final LinkedHashSet<Integer> setSecond = memory.get(first);
			for (final Integer second : setSecond) {
				if (primes.get(first) + primes.get(second) * 4 > smallestSum) {
					break;
				}
				if (memory.get(second) == null) {
					memory.set(second, makePairs.apply(second));
				}
				final LinkedHashSet<Integer> setThird = memory.get(second);
				for (final Integer third : setThird) {
					if (primes.get(first) + primes.get(second) + primes.get(third) * 3 > smallestSum) {
						break;
					}
					if (!setSecond.contains(third)) {
						continue;
					}
					if (memory.get(third) == null) {
						memory.set(third, makePairs.apply(third));
					}
					final LinkedHashSet<Integer> setFourth = memory.get(third);
					for (final Integer fourth : setFourth) {
						if (primes.get(first) + primes.get(second) + primes.get(third)
						+ primes.get(fourth) * 2 > smallestSum) {
							break;
						}
						if (!setSecond.contains(fourth) || !setThird.contains(fourth)) {
							continue;
						}
						if (memory.get(fourth) == null) {
							memory.set(fourth, makePairs.apply(fourth));
						}
						final LinkedHashSet<Integer> setFifth = memory.get(fourth);
						for (final Integer fifth : setFifth) {
							if (primes.get(first) + primes.get(second) + primes.get(third) + primes.get(fourth)
							+ primes.get(fifth) > smallestSum) {
								break;
							}
							if (!setSecond.contains(fifth) || !setThird.contains(fifth) || !setFourth.contains(fifth)) {
								continue;
							}
							final int sum = primes.get(first) + primes.get(second) + primes.get(third)
							+ primes.get(fourth) + primes.get(fifth);
							if (sum < smallestSum) {
								smallestSum = sum;
							}
						}
					}
				}
			}
		}
		return Long.toString(smallestSum);
	}

	/*
	 * Triangle, square, pentagonal, hexagonal, heptagonal, and octagonal numbers
	 * are all figurate (polygonal) numbers and are generated by the following
	 * formulae:
	 *
	 * Triangle P3,n=n(n+1)/2 1, 3, 6, 10, 15, ... Square P4,n=n2 1, 4, 9, 16, 25,
	 * ... Pentagonal P5,n=n(3n−1)/2 1, 5, 12, 22, 35, ... Hexagonal P6,n=n(2n−1) 1,
	 * 6, 15, 28, 45, ... Heptagonal P7,n=n(5n−3)/2 1, 7, 18, 34, 55, ... Octagonal
	 * P8,n=n(3n−2) 1, 8, 21, 40, 65, ... The ordered set of three 4-digit numbers:
	 * 8128, 2882, 8281, has three interesting properties.
	 *
	 * The set is cyclic, in that the last two digits of each number is the first
	 * two digits of the next number (including the last number with the first).
	 * Each polygonal type: triangle (P3,127=8128), square (P4,91=8281), and
	 * pentagonal (P5,44=2882), is represented by a different number in the set.
	 * This is the only set of 4-digit numbers with this property. Find the sum of
	 * the only ordered set of six cyclic 4-digit numbers for which each polygonal
	 * type: triangle, square, pentagonal, hexagonal, heptagonal, and octagonal, is
	 * represented by a different number in the set.
	 */
	public String euler61() {
		final ArrayList<ArrayList<Long>> numbers = new ArrayList<>();
		for (int i = 0; i < 6; i++) {
			numbers.add(new ArrayList<Long>());
		}
		long a = 0, b, c, d, e, f;
		final int lower = 1000;
		final int upper = 10000;
		for (int n = 1; a < 10000; n++) {
			a = n * (n + 1) / 2;
			b = n * n;
			c = n * (3 * n - 1) / 2;
			d = n * (2 * n - 1);
			e = n * (5 * n - 3) / 2;
			f = n * (3 * n - 2);
			if (a >= lower && a < upper) {
				numbers.get(0).add(a);
			}
			if (b >= lower && b < upper) {
				numbers.get(1).add(b);
			}
			if (c >= lower && c < upper) {
				numbers.get(2).add(c);
			}
			if (d >= lower && d < upper) {
				numbers.get(3).add(d);
			}
			if (e >= lower && e < upper) {
				numbers.get(4).add(e);
			}
			if (f >= lower && f < upper) {
				numbers.get(5).add(f);
			}
		}
		final ArrayList<String> permutations = Library.getPermutations("012345");
		for (final String permutation : permutations) {
			final ArrayList<Long> candidates = new ArrayList<>();
			candidates.addAll(numbers.get(Integer.parseInt(permutation.substring(0, 1))));
			for (int i = 1; i < permutation.length() && candidates.size() > 0; i++) {
				final int current = Integer.parseInt(permutation.substring(i, i + 1));
				final ArrayList<Long> oldCandidates = new ArrayList<>(candidates);
				final int oldSize = oldCandidates.size();
				for (int j = 0; j < oldSize; j++) {
					final long x = candidates.get(j);
					for (final Long y : numbers.get(current)) {
						final long suffix = x % 100;
						final long prefix = y / 100;
						if (suffix == prefix && !(suffix < 10) && !(prefix < 10)) {
							candidates.add(x * 100 + y % 100);
						}
					}
				}
				candidates.removeAll(oldCandidates);
				if (i == permutation.length() - 1 && candidates.size() > 0) {
					for (final Long l : candidates) {
						final String s = Long.toString(l);
						if (s.charAt(0) == s.charAt(s.length() - 2) && s.charAt(1) == s.charAt(s.length() - 1)) {
							int sum = 0;
							sum += Integer.parseInt(s.substring(0, 4));
							for (int k = 2; k < s.length() - 2; k += 2) {
								sum += Integer.parseInt(s.substring(k, k + 4));
							}
							return Integer.toString(sum);
						}
					}
				}
			}

		}
		return "";
	}

	/*
	 * The cube, 41063625 (3453), can be permuted to produce two other cubes:
	 * 56623104 (3843) and 66430125 (4053). In fact, 41063625 is the smallest cube
	 * which has exactly three permutations of its digits which are also cube.
	 *
	 * Find the smallest cube for which exactly five permutations of its digits are
	 * cube.
	 */
	public String euler62() {
		int numberOfDigits = 1;
		final CountMap<List<Integer>> count = new CountMap<>();
		final HashMap<List<Integer>, Long> smallestCube = new HashMap<>();
		for (long i = 0;; i++) {
			long cube = i * i * i;
			while (cube / (long) Math.pow(10, numberOfDigits) != 0) {
				numberOfDigits++;
				count.clear();
			}
			final List<Integer> digits = Arrays.asList(new Integer[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
			while (cube > 0) {
				final int lastDigit = (int) (cube % 10);
				final int x = digits.get(lastDigit);
				digits.set(lastDigit, x + 1);
				cube /= 10;
			}
			if (!count.containsKey(digits)) {
				smallestCube.put(digits, i * i * i);
			}
			if (count.countUp(digits) == 5) {
				return Long.toString(smallestCube.get(digits));
			}
		}
	}

	/*
	 * The 5-digit number, 16807=75, is also a fifth power. Similarly, the 9-digit
	 * number, 134217728=89, is a ninth power.
	 *
	 * How many n-digit positive integers exist which are also an nth power?
	 */
	public String euler63() {
		int counter = 0;
		for (long i = 1; i < 10; i++) {
			for (int e = 1;; e++) {
				BigInteger n = BigInteger.valueOf(i);
				n = n.pow(e);
				final int digits = Library.countDigits(n);
				if (e == digits) {
					counter++;
				} else if (digits < e || digits > e) {
					break;
				}
			}
		}
		return Integer.toString(counter);
	}

	/*
	 * How many continued fractions for N ≤ 10000 have an odd period?
	 * https://en.wikipedia.org/wiki/Methods_of_computing_square_roots#
	 * Continued_fraction_expansion
	 */
	public String euler64() {
		int counter = 0;
		for (int i = 2; i <= 10000; i++) {
			final int sqrt = (int) Math.floor(Math.sqrt(i));
			if (sqrt * sqrt == i) {
				continue;
			}
			int m = 0;
			int d = 1;
			int a = sqrt;
			final ArrayList<Integer> sequence = new ArrayList<>();
			sequence.add(a);
			do {
				m = d * a - m;
				d = (i - (m * m)) / (d);
				a = (int) Math.floor((sequence.get(0) + m) / d);
				sequence.add(a);
			} while (a != 2 * sequence.get(0));
			if (sequence.size() % 2 == 0) {
				counter++;
			}
		}
		return Integer.toString(counter);
	}

	/*
	 * Find the sum of digits in the numerator of the 100th convergent of the
	 * continued fraction for e.
	 */
	public String euler65() {
		BigInteger d = BigInteger.ONE;
		BigInteger n = BigInteger.valueOf(2);
		for (int i = 2; i <= 100; i++) {
			final BigInteger temp = d;
			final int c = (i % 3 == 0) ? 2 * (i / 3) : 1;
			d = n;
			n = d.multiply(BigInteger.valueOf(c)).add(temp);
		}
		return Integer.toString(Library.digitSum(n));
	}

	/*
	 * Consider quadratic Diophantine equations of the form:
	 *
	 * x2 – Dy2 = 1
	 *
	 * For example, when D=13, the minimal solution in x is 6492 – 13×1802 = 1.
	 *
	 * It can be assumed that there are no solutions in positive integers when D is
	 * square.
	 *
	 * By finding minimal solutions in x for D = {2, 3, 5, 6, 7}, we obtain the
	 * following:
	 *
	 * 32 – 2×22 = 1 22 – 3×12 = 1 92 – 5×42 = 1 52 – 6×22 = 1 82 – 7×32 = 1
	 *
	 * Hence, by considering minimal solutions in x for D ≤ 7, the largest x is
	 * obtained when D=5.
	 *
	 * Find the value of D ≤ 1000 in minimal solutions of x for which the largest
	 * value of x is obtained.
	 */
	public String euler66() {
		int result = 0;
		BigInteger max = BigInteger.ZERO;
		for (int i = 2; i <= 1000; i++) {
			final int sqrt = (int) Math.sqrt(i);
			if (sqrt * sqrt == i) {
				continue;
			}
			BigInteger m = BigInteger.ZERO;
			BigInteger d = BigInteger.ONE;
			BigInteger a = BigInteger.valueOf(sqrt);
			BigInteger numm1 = BigInteger.ONE;
			BigInteger num = a;
			BigInteger denm1 = BigInteger.ZERO;
			BigInteger den = BigInteger.ONE;
			while (!num.multiply(num).subtract(den.pow(2).multiply(BigInteger.valueOf(i))).equals(BigInteger.ONE)) {
				m = d.multiply(a).subtract(m);
				d = (BigInteger.valueOf(i).subtract(m.pow(2))).divide(d);
				a = (BigInteger.valueOf(sqrt).add(m)).divide(d);
				final BigInteger numm2 = numm1;
				numm1 = num;
				final BigInteger denm2 = denm1;
				denm1 = den;
				num = a.multiply(numm1).add(numm2);
				den = a.multiply(denm1).add(denm2);
			}
			if (num.compareTo(max) > 0) {
				max = num;
				result = i;
			}
		}
		return Integer.toString(result);
	}

	/*
	 * Same as Problem 18 only bigger.
	 */
	public String euler67() throws IOException {
		final List<String> lines = Files.readAllLines(Paths.get("./67.txt"));
		final int size = lines.size();
		final long[][] grid = new long[size][size];
		for (int i = 0; i < size; i++) {
			final String[] numbers = lines.get(i).split(" ");
			for (int j = 0; j < numbers.length; j++) {
				grid[i][j] += Integer.parseInt(numbers[j]);
				if (i != 0) {
					if (j == 0) {
						grid[i][j] += grid[i - 1][j];
					} else {
						grid[i][j] += Long.max(grid[i - 1][j], grid[i - 1][j - 1]);
					}
				}
			}
		}
		long max = 0;
		for (int i = 0; i < size; i++) {
			max = Long.max(max, grid[size - 1][i]);
		}
		return Long.toString(max);
	}

	/*
	 * Consider the following "magic" 3-gon ring, filled with the numbers 1 to 6,
	 * and each line adding to nine.
	 *
	 *
	 * Working clockwise, and starting from the group of three with the numerically
	 * lowest external node (4,3,2 in this example), each solution can be described
	 * uniquely. For example, the above solution can be described by the set: 4,3,2;
	 * 6,2,1; 5,1,3.
	 *
	 * It is possible to complete the ring with four different totals: 9, 10, 11,
	 * and 12. There are eight solutions in total.
	 *
	 * Total Solution Set 9 4,2,3; 5,3,1; 6,1,2 9 4,3,2; 6,2,1; 5,1,3 10 2,3,5;
	 * 4,5,1; 6,1,3 10 2,5,3; 6,3,1; 4,1,5 11 1,4,6; 3,6,2; 5,2,4 11 1,6,4; 5,4,2;
	 * 3,2,6 12 1,5,6; 2,6,4; 3,4,5 12 1,6,5; 3,5,4; 2,4,6 By concatenating each
	 * group it is possible to form 9-digit strings; the maximum string for a 3-gon
	 * ring is 432621513.
	 *
	 * Using the numbers 1 to 10, and depending on arrangements, it is possible to
	 * form 16- and 17-digit strings. What is the maximum 16-digit string for a
	 * "magic" 5-gon ring?
	 */
	public String euler68() {
		long max = 0;
		for (int target = 27; target >= 6; target--) {
			for (int a = 1; a <= 10; a++) {
				for (int b = 1; b <= 10; b++) {
					if (a == b) {
						continue;
					}
					for (int c = 1; c <= 10; c++) {
						final int line1 = a + b + c;
						if (line1 != target || !Library.noDuplicates(new Integer[] { a, b, c })) {
							continue;
						}
						for (int d = 1; d <= 10; d++) {
							if (!Library.noDuplicates(new Integer[] { a, b, c, d })) {
								continue;
							}
							for (int e = 1; e <= 10; e++) {
								final int line2 = d + c + e;
								if (line2 != target || !Library.noDuplicates(new Integer[] { a, b, c, d, e })) {
									continue;
								}
								for (int f = 1; f <= 10; f++) {
									if (!Library.noDuplicates(new Integer[] { a, b, c, d, e, f })) {
										continue;
									}
									for (int g = 1; g <= 10; g++) {
										final int line3 = f + e + g;
										if (line3 != target || !Library.noDuplicates(new Integer[] { a, b, c, d, e, f, g })) {
											continue;
										}
										for (int h = 1; h <= 10; h++) {
											if (!Library.noDuplicates(new Integer[] { a, b, c, d, e, f, g, h })) {
												continue;
											}
											for (int i = 1; i <= 10; i++) {
												final int line4 = h + g + i;
												if (line4 != target || Library.noDuplicates(new Integer[] { a, b, c, d, e, f, g, h, i })) {
													continue;
												}
												for (int j = 1; j <= 10; j++) {
													final int line5 = j + i + b;
													if (line5 == target && Library.noDuplicates(
															new Integer[] { a, b, c, d, e, f, g, h, i, j })) {
														if (a < d && a < f && a < h && j > a) {
															final String s = "" + a + b + c + d + c + e + f + e + g + h
																	+ g + i + j + i + b;
															if (s.length() == 16) {
																max = Math.max(max, Long.parseLong(s));
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}

			}
		}
		return Long.toString(max);
	}

	/*
	 * It can be seen that n=6 produces a maximum n/φ(n) for n ≤ 10.
	 *
	 * Find the value of n ≤ 1,000,000 for which n/φ(n) is a maximum.
	 */
	public String euler69() {
		double max = 0;
		int result = 0;
		final long[] phi = Library.listPhi(1000000);
		for (int n = 2; n < phi.length; n++) {
			final double x = (1.0 * n) / (1.0 * phi[n]);
			if (x > max) {
				result = n;
				max = x;
			}
		}
		return Integer.toString(result);
	}

	/*
	 * By listing the first six prime numbers: 2, 3, 5, 7, 11, and 13, we can see
	 * that the 6th prime is 13.
	 *
	 * What is the 10 001st prime number?
	 */
	public String euler7() {
		int count = 0;
		for (int i = 2;; i++) {
			if (Library.isPrime(i)) {
				count++;
				if (count == 10001) {
					return Integer.toString(i);
				}
			}
		}
	}

	/*
	 * Euler's Totient function, φ(n) [sometimes called the phi function], is used
	 * to determine the number of positive numbers less than or equal to n which are
	 * relatively prime to n. For example, as 1, 2, 4, 5, 7, and 8, are all less
	 * than nine and relatively prime to nine, φ(9)=6. The number 1 is considered to
	 * be relatively prime to every positive number, so φ(1)=1.
	 *
	 * Interestingly, φ(87109)=79180, and it can be seen that 87109 is a permutation
	 * of 79180.
	 *
	 * Find the value of n, 1 < n < 10^7, for which φ(n) is a permutation of n and
	 * the ratio n/φ(n) produces a minimum.
	 */
	public String euler70() {
		int result = 0;
		double min = Double.MAX_VALUE;
		final List<Integer> primes = Library.listPrimes(2000, 5000);
		for (int i = 0; i < primes.size() && primes.get(i) < Math.sqrt(10000000); i++) {
			for (int j = i + 1; j < primes.size(); j++) {
				final int n = primes.get(i) * primes.get(j);
				if (n > 10000000) {
					break;
				}
				if (Library.isPermutation(n, Library.phi(n))) {
					final double ratio = (1.0 * n) / (1.0 * Library.phi(n));
					if (ratio < min) {
						min = ratio;
						result = n;
					}
				}
			}
		}
		return Integer.toString(result);
	}

	/*
	 * Consider the fraction, n/d, where n and d are positive integers. If n<d and
	 * HCF(n,d)=1, it is called a reduced proper fraction.
	 *
	 * If we list the set of reduced proper fractions for d ≤ 8 in ascending order
	 * of size, we get:
	 *
	 * 1/8, 1/7, 1/6, 1/5, 1/4, 2/7, 1/3, 3/8, 2/5, 3/7, 1/2, 4/7, 3/5, 5/8, 2/3,
	 * 5/7, 3/4, 4/5, 5/6, 6/7, 7/8
	 *
	 * It can be seen that 2/5 is the fraction immediately to the left of 3/7.
	 *
	 * By listing the set of reduced proper fractions for d ≤ 1,000,000 in ascending
	 * order of size, find the numerator of the fraction immediately to the left of
	 * 3/7.
	 */
	public String euler71() {
		final int scale = 50;
		final BigDecimal target = BigDecimal.valueOf(3).divide(BigDecimal.valueOf(7), scale, RoundingMode.HALF_UP);
		BigDecimal best = BigDecimal.ONE;
		int result = 0;
		int n = 2;
		int d = 7;
		BigDecimal difference = target
				.subtract(BigDecimal.valueOf(n).divide(BigDecimal.valueOf(d), scale, RoundingMode.HALF_UP));
		while (d <= 1000000) {
			if (difference.signum() == 1) {
				if (difference.compareTo(best) < 0) {
					best = difference;
					result = n;
				}
				n++;
			} else {
				d++;
			}
			difference = target
					.subtract(BigDecimal.valueOf(n).divide(BigDecimal.valueOf(d), scale, RoundingMode.HALF_UP));
		}
		return Integer.toString(result);
	}

	/*
	 * Consider the fraction, n/d, where n and d are positive integers. If n<d and
	 * HCF(n,d)=1, it is called a reduced proper fraction.
	 *
	 * If we list the set of reduced proper fractions for d ≤ 8 in ascending order
	 * of size, we get:
	 *
	 * 1/8, 1/7, 1/6, 1/5, 1/4, 2/7, 1/3, 3/8, 2/5, 3/7, 1/2, 4/7, 3/5, 5/8, 2/3,
	 * 5/7, 3/4, 4/5, 5/6, 6/7, 7/8
	 *
	 * It can be seen that there are 21 elements in this set.
	 *
	 * How many elements would be contained in the set of reduced proper fractions
	 * for d ≤ 1,000,000?
	 */
	public String euler72() {
		final long[] phi = Library.listPhi(1000000);
		long sum = 0;
		for (final Long l : phi) {
			sum += l;
		}
		return Long.toString(sum);
	}

	/*
	 * Consider the fraction, n/d, where n and d are positive integers. If n<d and
	 * HCF(n,d)=1, it is called a reduced proper fraction.
	 *
	 * If we list the set of reduced proper fractions for d ≤ 8 in ascending order
	 * of size, we get:
	 *
	 * 1/8, 1/7, 1/6, 1/5, 1/4, 2/7, 1/3, 3/8, 2/5, 3/7, 1/2, 4/7, 3/5, 5/8, 2/3,
	 * 5/7, 3/4, 4/5, 5/6, 6/7, 7/8
	 *
	 * It can be seen that there are 3 fractions between 1/3 and 1/2.
	 *
	 * How many fractions lie between 1/3 and 1/2 in the sorted set of reduced
	 * proper fractions for d ≤ 12,000?
	 */
	public String euler73() {
		int counter = 0;
		final double lower = 1.0 / 3.0;
		final double upper = 1.0 / 2.0;
		for (int d = 2; d <= 12000; d++) {
			for (int n = 0; n < d; n++) {
				final double fraction = (1.0 * n) / (1.0 * d);
				if (lower < fraction && fraction < upper && Library.ggT(d, n) == 1) {
					counter++;
				} else if (fraction > upper) {
					break;
				}
			}
		}
		return Integer.toString(counter);
	}

	/*
	 * The number 145 is well known for the property that the sum of the factorial
	 * of its digits is equal to 145:
	 *
	 * 1! + 4! + 5! = 1 + 24 + 120 = 145
	 *
	 * Perhaps less well known is 169, in that it produces the longest chain of
	 * numbers that link back to 169; it turns out that there are only three such
	 * loops that exist:
	 *
	 * 169 → 363601 → 1454 → 169 871 → 45361 → 871 872 → 45362 → 872
	 *
	 * It is not difficult to prove that EVERY starting number will eventually get
	 * stuck in a loop. For example,
	 *
	 * 69 → 363600 → 1454 → 169 → 363601 (→ 1454) 78 → 45360 → 871 → 45361 (→ 871)
	 * 540 → 145 (→ 145)
	 *
	 * Starting with 69 produces a chain of five non-repeating terms, but the
	 * longest non-repeating chain with a starting number below one million is sixty
	 * terms.
	 *
	 * How many chains, with a starting number below one million, contain exactly
	 * sixty non-repeating terms?
	 */
	public String euler74() {
		int result = 0;
		final long[] sequence = new long[6 * (9 * 8 * 7 * 6 * 5 * 4 * 3 * 2) + 1];
		sequence[0] = 1;
		for (int i = 1; i < 10; i++) {
			sequence[i] = i * sequence[i - 1];
		}
		for (int start = 3; start < 1000000; start++) {
			long n = start;
			int steps = 0;
			final Set<Long> chain = new HashSet<>();
			while (!chain.contains(n)) {
				if (n > Integer.MAX_VALUE) {
					System.exit(0);
				}
				chain.add(n);
				if (sequence[(int) n] == 0) {
					int m = 0;
					while (n > 0) {
						m += sequence[(int) (n % 10)];
						n /= 10;
					}
					n = m;
					steps++;
				} else {
					n = sequence[(int) n];
					steps++;
				}
				if (steps == 60) {
					result++;
					break;
				}
			}
		}
		return Integer.toString(result);
	}

	/*
	 * It turns out that 12 cm is the smallest length of wire that can be bent to
	 * form an integer sided right angle triangle in exactly one way, but there are
	 * many more examples.
	 *
	 * 12 cm: (3,4,5) 24 cm: (6,8,10) 30 cm: (5,12,13) 36 cm: (9,12,15) 40 cm:
	 * (8,15,17) 48 cm: (12,16,20)
	 *
	 * In contrast, some lengths of wire, like 20 cm, cannot be bent to form an
	 * integer sided right angle triangle, and other lengths allow more than one
	 * solution to be found; for example, using 120 cm it is possible to form
	 * exactly three different integer sided right angle triangles.
	 *
	 * 120 cm: (30,40,50), (20,48,52), (24,45,51)
	 *
	 * Given that L is the length of the wire, for how many values of L ≤ 1,500,000
	 * can exactly one integer sided right angle triangle be formed?
	 */
	public String euler75() {
		final int limit = 1500000;
		final int[] triangles = new int[limit + 1];
		int result = 0;
		final int mlimit = (int) Math.sqrt(limit / 2);
		for (long m = 2; m < mlimit; m++) {
			for (long n = 1; n < m; n++) {
				if (((n + m) % 2) == 1 && Library.ggT(n, m) == 1) {
					final long a = m * m + n * n;
					final long b = m * m - n * n;
					final long c = 2 * m * n;
					long p = a + b + c;
					while (p <= limit) {
						triangles[(int) p]++;
						if (triangles[(int) p] == 1) {
							result++;
						}
						if (triangles[(int) p] == 2) {
							result--;
						}
						p += a + b + c;
					}
				}
			}
		}
		return Integer.toString(result);
	}

	/*
	 * How many different ways can one hundred be written as a sum of at least two
	 * positive integers?
	 */
	public String euler76() {
		final long[] ways = new long[101];
		ways[0] = 1;
		for (int i = 1; i < ways.length - 1; i++) {
			for (int j = i; j < ways.length; j++) {
				ways[j] += ways[j - i];
			}
		}
		return Long.toString(ways[100]);
	}

	/*
	 * What is the first value which can be written as the sum of primes in over
	 * five thousand different ways?
	 */
	public String euler77() {
		int target = 2;
		final List<Integer> primes = Library.listPrimes(1000);
		while (true) {
			final long[] ways = new long[target + 1];
			ways[0] = 1;
			for (int i = 0; i < primes.size(); i++) {
				for (int j = primes.get(i); j <= target; j++) {
					ways[j] += ways[j - primes.get(i)];
				}
			}
			if (ways[target] > 5000) {
				break;
			}
			target++;
		}
		return Integer.toString(target);
	}

	/*
	 * Find the least value of n for which p(n) is divisible by one million.
	 */
	public String euler78() {
		final ArrayList<Long> partitions = new ArrayList<>();
		partitions.add(1L);
		for (int n = 1;; n++) {
			long p = 0;
			int k = 1;
			int penta = k * (3 * k - 1) / 2;
			while (penta <= n) {
				final int sign = (k % 2 == 0) ? -1 : 1;
				p += (sign * partitions.get(n - penta));
				p %= 1000000;
				if (k > 0) {
					k *= -1;
				} else {
					k *= -1;
					k += 1;
				}
				penta = k * (3 * k - 1) / 2;
			}
			if (p == 0) {
				return Integer.toString(n);
			}
			partitions.add(p);
		}
	}

	/*
	 * A common security method used for online banking is to ask the user for three
	 * random characters from a passcode. For example, if the passcode was 531278,
	 * they may ask for the 2nd, 3rd, and 5th characters; the expected reply would
	 * be: 317.
	 *
	 * The text file, keylog.txt, contains fifty successful login attempts.
	 *
	 * Given that the three characters are always asked for in order, analyse the
	 * file so as to determine the shortest possible secret passcode of unknown
	 * length.
	 */
	public String euler79() throws IOException {
		final List<String> numbers = Files.readAllLines(Paths.get("./79.txt"));
		final List<ArrayList<Integer>> after = new ArrayList<>(10);
		final boolean[] digitsUsed = new boolean[10];
		for (int i = 0; i < 10; i++) {
			after.add(new ArrayList<Integer>());
		}
		for (final String number : numbers) {
			final int digit = Character.digit(number.charAt(0), 10);
			digitsUsed[digit] = true;
			final int i1 = Character.digit(number.charAt(1), 10);
			final int i2 = Character.digit(number.charAt(2), 10);
			digitsUsed[digit] = true;
			digitsUsed[i1] = true;
			digitsUsed[i2] = true;
			if (i1 == i2) {
				if (Collections.frequency(after.get(digit), i1) == 0) {
					after.get(digit).add(i1);
					after.get(digit).add(i2);
				}
				if (Collections.frequency(after.get(digit), i1) == 1) {
					after.get(digit).add(i1);
				}
			} else {
				if (Collections.frequency(after.get(digit), i1) == 0) {
					after.get(digit).add(i1);
				}
				if (Collections.frequency(after.get(digit), i2) == 0) {
					after.get(digit).add(i2);
				}
			}
			if (Collections.frequency(after.get(i1), i2) == 0) {
				after.get(i1).add(i2);
			}
		}
		for (int i = 0; i < 10; i++) {
			if (digitsUsed[i]) {
				Collections.sort(after.get(i));
			}
		}
		final ArrayList<Integer> result = new ArrayList<>();
		boolean changed = false;
		do {
			changed = false;
			for (int i = 0; i < after.size(); i++) {
				if (digitsUsed[i]) {
					final ArrayList<Integer> sortedResult = new ArrayList<>(result);
					Collections.sort(sortedResult);
					if (sortedResult.equals(after.get(i))) {
						result.add(0, i);
						changed = true;
					}
				}
			}
		} while (changed);
		return result.stream().map(Object::toString).collect(Collectors.joining(""));
	}

	/*
	 * The four adjacent digits in the 1000-digit number that have the greatest
	 * product are 9 × 9 × 8 × 9 = 5832.
	 *
	 * 73167176531330624919225119674426574742355349194934
	 * 96983520312774506326239578318016984801869478851843
	 * 85861560789112949495459501737958331952853208805511
	 * 12540698747158523863050715693290963295227443043557
	 * 66896648950445244523161731856403098711121722383113
	 * 62229893423380308135336276614282806444486645238749
	 * 30358907296290491560440772390713810515859307960866
	 * 70172427121883998797908792274921901699720888093776
	 * 65727333001053367881220235421809751254540594752243
	 * 52584907711670556013604839586446706324415722155397
	 * 53697817977846174064955149290862569321978468622482
	 * 83972241375657056057490261407972968652414535100474
	 * 82166370484403199890008895243450658541227588666881
	 * 16427171479924442928230863465674813919123162824586
	 * 17866458359124566529476545682848912883142607690042
	 * 24219022671055626321111109370544217506941658960408
	 * 07198403850962455444362981230987879927244284909188
	 * 84580156166097919133875499200524063689912560717606
	 * 05886116467109405077541002256983155200055935729725
	 * 71636269561882670428252483600823257530420752963450
	 *
	 * Find the thirteen adjacent digits in the 1000-digit number that have the
	 * greatest product. What is the value of this product?
	 */
	public String euler8() throws IOException {
		final String number = Files.readAllLines(Paths.get("/8.txt")).get(0);
		long max = 0;
		long product = 1;
		long products = 0;
		for (int i = 0; i < number.length(); i++) {
			final int digit = Character.digit(number.charAt(i), 10);
			if (digit == 0) {
				product = 1;
				products = 0;
			} else {
				if (products < 13) {
					product *= digit;
					products++;
				} else {
					product /= Character.digit(number.charAt(i - 13), 10);
					product *= Character.digit(number.charAt(i), 10);
					if (product > max) {
						max = product;
					}
				}
			}
		}
		return Long.toString(max);
	}

	/*
	 * It is well known that if the square root of a natural number is not an
	 * integer, then it is irrational. The decimal expansion of such square roots is
	 * infinite without any repeating pattern at all.
	 *
	 * The square root of two is 1.41421356237309504880..., and the digital sum of
	 * the first one hundred decimal digits is 475.
	 *
	 * For the first one hundred natural numbers, find the total of the digital sums
	 * of the first one hundred decimal digits for all the irrational square roots.
	 */
	public String euler80() {
		int sum = 0;
		int j = 1;
		for (int i = 1; i <= 100; i++) {
			if (j * j == i) {
				j++;
				continue;
			}
			BigDecimal d = new BigDecimal(i);
			d = Library.squareRoot(d, 103);
			final String s = d.toPlainString().substring(0, 101).replaceAll("\\.", "");
			sum += Library.digitSum(s);
		}
		return Integer.toString(sum);
	}

	/*
	 * In the 5 by 5 matrix below, the minimal path sum from the top left to the
	 * bottom right, by only moving to the right and down, is indicated in bold red
	 * and is equal to 2427.
	 *
	 * Find the minimal path sum, in matrix.txt (right click and
	 * "Save Link/Target As..."), a 31K text file containing a 80 by 80 matrix, from
	 * the top left to the bottom right by only moving right and down.
	 */
	public String euler81() throws IOException {
		final List<String> lines = Files.readAllLines(Paths.get("./81.txt"));
		final int[][] matrix = new int[lines.size()][lines.size()];
		for (int i = 0; i < lines.size(); i++) {
			final String[] split = lines.get(i).split(",");
			for (int j = 0; j < split.length; j++) {
				matrix[i][j] = Integer.parseInt(split[j]);
			}
		}

		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				if (i == 0 && j != 0) {
					matrix[i][j] += matrix[i][j - 1];
				} else if (i != 0 && j == 0) {
					matrix[i][j] += matrix[i - 1][j];
				} else if (i != 0 && j != 0) {
					matrix[i][j] += Math.min(matrix[i - 1][j], matrix[i][j - 1]);
				}
			}
		}
		return Integer.toString(matrix[matrix.length - 1][matrix.length - 1]);
	}

	/*
	 * The minimal path sum in the 5 by 5 matrix below, by starting in any cell in
	 * the left column and finishing in any cell in the right column, and only
	 * moving up, down, and right, is indicated in red and bold; the sum is equal to
	 * 994.
	 *
	 * Find the minimal path sum, in matrix.txt (right click and
	 * "Save Link/Target As..."), a 31K text file containing a 80 by 80 matrix, from
	 * the left column to the right column.
	 */
	public String euler82() throws IOException {
		final List<String> lines = Files.readAllLines(Paths.get("./81.txt"));
		final int[][] matrix = new int[lines.size()][lines.size()];
		for (int i = 0; i < lines.size(); i++) {
			final String[] split = lines.get(i).split(",");
			for (int j = 0; j < split.length; j++) {
				matrix[i][j] = Integer.parseInt(split[j]);
			}
		}
		for (int j = 1; j < matrix[0].length; j++) {
			final int[] column = new int[lines.size()];
			for (int i = 0; i < matrix.length; i++) {
				if (i == 0) {
					column[i] = matrix[i][j] + matrix[i][j - 1];
				} else {
					column[i] = matrix[i][j] + Math.min(column[i - 1], matrix[i][j - 1]);
				}
			}
			for (int i = matrix.length - 1; i >= 0; i--) {
				if (i == matrix.length - 1) {
					matrix[i][j] = Math.min(matrix[i][j] + matrix[i][j - 1], column[i] + matrix[i][j - 1]);
				} else {
					matrix[i][j] = Math.min(column[i], matrix[i][j] + matrix[i + 1][j]);
				}
			}
		}
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < matrix.length; i++) {
			min = Math.min(min, matrix[i][matrix.length - 1]);
		}
		return Integer.toString(min);
	}

	/*
	 * In the 5 by 5 matrix below, the minimal path sum from the top left to the
	 * bottom right, by moving left, right, up, and down, is indicated in bold red
	 * and is equal to 2297. Find the minimal path sum, in matrix.txt (right click
	 * and "Save Link/Target As..."), a 31K text file containing a 80 by 80 matrix,
	 * from the top left to the bottom right by moving left, right, up, and down.
	 */
	public String euler83() throws IOException {
		final List<String> lines = Files.readAllLines(Paths.get("./81.txt"));
		final int[][] matrix = new int[lines.size()][lines.size()];
		final int[][] best = new int[lines.size()][lines.size()];
		for (int i = 0; i < lines.size(); i++) {
			final String[] split = lines.get(i).split(",");
			for (int j = 0; j < split.length; j++) {
				matrix[i][j] = Integer.parseInt(split[j]);
			}
		}
		final PriorityQueue<Point> queue = new PriorityQueue<>(new Comparator<Point>() {
			@Override
			public int compare(final Point p1, final Point p2) {
				return Double.compare(p1.getX(), p2.getX());
			}
		});
		final boolean[][] done = new boolean[lines.size()][lines.size()];
		queue.add(new Point(matrix[0][0], 0));
		best[0][0] = matrix[0][0];
		while (!queue.isEmpty() && !done[lines.size() - 1][lines.size() - 1]) {
			final Point p = queue.poll();
			final int py = p.y / lines.size();
			final int px = p.y % lines.size();
			done[py][px] = true;
			if (py > 0 && !done[py - 1][px]) {
				final int value = best[py][px] + matrix[py - 1][px];
				if (best[py - 1][px] == 0 || value < best[py][px]) {
					best[py - 1][px] = value;
				}
				final Point toAdd = new Point(value, p.y - lines.size());
				if (!queue.contains(toAdd)) {
					queue.add(toAdd);
				}
			}
			if (py < lines.size() - 1 && !done[py + 1][px]) {
				final int value = best[py][px] + matrix[py + 1][px];
				if (best[py + 1][px] == 0 || value < best[py][px]) {
					best[py + 1][px] = value;
				}
				final Point toAdd = new Point(value, p.y + lines.size());
				if (!queue.contains(toAdd)) {
					queue.add(toAdd);
				}
			}
			if (px > 0 && !done[py][px - 1]) {
				final int value = best[py][px] + matrix[py][px - 1];
				if (best[py][px - 1] == 0 || value < best[py][px]) {
					best[py][px - 1] = value;
				}
				final Point toAdd = new Point(value, p.y - 1);
				if (!queue.contains(toAdd)) {
					queue.add(toAdd);
				}
			}
			if (px < lines.size() - 1 && !done[py][px + 1]) {
				final int value = best[py][px] + matrix[py][px + 1];
				if (best[py][px + 1] == 0 || value < best[py][px]) {
					best[py][px + 1] = value;
				}
				final Point toAdd = new Point(value, p.y + 1);
				if (!queue.contains(toAdd)) {
					queue.add(toAdd);
				}
			}
		}
		return Integer.toString(best[lines.size() - 1][lines.size() - 1]);
	}

	/*
	 * In the game, Monopoly, the standard board is set up in the following way:
	 *
	 * GO A1 CC1 A2 T1 R1 B1 CH1 B2 B3 JAIL H2 C1 T2 U1 H1 C2 CH3 C3 R4 R2 G3 D1 CC3
	 * CC2 G2 D2 G1 D3 G2J F3 U2 F2 F1 R3 E3 E2 CH2 E1 FP A player starts on the GO
	 * square and adds the scores on two 6-sided dice to determine the number of
	 * squares they advance in a clockwise direction. Without any further rules we
	 * would expect to visit each square with equal probability: 2.5%. However,
	 * landing on G2J (Go To Jail), CC (community chest), and CH (chance) changes
	 * this distribution.
	 *
	 * In addition to G2J, and one card from each of CC and CH, that orders the
	 * player to go directly to jail, if a player rolls three consecutive doubles,
	 * they do not advance the result of their 3rd roll. Instead they proceed
	 * directly to jail.
	 *
	 * At the beginning of the game, the CC and CH cards are shuffled. When a player
	 * lands on CC or CH they take a card from the top of the respective pile and,
	 * after following the instructions, it is returned to the bottom of the pile.
	 * There are sixteen cards in each pile, but for the purpose of this problem we
	 * are only concerned with cards that order a movement; any instruction not
	 * concerned with movement will be ignored and the player will remain on the
	 * CC/CH square.
	 *
	 * Community Chest (2/16 cards): Advance to GO Go to JAIL Chance (10/16 cards):
	 * Advance to GO Go to JAIL Go to C1 Go to E3 Go to H2 Go to R1 Go to next R
	 * (railway company) Go to next R Go to next U (utility company) Go back 3
	 * squares. The heart of this problem concerns the likelihood of visiting a
	 * particular square. That is, the probability of finishing at that square after
	 * a roll. For this reason it should be clear that, with the exception of G2J
	 * for which the probability of finishing on it is zero, the CH squares will
	 * have the lowest probabilities, as 5/8 request a movement to another square,
	 * and it is the final square that the player finishes at on each roll that we
	 * are interested in. We shall make no distinction between "Just Visiting" and
	 * being sent to JAIL, and we shall also ignore the rule about requiring a
	 * double to "get out of jail", assuming that they pay to get out on their next
	 * turn.
	 *
	 * By starting at GO and numbering the squares sequentially from 00 to 39 we can
	 * concatenate these two-digit numbers to produce strings that correspond with
	 * sets of squares.
	 *
	 * Statistically it can be shown that the three most popular squares, in order,
	 * are JAIL (6.24%) = Square 10, E3 (3.18%) = Square 24, and GO (3.09%) = Square
	 * 00. So these three most popular squares can be listed with the six-digit
	 * modal string: 102400.
	 *
	 * If, instead of using two 6-sided dice, two 4-sided dice are used, find the
	 * six-digit modal string.
	 */
	public String euler84() {
		final CountMap<Integer> visited = new CountMap<>();
		int position = 0;
		int doublesRolled = 0;
		final ArrayList<Integer> community = new ArrayList<>();
		community.add(0);
		community.add(10);
		for (int i = 0; i < 14; i++) {
			community.add(null);
		}
		final ArrayList<Integer> chance = new ArrayList<>();
		chance.add(0);
		chance.add(10);
		chance.add(11);
		chance.add(24);
		chance.add(39);
		chance.add(5);
		chance.add(40); // Next Railroad
		chance.add(40);
		chance.add(41); // Next Utility
		chance.add(42); // -3 squares
		for (int i = 0; i < 6; i++) {
			chance.add(null);
		}
		Collections.shuffle(community);
		Collections.shuffle(chance);
		final SecureRandom random = new SecureRandom();
		int reps = 10000000;
		while (reps > 0) {
			final int rollFirst = random.nextInt(4) + 1;
			final int rollSecond = random.nextInt(4) + 1;
			if (rollFirst == rollSecond) {
				doublesRolled++;
				if (doublesRolled == 3) {
					position = 10;
					doublesRolled = 0;
				}
			} else {
				doublesRolled = 0;
				position = (position + rollFirst + rollSecond) % 40;
			}
			if (position == 7 || position == 22 || position == 36) {
				final Integer card = chance.get(0);
				chance.remove(0);
				chance.add(card);
				if (card != null) {
					if (card < 40) {
						position = card;
					} else {
						if (card == 40) {
							if (position == 7) {
								position = 15;
							}
							if (position == 22) {
								position = 25;
							}
							if (position == 36) {
								position = 5;
							}
						} else if (card == 41) {
							if (position == 7) {
								position = 12;
							}
							if (position == 22) {
								position = 28;
							}
							if (position == 36) {
								position = 12;
							}
						} else if (card == 42) {
							position -= 3;
						}
					}
				}
			}
			if (position == 2 || position == 17 || position == 33) {
				final Integer card = community.get(0);
				community.remove(0);
				community.add(card);
				if (card != null) {
					position = card;
				}
			}
			if (position == 30) {
				position = 10;
			}
			visited.countUp(position);
			reps--;
		}
		final List<Entry<Integer, Integer>> list = visited.getSortedListByValue();
		return "" + list.get(0).getKey() + list.get(1).getKey() + list.get(2).getKey();
	}

	/*
	 * By counting carefully it can be seen that a rectangular grid measuring 3 by 2
	 * contains eighteen rectangles:
	 *
	 *
	 * Although there exists no rectangular grid that contains exactly two million
	 * rectangles, find the area of the grid with the nearest solution.
	 */
	public String euler85() {
		int best = 2000000;
		int result = 0;
		for (int d1 = 2; d1 < 2000; d1++) {
			for (int d2 = d1; d2 < 2000; d2++) {
				int counter = 0;
				for (int i = 1; i <= d1; i++) {
					for (int j = 1; j <= d2; j++) {
						counter += (d1 - i + 1) * (d2 - j + 1);
						if (counter > best + 2000000) {
							break;
						}
					}
				}
				if (Math.abs(2000000 - counter) < best) {
					best = Math.abs(2000000 - counter);
					result = d1 * d2;
				}
				if (counter > 2000000) {
					break;
				}
			}
		}
		return Integer.toString(result);
	}

	/*
	 * A spider, S, sits in one corner of a cuboid room, measuring 6 by 5 by 3, and
	 * a fly, F, sits in the opposite corner. By travelling on the surfaces of the
	 * room the shortest "straight line" distance from S to F is 10 and the path is
	 * shown on the diagram.
	 *
	 *
	 * However, there are up to three "shortest" path candidates for any given
	 * cuboid and the shortest route doesn't always have integer length.
	 *
	 * It can be shown that there are exactly 2060 distinct cuboids, ignoring
	 * rotations, with integer dimensions, up to a maximum size of M by M by M, for
	 * which the shortest route has integer length when M = 100. This is the least
	 * value of M for which the number of solutions first exceeds two thousand; the
	 * number of solutions when M = 99 is 1975.
	 *
	 * Find the least value of M such that the number of solutions first exceeds one
	 * million.
	 */
	public String euler86() {
		int lower = 0;
		int upper = Integer.MAX_VALUE;
		final int target = 1000000;
		while (true) {
			final int M = (int) Math.ceil((upper + lower) / 2.0);
			int counter = 0;
			for (int z = 1; z <= M && counter < target; z++) {
				for (int xy = 2; xy <= 2 * z && counter < target; xy++) {
					final double smallest = Math.sqrt(xy * xy + z * z);
					if (smallest == Math.rint(smallest)) {
						for (int x = Math.max(1, xy - z); x <= xy / 2; x++) {
							counter++;
						}
					}
				}
			}
			if (M == upper) {
				return Integer.toString(M);
			}
			if (counter < target) {
				lower = M;
			} else {
				upper = M;
			}
		}
	}

	/*
	 * The smallest number expressible as the sum of a prime square, prime cube, and
	 * prime fourth power is 28. In fact, there are exactly four numbers below fifty
	 * that can be expressed in such a way:
	 *
	 * 28 = 22 + 23 + 24 33 = 32 + 23 + 24 49 = 52 + 23 + 24 47 = 22 + 33 + 24
	 *
	 * How many numbers below fifty million can be expressed as the sum of a prime
	 * square, prime cube, and prime fourth power?
	 */
	public String euler87() {
		final int limit = 50000000;
		final int limitSqr = (int) Math.ceil(Math.sqrt(limit));
		List<Integer> primes = Library.listPrimes(limitSqr);
		final List<Integer> squares = new ArrayList<>();
		final List<Integer> cubes = new ArrayList<>();
		final List<Integer> tesseracts = new ArrayList<>();
		for (final Integer prime : primes) {
			int x = prime * prime;
			squares.add(x);
			if (x < Integer.MAX_VALUE / prime && x * prime < limit) {
				x *= prime;
				cubes.add(x);
				if (x < Integer.MAX_VALUE / prime && x * prime < limit) {
					x *= prime;
					tesseracts.add(x);
				}
			}
		}
		primes = null;
		final BitSet canSum = new BitSet();
		for (int i = 0; i < squares.size(); i++) {
			final int square = squares.get(i);
			for (int j = 0; j < cubes.size(); j++) {
				final int cube = cubes.get(j);
				for (int k = 0; k < tesseracts.size(); k++) {
					final int tesseract = tesseracts.get(k);
					final long sum = square + cube + tesseract;
					if (sum < limit) {
						canSum.set(square + cube + tesseract);
					} else {
						break;
					}
				}
			}
		}
		return Integer.toString(canSum.cardinality());
	}

	/*
	 * A natural number, N, that can be written as the sum and product of a given
	 * set of at least two natural numbers, {a1, a2, ... , ak} is called a
	 * product-sum number: N = a1 + a2 + ... + ak = a1 × a2 × ... × ak.
	 *
	 * For example, 6 = 1 + 2 + 3 = 1 × 2 × 3.
	 *
	 * For a given set of size, k, we shall call the smallest N with this property a
	 * minimal product-sum number. The minimal product-sum numbers for sets of size,
	 * k = 2, 3, 4, 5, and 6 are as follows.
	 *
	 * k=2: 4 = 2 × 2 = 2 + 2 k=3: 6 = 1 × 2 × 3 = 1 + 2 + 3 k=4: 8 = 1 × 1 × 2 × 4
	 * = 1 + 1 + 2 + 4 k=5: 8 = 1 × 1 × 2 × 2 × 2 = 1 + 1 + 2 + 2 + 2 k=6: 12 = 1 ×
	 * 1 × 1 × 1 × 2 × 6 = 1 + 1 + 1 + 1 + 2 + 6
	 *
	 * Hence for 2≤k≤6, the sum of all the minimal product-sum numbers is 4+6+8+12 =
	 * 30; note that 8 is only counted once in the sum.
	 *
	 * In fact, as the complete set of minimal product-sum numbers for 2≤k≤12 is {4,
	 * 6, 8, 12, 15, 16}, the sum is 61.
	 *
	 * What is the sum of all the minimal product-sum numbers for 2≤k≤12000?
	 */
	public String euler88() {
		long sum = 0;
		final int limit = 12000;
		final List<Integer> primes = Library.listPrimes(limit);
		final HashSet<Integer> seen = new HashSet<>();
		for (int k = 2; k <= limit; k++) {
			boolean done = false;
			for (int n = k; !done; n++) {
				done = false;
				if (Collections.binarySearch(primes, n) >= 0) {
					continue;
				}
				final List<List<Long>> possibleFactors = Library.listPossibleMultiplicants(n);
				for (final List<Long> factors : possibleFactors) {
					int sumFac = k - factors.size(); // #ones added as factors
					for (final Long factor : factors) {
						sumFac += factor;
					}
					if (sumFac == n) {
						if (!seen.contains(n)) {
							sum += n;
							seen.add(n);
						}
						done = true;
						break;
					}
				}
			}
		}
		return Long.toString(sum);
	}

	/*
	 * For a number written in Roman numerals to be considered valid there are basic
	 * rules which must be followed. Even though the rules allow some numbers to be
	 * expressed in more than one way there is always a "best" way of writing a
	 * particular number.
	 *
	 * For example, it would appear that there are at least six ways of writing the
	 * number sixteen:
	 *
	 * IIIIIIIIIIIIIIII VIIIIIIIIIII VVIIIIII XIIIIII VVVI XVI
	 *
	 * However, according to the rules only XIIIIII and XVI are valid, and the last
	 * example is considered to be the most efficient, as it uses the least number
	 * of numerals.
	 *
	 * The 11K text file, roman.txt (right click and 'Save Link/Target As...'),
	 * contains one thousand numbers written in valid, but not necessarily minimal,
	 * Roman numerals; see About... Roman Numerals for the definitive rules for this
	 * problem.
	 *
	 * Find the number of characters saved by writing each of these in their minimal
	 * form.
	 */
	public String euler89() throws IOException {
		final List<String> lines = Files.readAllLines(Paths.get("89.txt"));
		int saved = 0;
		final String[] order = new String[] { "I", "IV", "V", "IX", "X", "XL", "L", "XC", "C", "CD", "D", "CM", "M" };
		final int[] value = new int[] { 1, 4, 5, 9, 10, 40, 50, 90, 100, 400, 500, 900, 1000 };
		for (final String numeral : lines) {
			int n = 0;
			for (int i = 0; i < numeral.length(); i++) {
				String s = numeral.substring(i, Math.min(i + 2, numeral.length()));
				int pos = Library.linearSearch(order, s);
				if (pos < 0) {
					s = s.substring(0, 1);
				} else {
					i++;
				}
				pos = Library.linearSearch(order, s);
				n += value[pos];
			}
			final String optimal = Library.asRomanNumber(n);
			saved += numeral.length() - optimal.length();
		}
		return Long.toString(saved);
	}

	/*
	 * A Pythagorean triplet is a set of three natural numbers, a < b < c, for
	 * which,
	 *
	 * a2 + b2 = c2 For example, 32 + 42 = 9 + 16 = 25 = 52.
	 *
	 * There exists exactly one Pythagorean triplet for which a + b + c = 1000. Find
	 * the product abc.
	 */
	public String euler9() {
		for (int a = 1; a + a + 1 + a + 1 + 1 <= 1000; a++) {
			for (int b = a + 1; a + b + b + 1 <= 1000; b++) {
				for (int c = b + 1; a + b + c <= 1000; c++) {
					if (Math.pow(a, 2) + Math.pow(b, 2) == Math.pow(c, 2) && (a + b + c) == 1000) {
						return Long.toString(a * b * c);
					}
				}
			}
		}
		return "Fail";
	}

	/*
	 * Each of the six faces on a cube has a different digit (0 to 9) written on it;
	 * the same is done to a second cube. By placing the two cubes side-by-side in
	 * different positions we can form a variety of 2-digit numbers.
	 *
	 * For example, the square number 64 could be formed:
	 *
	 *
	 * In fact, by carefully choosing the digits on both cubes it is possible to
	 * display all of the square numbers below one-hundred: 01, 04, 09, 16, 25, 36,
	 * 49, 64, and 81.
	 *
	 * For example, one way this can be achieved is by placing {0, 5, 6, 7, 8, 9} on
	 * one cube and {1, 2, 3, 4, 8, 9} on the other cube.
	 *
	 * However, for this problem we shall allow the 6 or 9 to be turned upside-down
	 * so that an arrangement like {0, 5, 6, 7, 8, 9} and {1, 2, 3, 4, 6, 7} allows
	 * for all nine square numbers to be displayed; otherwise it would be impossible
	 * to obtain 09.
	 *
	 * In determining a distinct arrangement we are interested in the digits on each
	 * cube, not the order.
	 *
	 * {1, 2, 3, 4, 5, 6} is equivalent to {3, 6, 4, 1, 2, 5} {1, 2, 3, 4, 5, 6} is
	 * distinct from {1, 2, 3, 4, 5, 9}
	 *
	 * But because we are allowing 6 and 9 to be reversed, the two distinct sets in
	 * the last example both represent the extended set {1, 2, 3, 4, 5, 6, 9} for
	 * the purpose of forming 2-digit numbers.
	 *
	 * How many distinct arrangements of the two cubes allow for all of the square
	 * numbers to be displayed?
	 */
	public String euler90() {
		final HashSet<List<Integer>> possible = new HashSet<>();
		final List<Integer> zeroToNine = Arrays.asList(new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 });
		for (int i = 0; i <= 9; i++) {
			for (int j = 0; j <= 9 && i != j; j++) {
				for (int k = 0; k <= 9 && k != i && k != j; k++) {
					for (int l = 0; l <= 9 && l != i && l != j && l != k; l++) {
						final ArrayList<Integer> copy = new ArrayList<>(zeroToNine);
						copy.remove((Integer) i);
						copy.remove((Integer) j);
						copy.remove((Integer) k);
						copy.remove((Integer) l);
						Collections.sort(copy);
						possible.add(copy);
					}
				}
			}
		}
		int counter = 0;
		for (final List<Integer> first : possible) {
			for (final List<Integer> second : possible) {
				if (first.contains(6) && !first.contains(9)) {
					first.add(9);
				}
				if (second.contains(6) && !second.contains(9)) {
					second.add(9);
				}
				if (first.contains(9) && !first.contains(6)) {
					first.add(6);
				}
				if (second.contains(9) && !second.contains(6)) {
					second.add(6);
				}
				final boolean[] squares = new boolean[9];
				squares[0] = (first.contains(0) && second.contains(1)) || (first.contains(1) && second.contains(0));
				squares[1] = (first.contains(0) && second.contains(4)) || (first.contains(4) && second.contains(0));
				squares[2] = (first.contains(0) && second.contains(9)) || (first.contains(9) && second.contains(0));
				squares[3] = (first.contains(1) && second.contains(6)) || (first.contains(6) && second.contains(1));
				squares[4] = (first.contains(2) && second.contains(5)) || (first.contains(5) && second.contains(2));
				squares[5] = (first.contains(3) && second.contains(6)) || (first.contains(6) && second.contains(3));
				squares[6] = (first.contains(4) && second.contains(9)) || (first.contains(9) && second.contains(4));
				squares[7] = (first.contains(6) && second.contains(4)) || (first.contains(4) && second.contains(6));
				squares[8] = (first.contains(8) && second.contains(1)) || (first.contains(1) && second.contains(8));
				boolean found = true;
				for (int i = 0; i < squares.length; i++) {
					if (!squares[i]) {
						found = false;
						break;
					}
				}
				if (found) {
					counter++;
				}
			}
		}
		return Integer.toString(counter / 2);
	}

	/*
	 * The points P (x1, y1) and Q (x2, y2) are plotted at integer co-ordinates and
	 * are joined to the origin, O(0,0), to form ΔOPQ.
	 *
	 *
	 * There are exactly fourteen triangles containing a right angle that can be
	 * formed when each co-ordinate lies between 0 and 2 inclusive; that is, 0 ≤ x1,
	 * y1, x2, y2 ≤ 2.
	 *
	 *
	 * Given that 0 ≤ x1, y1, x2, y2 ≤ 50, how many right triangles can be formed?
	 */
	public String euler91() {
		final Point origin = new Point(0, 0);
		int counter = 0;
		final int target = 50;
		for (int x1 = 0; x1 <= target; x1++) {
			for (int y1 = 0; y1 <= target; y1++) {
				for (int x2 = 0; x2 <= target; x2++) {
					for (int y2 = 0; y2 <= target; y2++) {
						final Point p1 = new Point(x1, y1);
						final Point p2 = new Point(x2, y2);
						if (!p1.equals(p2) && !p1.equals(origin) && !p2.equals(origin)) {
							final double[] lengths = new double[3];
							lengths[0] = Math.sqrt(p1.x * p1.x + p1.y * p1.y);
							lengths[1] = Math.sqrt(p2.x * p2.x + p2.y * p2.y);
							lengths[2] = Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));
							Arrays.sort(lengths);
							if (Math.abs(Math.pow(lengths[0], 2) + Math.pow(lengths[1], 2)
							- Math.pow(lengths[2], 2)) < 0.0001) {
								counter++;
							}
						}
					}
				}
			}
		}
		return Integer.toString(counter / 2);
	}

	/*
	 * A number chain is created by continuously adding the square of the digits in
	 * a number to form a new number until it has been seen before.
	 *
	 * For example,
	 *
	 * 44 → 32 → 13 → 10 → 1 → 1 85 → 89 → 145 → 42 → 20 → 4 → 16 → 37 → 58 → 89
	 *
	 * Therefore any chain that arrives at 1 or 89 will become stuck in an endless
	 * loop. What is most amazing is that EVERY starting number will eventually
	 * arrive at 1 or 89.
	 *
	 * How many starting numbers below ten million will arrive at 89?
	 */
	public String euler92() {
		final int target = 10000000;
		int counter = 0;
		final int[] calculated = new int[target];
		for (int i = 1; i < target; i++) {
			int n = i;
			final ArrayList<Integer> sequence = new ArrayList<>();
			sequence.add(i);
			while (n != 1 && n != 89 && calculated[n] == 0) {
				int m = n;
				n = 0;
				while (m != 0) {
					final int x = m % 10;
					m /= 10;
					n += x * x;
				}
				sequence.add(n);
			}
			if (n == 1 || calculated[n] == 1) {
				for (final Integer seq : sequence) {
					calculated[seq] = 1;
				}
			}
			if (n == 89 || calculated[n] == 89) {
				counter++;
				for (final Integer seq : sequence) {
					calculated[seq] = 89;
				}
			}
		}
		return Integer.toString(counter);
	}

	/*
	 * By using each of the digits from the set, {1, 2, 3, 4}, exactly once, and
	 * making use of the four arithmetic operations (+, −, *, /) and
	 * brackets/parentheses, it is possible to form different positive integer
	 * targets.
	 *
	 * For example,
	 *
	 * 8 = (4 * (1 + 3)) / 2 14 = 4 * (3 + 1 / 2) 19 = 4 * (2 + 3) − 1 36 = 3 * 4 *
	 * (2 + 1)
	 *
	 * Note that concatenations of the digits, like 12 + 34, are not allowed.
	 *
	 * Using the set, {1, 2, 3, 4}, it is possible to obtain thirty-one different
	 * target numbers of which 36 is the maximum, and each of the numbers 1 to 28
	 * can be obtained before encountering the first non-expressible number.
	 *
	 * Find the set of four distinct digits, a < b < c < d, for which the longest
	 * set of consecutive positive integers, 1 to n, can be obtained, giving your
	 * answer as a string: abcd.
	 */
	//	public String euler93() {
	//		final char[] operands = new char[] { '+', '-', '*', '/' };
	//		final String[] possibleCombinations = new String[] { "(a.b),(c;d)", "((a.b),c);d", "(a.(b,c));d", "a.(b,(c;d))",
	//				"a.((b,c);d)" };
	//		int record = 1;
	//		String recordNumbers = "";
	//		for (int a = 1; a < 10; a++) {
	//			for (int b = a + 1; b < 10; b++) {
	//				for (int c = b + 1; c < 10; c++) {
	//					for (int d = c + 1; d < 10; d++) {
	//						final Set<Integer> found = new TreeSet<>();
	//						final ArrayList<String> permutations = Library.getPermutations("" + a + b + c + d);
	//						for (final String permutation : permutations) {
	//							for (final String combination : possibleCombinations) {
	//								for (int op1 = 0; op1 < 4; op1++) {
	//									for (int op2 = 0; op2 < 4; op2++) {
	//										for (int op3 = 0; op3 < 4; op3++) {
	//											String term = combination;
	//											term = term.replace('a', permutation.charAt(0));
	//											term = term.replace('b', permutation.charAt(1));
	//											term = term.replace('c', permutation.charAt(2));
	//											term = term.replace('d', permutation.charAt(3));
	//											term = term.replace('.', operands[op1]);
	//											term = term.replace(',', operands[op2]);
	//											term = term.replace(';', operands[op3]);
	//											final double result = Library.evalTerm(term);
	//											if (result % 1 == 0 && !found.contains((int) result) && result >= 1) {
	//												found.add((int) result);
	//											}
	//										}
	//									}
	//								}
	//							}
	//						}
	//						for (int i = 1;; i++) {
	//							if (found.contains(i)) {
	//								if (record < i) {
	//									record = i;
	//									recordNumbers = "" + a + b + c + d;
	//								}
	//							} else {
	//								break;
	//							}
	//						}
	//					}
	//				}
	//			}
	//		}
	//		return recordNumbers;
	//	}

	/*
	 * It is easily proved that no equilateral triangle exists with integral length
	 * sides and integral area. However, the almost equilateral triangle 5-5-6 has
	 * an area of 12 square units.
	 *
	 * We shall define an almost equilateral triangle to be a triangle for which two
	 * sides are equal and the third differs by no more than one unit.
	 *
	 * Find the sum of the perimeters of all almost equilateral triangles with
	 * integral side lengths and area and whose perimeters do not exceed one billion
	 * (1,000,000,000).
	 */
	public String euler94() {
		long sum = 0;
		final int max = 1000000000;
		for (long a = 1; 3 * a + 1 <= max; a += 2) {
			final long x1 = (3 * a + 1) * (a - 1);
			final double r1 = Math.sqrt(x1);
			if ((r1 == Math.floor(r1)) && !Double.isInfinite(r1)) {
				sum += 3 * a + 1;
			} else {
				final long x2 = (3 * a - 1) * (a + 1);
				final double r2 = Math.sqrt(x2);
				if ((r2 == Math.floor(r2)) && !Double.isInfinite(r2)) {
					sum += 3 * a - 1;
				}
			}
		}
		return Long.toString(sum);
	}

	/*
	 * The proper divisors of a number are all the divisors excluding the number
	 * itself. For example, the proper divisors of 28 are 1, 2, 4, 7, and 14. As the
	 * sum of these divisors is equal to 28, we call it a perfect number.
	 *
	 * Interestingly the sum of the proper divisors of 220 is 284 and the sum of the
	 * proper divisors of 284 is 220, forming a chain of two numbers. For this
	 * reason, 220 and 284 are called an amicable pair.
	 *
	 * Perhaps less well known are longer chains. For example, starting with 12496,
	 * we form a chain of five numbers:
	 *
	 * 12496 → 14288 → 15472 → 14536 → 14264 (→ 12496 → ...)
	 *
	 * Since this chain returns to its starting point, it is called an amicable
	 * chain.
	 *
	 * Find the smallest member of the longest amicable chain with no element
	 * exceeding one million.
	 */
	public String euler95() {
		final int limit = 1000000;
		final List<Integer> primes = Library.listPrimes((int) Math.sqrt(limit / 2));
		int longestChain = 0;
		int bestValue = 0;
		final int[] sumOfDivisors = new int[limit + 1];
		for (int i = 4; i < sumOfDivisors.length; i++) {
			if (sumOfDivisors[i] == 0) {
				final List<Integer> divisors = Library.getDivisors(i);
				int sum = 1;
				for (int j = 1; j < divisors.size() - 1; j++) {
					sum += divisors.get(j);
				}
				sumOfDivisors[i] = sum;
				for (int p = 0; p < primes.size() && primes.get(p) < i && primes.get(p) * i < limit; p++) {
					if (!divisors.contains(primes.get(p))) {
						int newSum = sum + divisors.get(divisors.size() - 1);
						for (int j = 0; j < divisors.size() - 1; j++) {
							newSum += divisors.get(j) * primes.get(p);
						}
						sumOfDivisors[i * primes.get(p)] = newSum;
					}
				}

			}
		}
		for (int i = 1; i < limit; i++) {
			final ArrayList<Integer> seen = new ArrayList<>();
			int sum = 0;
			boolean fail = false;
			int n = i;
			while (!seen.contains(i)) {
				sum = sumOfDivisors[n];
				if (sum > limit || sum < 1 || seen.contains(sum)) {
					fail = true;
					break;
				}
				seen.add(sum);
				n = sum;
			}
			if (!fail) {
				if (longestChain < seen.size()) {
					longestChain = seen.size();
					bestValue = i;
				}
			}
		}
		return Long.toString(bestValue);
	}

	/*
	 * Su Doku (Japanese meaning number place) is the name given to a popular puzzle
	 * concept. Its origin is unclear, but credit must be attributed to Leonhard
	 * Euler who invented a similar, and much more difficult, puzzle idea called
	 * Latin Squares. The objective of Su Doku puzzles, however, is to replace the
	 * blanks (or zeros) in a 9 by 9 grid in such that each row, column, and 3 by 3
	 * box contains each of the digits 1 to 9. Below is an example of a typical
	 * starting puzzle grid and its solution grid. A well constructed Su Doku puzzle
	 * has a unique solution and can be solved by logic, although it may be
	 * necessary to employ "guess and test" methods in order to eliminate options
	 * (there is much contested opinion over this). The complexity of the search
	 * determines the difficulty of the puzzle; the example above is considered easy
	 * because it can be solved by straight forward direct deduction.
	 *
	 * The 6K text file, sudoku.txt (right click and 'Save Link/Target As...'),
	 * contains fifty different Su Doku puzzles ranging in difficulty, but all with
	 * unique solutions (the first puzzle in the file is the example above).
	 *
	 * By solving all fifty puzzles find the sum of the 3-digit numbers found in the
	 * top left corner of each solution grid; for example, 483 is the 3-digit number
	 * found in the top left corner of the solution grid above.
	 */
	public String euler96() throws IOException {
		final List<String> lines = Files.readAllLines(Paths.get("96.txt"));
		String board = "";
		int result = 0;
		for (final String line : lines) {
			if (!line.contains("Grid")) {
				board += line;
				if (board.length() == 81) {
					final int[][] solution = Library.solveSudoku(board);
					result += solution[0][0] * 100 + solution[0][1] * 10 + solution[0][2];
					board = "";
				}
			}
		}
		return Integer.toString(result);
	}

	/*
	 * The first known prime found to exceed one million digits was discovered in
	 * 1999, and is a Mersenne prime of the form 26972593−1; it contains exactly
	 * 2,098,960 digits. Subsequently other Mersenne primes, of the form 2p−1, have
	 * been found which contain more digits.
	 *
	 * However, in 2004 there was found a massive non-Mersenne prime which contains
	 * 2,357,207 digits: 28433×27830457+1.
	 *
	 * Find the last ten digits of this prime number.
	 */
	public String euler97() {
		final long digits = 10000000000L;
		return BigInteger.valueOf(28433)
				.multiply(BigInteger.valueOf(2).modPow(BigInteger.valueOf(7830457), BigInteger.valueOf(digits)))
				.add(BigInteger.ONE).mod(BigInteger.valueOf(digits)).toString();
	}

	/**
	 * Comparing two numbers written in index form like 211 and 37 is not difficult,
	 * as any calculator would confirm that 211 = 2048 < 37 = 2187.
	 *
	 * However, confirming that 632382518061 > 519432525806 would be much more
	 * difficult, as both numbers contain over three million digits.
	 *
	 * Using base_exp.txt (right click and 'Save Link/Target As...'), a 22K text
	 * file containing one thousand lines with a base/exponent pair on each line,
	 * determine which line number has the greatest numerical value.
	 *
	 * NOTE: The first two lines in the file represent the numbers in the example
	 * given above.
	 */
	public String euler99() throws IOException {
		final List<String> lines = Files.readAllLines(Paths.get("99.txt"));
		double best = 0.0;
		int bestLine = 0;
		for (int i = 0; i < lines.size(); i++) {
			final String[] parts = lines.get(i).split(",");
			final double base = Double.parseDouble(parts[0]);
			final double exponent = Double.parseDouble(parts[1]);
			final double result = exponent * Math.log(base);
			if (result > best) {
				best = result;
				bestLine = i;
			}
		}
		return Integer.toString(bestLine + 1);
	}
}
