package de.oesing.all;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import javax.naming.OperationNotSupportedException;

public class OesingLibMath {

	/*
	 * D(n) = 1 for any prime p D(ab) = D(a)b + a D(b) for all integers a, b
	 * (Leibniz rule)
	 */
	public static BigInteger arithmeticDerivative(final BigInteger n, final BigInteger[] results, final List<Integer> primes) {
		if (n.compareTo(BigInteger.valueOf(results.length)) < 0 && results[n.intValueExact()] != null) {
			return results[n.intValueExact()];
		}
		if (n.isProbablePrime(100)) {
			if (n.compareTo(BigInteger.valueOf(results.length)) < 0) {
				results[n.intValueExact()] = BigInteger.ONE;
			}
			return BigInteger.ONE;
		}
		BigInteger factor1, factor2;
		for (int i = 0;; i++) {
			final int prime = primes.get(i);
			if (n.mod(BigInteger.valueOf(prime)) == BigInteger.ZERO) {
				factor1 = BigInteger.valueOf(prime);
				factor2 = n.divide(factor1);
				break;
			}
		}
		// D(a)b + a D(b)
		final BigInteger derivative = arithmeticDerivative(factor1, results,primes).multiply(factor2)
				.add(factor1.multiply(arithmeticDerivative(factor2, results,primes)));
		if (n.compareTo(BigInteger.valueOf(results.length)) < 0) {
			results[n.intValueExact()] = derivative;
		}
		return derivative;
	}

	/**
	 * @param number A string representing a number in any base. (binary,decimal..)
	 * @param base1  The starting base.
	 * @param base2  The resulting base.
	 * @return number_b1 to number_b2.
	 * @throws OperationNotSupportedException
	 */
	public static String baseConversion(final String number, final int base1, final int base2) {
		if ((base2 <= 1 || base2 >= 36) && (base1 <= 1 || base1 >= 36)) {
			throw new IllegalArgumentException("Conversion is not definied for those bases. 1 < base <= 36");
		}
		final BigInteger out = new BigInteger(number, base1);
		return out.toString(base2);
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

	/**
	 * @param functionName      The name of the function that gets evaluated.
	 * @param numberOfArguments The number of arguments the function accepts.
	 * @param currentTerm       The term that gets evaluated by
	 *                          {@link #OesingLibMath()#evalTerm(String)}.
	 * @return Returns the new term, by evaluating the given function to a value.
	 */
	private static ArrayList<String> doFunction(final String functionName, int numberOfArguments,
			final ArrayList<String> currentTerm) {
		final Method[] methods = Math.class.getMethods();
		final Double[] dArguments = new Double[numberOfArguments];
		for (int i = 0; i < numberOfArguments; i++) {
			dArguments[i] = Double.parseDouble(currentTerm.get(i));
		}
		Double result = null;
		switch (functionName) {
		case "+":
			result = dArguments[0] + dArguments[1];
			break;
		case "-":
			result = dArguments[0] - dArguments[1];
			break;
		case "*":
			result = dArguments[0] * dArguments[1];
			break;
		case "/":
			result = dArguments[0] / dArguments[1];
			break;
		case "^":
			result = Math.pow(dArguments[0], dArguments[1]);
		default:
			for (int i = 0; i < methods.length; i++) {
				if (methods[i].getName().toLowerCase().equals(functionName.toLowerCase())) {
					final Method toEvaluate = methods[i];
					try {
						result = (double) toEvaluate.invoke(null, (Object[]) dArguments);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						e.printStackTrace();
						return null;
					}
					break;
				}
			}
			break;
		}
		for (; numberOfArguments >= 0; numberOfArguments--) { // >= to also
			// remove the
			// function name
			currentTerm.remove(0);
		}
		currentTerm.add(Double.toString(result));
		return currentTerm;
	}

	/**
	 * This method only supports a few functions.
	 *
	 * @param term Any mathematical term that can be evaluated.
	 * @return Returns the result of the given term.
	 */
	public static double evalTerm(final String term) {
		final ArrayList<String> tokens = new ArrayList<>();
		final Character[] operands = { ')', '(', '+', '-', '*', '/', '^' };
		final String[] functions = { "sin", "max", "min", "tan", "cos" };
		if (term.replaceAll("\\(", "").length() != term.replaceAll("\\)", "").length()) {
			throw new IllegalArgumentException("The term has uneven number of opening and closing brackets");
		}
		final int lastDigitOperand = OesingLibArray.linearSearch(operands, term.charAt(term.length() - 1));
		if (lastDigitOperand >= 2) {
			throw new IllegalArgumentException("The term has an operand at the end");
		} else if (lastDigitOperand == 1) {
			throw new IllegalArgumentException("The term has an opening bracket at the end");
		}
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < term.length(); i++) {
			final char c = term.charAt(i);
			if (c == ',') { // Skip commas
				if (sb.length() > 0) {
					tokens.add(sb.toString());
				}
				sb.setLength(0);
				continue;
			}
			final boolean isCurrentCharOperand = (OesingLibArray.linearSearch(operands, c) >= 0);
			if (isCurrentCharOperand) {
				if (!sb.toString().equals("")) {
					tokens.add(sb.toString());
					sb.setLength(0); // Reset Stringbuilder.
				}
				tokens.add(Character.toString(c));
			} else {
				sb.append(c);
			}
		}
		if (!sb.toString().equals("")) { // If the term has no brackets but
			// there is a token left.
			tokens.add(sb.toString());
		}
		ArrayList<String> postfix = toPostFix(tokens, functions);
		while (postfix.size() != 1) {
			for (int i = 0; i < postfix.size() || postfix.size() != 1; i++) {
				final String token = postfix.get(i);
				if ("*/+-".contains(token) || OesingLibArray.linearSearch(functions, token) >= 0) {
					postfix = doFunction(token, i, postfix);
					i = -1;
				}
			}
		}
		return Double.parseDouble(postfix.get(0));
	}

	/**
	 * Using Miller-Rabin primality test in the deterministic version for all Long.
	 *
	 * @param n Long value to check if prime.
	 * @return True if its a prime.
	 */
	public static boolean isPrime(final long n) {
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

	public static void main(final String[] args) {
		System.out.println(numberToEnglish(111111353));
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
			final long millions = number/ 1000000;
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
	 * https://en.wikipedia.org/wiki/Shunting-yard_algorithm
	 *
	 * @param tokens    The tokens that gets transformed into reverse polish or
	 *                  postfix notation.
	 * @param functions The supported functions like sin,cos,max and so on.
	 * @return Returns an array of tokens in the postfix order.
	 */
	public static ArrayList<String> toPostFix(final ArrayList<String> tokens, final String[] functions) {
		// TODO max(sin(x),sin(y)) => Wrong
		final String[] operands = { "+", "-", "*", "/" };
		final HashMap<String, Integer> precedence = new HashMap<>();
		precedence.put("+", 1);
		precedence.put("-", 1);
		precedence.put("*", 2);
		precedence.put("/", 2);
		precedence.put("(", 4);
		precedence.put(")", 4);
		precedence.put("^", 3);
		// TODO: This precedence thing is ugly.
		final ArrayList<String> postfix = new ArrayList<>();
		final Stack<String> operators = new Stack<>();
		while (!tokens.isEmpty()) {
			final String token = tokens.remove(0);
			if (OesingLibArray.linearSearch(operands, token) == -1
					&& OesingLibArray.linearSearch(functions, token) == -1 && !token.equals("(")
					&& !token.equals(")")) { // Number
				postfix.add(token);
			} else if (OesingLibArray.linearSearch(functions, token) >= 0) { // Function
				operators.add(token);
			} else if (OesingLibArray.linearSearch(operands, token) >= 0) { // Operator
				final int precedenceOfToken = precedence.get(token);
				int precedenceOfOperand = 0;
				if (!operators.isEmpty()) {
					precedenceOfOperand = precedence.get(operators.peek());
				}
				while (!operators.isEmpty() && !operators.peek().equals("(")
						&& (precedenceOfToken <= precedenceOfOperand)) {
					postfix.add(operators.pop());
					if (!operators.isEmpty()) {
						precedenceOfOperand = precedence.get(operators.peek());
					} else {
						precedenceOfOperand = 0;
					}
				}
				operators.push(token);
			} else if (token.equals("(")) {
				operators.push(token);
			} else if (token.equals(")")) {
				while (!operators.isEmpty() && !operators.peek().equals("(")) {
					postfix.add(operators.pop());
				}
				if (!operators.isEmpty()) {
					operators.pop();
				}
			}
		}
		while (!operators.isEmpty()) {
			postfix.add(operators.pop());
		}
		return postfix;
	}
}
