package de.oesing.all;

import java.awt.AWTException;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.logging.LogManager;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.apache.commons.lang3.StringUtils;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.AjaxController;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.GDI32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HBITMAP;
import com.sun.jna.platform.win32.WinDef.HDC;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.RECT;
import com.sun.jna.platform.win32.WinGDI;
import com.sun.jna.platform.win32.WinGDI.BITMAPINFO;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.platform.win32.WinUser.WNDENUMPROC;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.util.Pair;
import netscape.javascript.JSObject;

/**
 * This is a collection of implementations by Benedikt Oesing.
 *
 * @author Benedikt Oesing (Benedikt[at]Oesing.de)
 * @version 1.0 Everything bunched together.
 */
public class OesingLibMain {
	public class ChatBot extends PircBot {
		public ChatBot() {
			this.setName("schnorzel");
		}

		@Override
		public void onMessage(final String channel, final String sender, final String login, final String hostname,
				final String message) {
			System.out.println(channel + ": " + sender + ": " + message);
		}
	}

	public class Config {
		// TODO
	}

	/**
	 * @see {@link OesingLibMain#htmlToSwing(String, String, File)}
	 */
	public class JavaBridge {
		public void log(final String text) {
			System.out.println(text);
		}
	}

	public static class TestSuit {

		private static Path dir = Paths.get("C:/Users/Naix/workspace/Benedikt Bibliothek/Tests");

		private final OesingLibMain lib = new OesingLibMain();

		private Method getMethod(final Class<? extends OesingLibMain> cl, final String functionName) {
			Method method = null;
			final Method[] methods = cl.getMethods();
			for (final Method m : methods) {
				if (m.getName().toLowerCase().equals(functionName.toLowerCase())) {
					method = m;
					break;
				}
			}
			return method;
		}

		/**
		 * @param functionName
		 */
		public void testFailFunction(final String functionName)
				throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			final File f = new File(dir + "/" + functionName + "Fail.txt");
			int numberOfTests = 0;
			int successful = 0;
			if (f.exists()) {
				final Method method = getMethod(lib.getClass(), functionName);
				final List<String> lines = Files.readAllLines(f.toPath());
				numberOfTests = lines.size();
				for (int i = 0; i < lines.size(); i++) {
					final String line = lines.get(i);
					final String[] inputs = line.split(",");
					final Class<?>[] inputTypes = method.getParameterTypes();
					final Object[] oInputs = new Object[inputs.length];
					for (int j = 0; j < inputTypes.length; j++) {
						oInputs[j] = lib.parseStringSecure(inputs[j], inputTypes[j]);
					}
					try {
						method.invoke(lib, oInputs);
					} catch (final Exception e) {
						System.out.println("Method " + method.getName() + " throw an exception with input: "
								+ Arrays.toString(inputs));
						continue;
					}
					lines.remove(i);
					i--;
					successful++;
				}
				System.out.println(
						functionName + ": Tested " + numberOfTests + " tests. successful completed: " + successful);
				f.delete();
				if (lines.size() > 0) {
					Files.write(f.toPath(), StringUtils.join(lines, "\n").getBytes(), StandardOpenOption.CREATE);
				}
			} else {
				System.out.println("No fails for this method recorded.");
			}
		}

		/**
		 * @param functionName
		 * @param numberOfTests
		 */
		public void testRandomInput(final String functionName, final int numberOfTests)
				throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
			int successful = 0;
			final Method method = getMethod(lib.getClass(), functionName);
			final Class<?>[] inputTypes = method.getParameterTypes();
			final Object[] args = new Object[inputTypes.length];
			for (int i = 0; i < numberOfTests; i++) {
				for (int j = 0; j < inputTypes.length; j++) {
					args[j] = OesingLibRandom.createRandom(inputTypes[j]);
				}
				System.out.println(method.getName() + " " + Arrays.toString(args));
				String result = null;
				try {
					result = "" + method.invoke(lib, args);
					writeTestResult(true, functionName, StringUtils.join(args, ",") + "|" + result);
					successful++;
				} catch (final Exception e) {
					writeTestResult(false, functionName, StringUtils.join(args, ","));
					continue;
				}
			}
			System.out.println(
					functionName + ": Tested " + numberOfTests + " tests. successful completed: " + successful);
		}

		/**
		 * @param functionName
		 */
		public void testSuccesfulFunction(final String functionName)
				throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			final File f = new File(dir + "/" + functionName + ".txt");
			int numberOfTests = 0;
			int successful = 0;
			if (f.exists()) {
				final Method method = getMethod(lib.getClass(), functionName);
				final List<String> lines = Files.readAllLines(f.toPath());
				numberOfTests = lines.size();
				for (final String line : lines) {
					final String[] inputOutput = line.split("\\|");
					final String[] inputs = inputOutput[0].split(",");
					final String output = inputOutput[1];
					final Class<?>[] inputTypes = method.getParameterTypes();
					final Object[] oInputs = new Object[inputs.length];
					for (int i = 0; i < inputTypes.length; i++) {
						oInputs[i] = lib.parseStringSecure(inputs[i], inputTypes[i]);
					}
					String result = "";
					try {
						result = "" + method.invoke(lib, oInputs);
					} catch (final Exception e) {
						System.out.println("Method " + method.getName() + " throw an exception with input: "
								+ Arrays.toString(inputs));
						continue;
					}
					if (result != null && !output.equals(result)) {
						System.out.println(
								"Method " + method.getName() + " failed with input: " + Arrays.toString(inputs));
					} else {
						successful++;
					}
				}
			} else {
				System.out.println("No file to test. Going for random inputs");
				testRandomInput(functionName, 10);
			}
			System.out.println(
					functionName + ": Tested " + numberOfTests + " tests. Sucessfull completed: " + successful);
		}

		/**
		 * @param succesful    If the function executed succesfuly.
		 * @param functionName Name of the function that gets tested.
		 * @param line         <input1,input2,..,inputX>|output
		 */
		public void writeTestResult(final boolean succesful, final String functionName, final String line)
				throws IOException {
			File f;
			if (succesful) {
				f = new File(dir + "/" + functionName + ".txt");
			} else {
				f = new File(dir + "/" + functionName + "Fail.txt");
			}
			if (!f.exists()) {
				f.createNewFile();
			}
			final List<String> lines = Files.readAllLines(f.toPath());
			Collections.sort(lines);
			if (Collections.binarySearch(lines, line) >= 0) {
				return;
			} else {
				Files.write(f.toPath(), (line + "\n").getBytes(), StandardOpenOption.APPEND);
			}
		}
	}

	@SuppressWarnings("unused")
	private final static char[] pokerStarsCode = new char[] { 0x63, 0x27, 0x26, 0x26, 0x4F, 0x35, 0x1D, 0x07, 0x19,
			0x45, 0x59, 0x21, 0x37, 0x3F, 0x00, 0x1B, 0x1B, 0x1A, 0x11, 0x1B, 0x03, 0x04, 0x4C, 0x65, 0x37, 0x00, 0x1D,
			0x1D, 0x48, 0x20, 0x0B, 0x3D, 0x45, 0x7B, 0x55, 0x36, 0x16, 0x00, 0x19, 0x00, 0x59, 0x06, 0x1C, 0x02,
			0x20 };

	private static int timeoutAfter = 120; // How many seconds of listening for
	// input before closing.
	private static boolean debug;
	private static boolean ignoreCall = false;
	private static TestSuit testSuit;

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

	public static void main(String[] args)
			throws NickAlreadyInUseException, IOException, IrcException, AWTException, InterruptedException,
			ExecutionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		final OesingLibMain lib = new OesingLibMain();
		final boolean closeAfterExecution = (args.length != 0);
		testSuit = new TestSuit();
		lib.functionShortcuts();
		JFrame javadoc = null;
		System.out.println("---------------------------------------------------------------\nCompiled at: "
				+ lib.getModifiedDate(
						Paths.get("C:/Users/Naix/workspace/Benedikt Bibliothek/bin/de/oesing/all/OesingLibrary.class"))
				+ "\nType help or ? for all functions\nType javadoc to open javadoc\nType exit to close\nType <function> <?> to see javadoc for that function\nType <function> <parameter1> <parameter2> or \"<parame terX>\"...\n---------------------------------------------------------------");
		List<String> suggestions = lib.getAllFunctions(false); // TODO Suggestions are only from LibMain.
		for (final String shortcut : lib.shortcuts.keySet()) {
			suggestions.add(shortcut);
		}
		suggestions.addAll(Arrays.asList(new String[] { "help", "?", "javadoc", "exit", "debug" }));
		while (true) {
			String line;
			if (args.length > 0) {
				line = StringUtils.join(args, " ");
				System.out.println(line);
				args = new String[0];
			} else {
				line = lib.getInput(null, "", suggestions, 2, 2, false, null, timeoutAfter);
				if (line == null) {
					System.out.println("Timeout after: " + timeoutAfter + " seconds.");
				}
			}
			if (line.equals("?") || line.toLowerCase().equals("help")) {
				lib.getAllFunctions(true);
			} else if (line.toLowerCase().equals("javadoc")) {
				if (javadoc == null || !javadoc.isVisible()) {
					javadoc = lib.htmlToSwing(null, null, new File(
							"C:/Users/Naix/workspace/Benedikt Bibliothek/doc/de/oesing/all/OesingLibrary.html"));
				} else {
					System.out.println("Javadoc already open");
				}
			} else if (line.toLowerCase().equals("exit")) {
				if (javadoc != null) {
					javadoc.dispose();
				}
				System.exit(0);
			} else if (line.toLowerCase().equals("debug")) {
				debug = !debug;
				System.out.println("Debug enabled: " + debug
						+ "\n---------------------------------------------------------------");

			} else {
				final String[] help = line.split(" ");
				String[] arguments = Arrays.copyOfRange(help, 1, help.length);
				int removed = 0;
				for (int i = 0; i < arguments.length - 1; i++) {
					if (arguments[i].substring(0, 1).equals("\"") && arguments[i + 1]
							.substring(arguments[i + 1].length() - 1, arguments[i + 1].length()).equals("\"")) {
						arguments[i] = arguments[i].substring(1) + " "
								+ arguments[i + 1].substring(0, arguments[i + 1].length() - 1);
						for (int j = i + 1; j < arguments.length - 1; j++) {
							arguments[j] = arguments[j + 1];
						}
						removed++;
					}
				}
				String outcome = null;
				arguments = Arrays.copyOfRange(arguments, 0, arguments.length - removed);
				boolean debugWasEnabled = false;
				try {
					outcome = lib.acceptInput(help[0], arguments);
					if (!ignoreCall && outcome != null) {
						// testSuit.writeTestResult(true, help[0], StringUtils.join(arguments, ",") +
						// "|" + outcome);
						if (!outcome.equals("")) {
							System.out.println(outcome);
						}
					} else {
						ignoreCall = false;
					}
				} catch (final Exception e1) {
					// testSuit.writeTestResult(false, help[0], StringUtils.join(arguments, ","));
					debugWasEnabled = (debug == true);
					debug = true;
					e1.printStackTrace();
					lib.sleep(1);
					System.out.println("Rerun in debug mode.");
					// Rerun and show debug output.
					try {
						lib.acceptInput(help[0], arguments);
					} catch (final Exception e2) {
						// Prevent double exception printing.
					}
				}
				if (outcome != null && outcome.equals("functionshortcuts")) {
					suggestions = lib.getAllFunctions(false);
					for (final String shortcut : lib.shortcuts.keySet()) {
						suggestions.add(shortcut);
					}
					suggestions.addAll(Arrays.asList(new String[] { "help", "?", "javadoc", "exit", "debug" }));
				}
				System.out.println("---------------------------------------------------------------");
			}
			if (closeAfterExecution) {
				return;
			}
		}
	}

	/**
	 * Prints a 2D Array in a block style.
	 *
	 * @param arr   A 2D Array of int.
	 * @param Print to console
	 * @return Returns the 2D Array as String.
	 */
	public static String printArray(final int[][] arr, final boolean output) {
		if (arr.length == 0 || arr[0].length == 0) {
			return "";
		}
		final StringBuilder out = new StringBuilder();
		for (int i = 0; i < arr.length - 1; i++) {
			out.append(Arrays.toString(arr[i]) + "\n");
		}
		out.append(Arrays.toString(arr[arr.length - 1]));
		if (output) {
			System.out.println(out.toString());
		}
		return out.toString();
	}

	/**
	 * Shortcut for Thread.sleep();
	 *
	 * @param seconds The number of seconds the current thread shall sleep.
	 */
	public static void sleep(final double seconds) {
		try {
			Thread.sleep((long) (seconds * 1000));
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param n        BigDecimal to calculate the root.
	 * @param decimals Number of decimals after the comma.
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
			if (debug) {
				System.out.println(a);
			}
		} while (!help.equals(a));
		return a.setScale(decimals, RoundingMode.FLOOR);
	}

	private List<HWND> windows;

	private HashMap<Integer, HashMap<Integer, HashMap<Integer, String>>> pushFold;

	private HashMap<String, String> shortcuts;

	private String consoleOut = null;

	private HashMap<String, String> hashToCard;

	private Color[] suitColors;

	/**
	 * @param functionName The name of the function that gets executed.
	 * @param args         The parameters given to the called function.
	 * @return Returns the result string.
	 */
	private String acceptInput(String functionName, final String[] args)
			throws IOException, NickAlreadyInUseException, IrcException, AWTException, InterruptedException,
			ExecutionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		String input;
		final String fullName = shortcuts.get(functionName.toLowerCase());
		if (fullName != null) {
			functionName = fullName;
		}
		if (args.length == 1 && args[0].equals("?")) {
			printJavadoc(functionName.toLowerCase());
			return null;
		}
		switch (functionName.toLowerCase()) {
		case "evalterm":
			if (args.length != 1) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 1");
			} else {
				return Double.toString(OesingLibMath.evalTerm(args[0]));
			}
			break;
		case "numbertoenglish":
			if (args.length != 1) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 1");
			} else {
				return OesingLibMath.numberToEnglish(parseStringSecure(args[0], Long.class));
			}
			break;
		case "solveluziferpuzzle":
			solveLuziferPuzzle();
			return "";
		case "guigenerate":
			if (args.length != 1) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 1");
			} else {
				guiGenerate(args[0]);
				return "";
			}
			break;
		case "binomial":
			if (args.length != 2) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 2");
			} else {
				return OesingLibMath.binomialCoefficient(parseStringSecure(args[0], Integer.class),
						parseStringSecure(args[1], Integer.class)).toString();
			}
			break;
		case "testsuccesfulfunction":
			if (args.length != 1) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 1");
			} else {
				testSuit.testSuccesfulFunction(args[0]);
				return "";
			}
			break;
		case "testfailfunction":
			if (args.length != 1) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 1");
			} else {
				testSuit.testFailFunction(args[0]);
				return "";
			}
			break;
		case "testrandominput":
			if (args.length != 2) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 2");
			} else {
				testSuit.testRandomInput(args[0], parseStringSecure(args[1], Integer.class));
				return "";
			}
			break;
		case "startlocalserver":
			if (args.length != 1) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 1");
			} else {
				startLocalServer(args[0]);
				return "";
			}
			break;
		case "encrypt":
			if (args.length < 2) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 2+");
			} else {
				encrypt(Paths.get(args[0]), args[1].toCharArray());
				return "";
			}
			break;
		case "decrypt":
			if (args.length < 2) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 2+");
			} else {
				decrypt(Paths.get(args[0]), args[1].toCharArray());
				return "";
			}
			break;
		case "windowtile":
			if (args.length != 1) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 1");
			} else {
				windowTile(args[0]);
				return "";
			}
			break;
		case "pokerrankhands":
			if (args.length == 0) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 1+");
			} else if (args.length == 1) {
				return pokerRankHands(args[0], 100, null).toString();
			} else if (args.length == 2) {
				return pokerRankHands(args[0], parseStringSecure(args[1], Integer.class), null).toString();
			} else {
				return pokerRankHands(args[0], parseStringSecure(args[1], Integer.class),
						Arrays.asList(Arrays.copyOfRange(args, 2, args.length))).toString();
			}
			break;
		case "pokerequity":
			if (args.length == 0 || args.length > 2) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 1.");
			} else if (args.length == 1) {
				return pokerEquity(args[0], "");
			} else if (args.length == 2) {
				return pokerEquity(args[0], args[1]);
			}
			break;
		case "pokerodds":
			if (args.length == 0 || args.length > 2) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 1.");
			} else if (args.length == 1) {
				return pokerOdds(args[0], "");
			} else if (args.length == 2) {
				return pokerOdds(args[0], args[1]);
			}
			break;
		case "functionshortcuts":
			functionShortcuts();
			return fullName;
		case "windowscreenshot":
			if (args.length != 1) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 1.");
			} else {
				imageSave("C:/Users/Naix/Desktop/" + System.currentTimeMillis(),
						windowScreenshot(windowGetHandles(args[0].toLowerCase()).get(0)), "png");
				return "";
			}
			break;
		case "imagetoascii":
			String art;
			switch (args.length) {
			case 2:
				art = imageToAscii(ImageIO.read(new File(args[0])), args[1], 4, "Serif");
				Files.write(Paths.get(args[0]).getParent().resolve("art.txt"), art.getBytes(),
						StandardOpenOption.CREATE);
				return "";
			case 3:
				art = imageToAscii(ImageIO.read(new File(args[0])), args[1], parseStringSecure(args[2], Integer.class),
						"Serif");
				Files.write(Paths.get(args[0]).getParent().resolve("art.txt"), art.getBytes(),
						StandardOpenOption.CREATE);
				return "";
			case 4:
				art = imageToAscii(ImageIO.read(new File(args[0])), args[1], parseStringSecure(args[2], Integer.class),
						args[3]);
				Files.write(Paths.get(args[0]).getParent().resolve("art.txt"), art.getBytes(),
						StandardOpenOption.CREATE);
				return "";
			default:
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 2..4.");
			}
			break;
		case "printjavadoc":
			if (args.length != 1) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 1.");
			} else {
				printJavadoc(args[0].toLowerCase());
				return "";
			}
			break;
		case "longestcommonsubstring":
			if (args.length != 2) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 2.");
			} else {
				return "" + longestCommonSubstring(args[0], args[1]);
			}
			break;
		case "youtubedownload":
			if (args.length != 1) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 1.");
			} else {
				youtubeDownload(args[0]);
				return "";
			}
			break;
		case "editdistance":
			if (args.length != 2 && args.length != 5) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 2/5");
			} else {
				if (args.length == 2) {
					return "" + OesingLibString.editDistance(args[0], args[1], -1, -1, -1);
				} else {
					return "" + OesingLibString.editDistance(args[0], args[1],
							parseStringSecure(args[2], Integer.class), parseStringSecure(args[3], Integer.class),
							parseStringSecure(args[4], Integer.class));
				}
			}
			break;
		case "htmlrecordstream":
			htmlRecordStream(args);
			return "";
		case "pokerpreflopadvice":
			if (args.length > 5 || args.length < 1) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 1..5");
			} else {
				return pokerPreFlopAdvice(args);
			}
			break;
		case "pokerpushfoldadvice":
			if (args.length != 3) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 3.");
			} else {
				return pokerPushFoldAdvice(parseStringSecure(args[0], Double.class),
						parseStringSecure(args[1], Double.class), parseStringSecure(args[2], Integer.class));
			}
			break;
		case "reversestring":
			if (args.length != 1) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 1.");
			} else {
				return reverseString(args[0]);
			}
			break;
		case "imagesteganography":
			if (args.length != 3) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 3.");
			} else {
				imageSteganography(parseStringSecure(args[0], Boolean.class), args[1], args[2]);
				return "";
			}
			break;
		case "imageresize":
			if (args.length != 3) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 3.");
			} else {
				BufferedImage out;
				out = imageResize(ImageIO.read(new File(args[0])), parseStringSecure(args[1], Integer.class),
						parseStringSecure(args[2], Integer.class));
				imageSave(addSuffixToPath(args[0], "Resize"), out, getFileEndingOfPath(args[0]));
				return "";
			}
			break;
		case "htmlbuildrequest":
			if (args.length != 4) {
				System.out.println("Wrong number of parameters. Found " + args.length
						+ " but need 4.\nIts <targetURL> <POST/GET> <requestBody> <urlParamters>");
			} else {
				return htmlBuildRequest(args[0], args[1], args[2], args[3]);
			}
			break;
		case "htmlgetviaurlandselectors":
			if (args.length < 2) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 2+.");
			} else {
				return htmlGetViaUrlAndSelectors(args[0], Arrays.asList(args)).toString();
			}
			break;
		case "htmlgetviaurlassoup":
			if (args.length != 1) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 1.");
			} else {
				return htmlGetViaUrlAsSoup(args[0]).toString();
			}
			break;
		case "findallregex":
			if (args.length != 2) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 2.");
			} else {
				return findAllRegex(args[0], args[1]).toString();
			}
			break;
		case "imagedrawviamouse":
			if (args.length != 1) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 1.");
			} else {
				imageDrawViaMouse(args[0]);
				return "";
			}
			break;
		case "htmlnewepisodes":
			htmlNewEpisodes();
			return "";
		case "evalCode":
			if (args.length == 0) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 1+.");
			} else {
				evalCode(String.join(" ", args));
				return "";
			}
			break;
		case "executecmd":
			if (args.length == 0) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 1+.");
			} else {
				executeCmd(true, args);
				return "";
			}
			break;
		case "twitchstart":
			twitchStart();
			return "";
		case "twitchchat":
			if (args.length != 1) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 1");
			} else {
				twitchChat(args);
				return "";
			}
			break;
		case "approxsquareroot":
			if (args.length > 2 || args.length == 0) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 2.");
			} else if (args.length == 1) {
				return "" + squareRoot(new BigDecimal(args[0]), 2);
			} else if (args.length == 2) {
				return "" + squareRoot(new BigDecimal(args[0]), parseStringSecure(args[1], Integer.class));
			}
			break;
		case "baseconversion":
			if (args.length != 3) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 3.");
			} else {
				return "" + OesingLibMath.baseConversion(args[0], parseStringSecure(args[1], Integer.class),
						parseStringSecure(args[2], Integer.class));
			}
			break;
		case "binaryto32bit":
			if (args.length != 1) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 1.");
			} else {
				return "" + binaryTo32Bit(args[0]);
			}
			break;
		case "colordistance":
			input = getInput(null, "give colors like this: <r1,g1,b1,r2,g2,b2>", null, 0, 0, false, null, 0);
			final String[] parts = input.split(",");
			return "" + OesingLibImage.colorDistance(
					new Color(parseStringSecure(parts[0], Integer.class), parseStringSecure(parts[1], Integer.class),
							parseStringSecure(parts[2], Integer.class)),
					new Color(parseStringSecure(parts[3], Integer.class), parseStringSecure(parts[4], Integer.class),
							parseStringSecure(parts[5], Integer.class)));
		case "digitsum":
			if (args.length != 1) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 1.");
			} else {
				return "" + digitSum(new BigInteger(args[0]));
			}
			break;
		case "ggt":
			if (args.length != 2) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 2.");
			} else {
				return "" + ggT(new BigInteger(args[0]), new BigInteger(args[1]));
			}
			break;
		case "htmlgetviaurlaslist":
			if (args.length != 2) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 2.");
			} else {
				return htmlGetViaUrlAsList(args[0], Boolean.getBoolean(args[1])).toString();
			}
			break;
		case "htmlgetviaurlasstring":
			if (args.length != 2) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 2.");
			} else {
				return htmlGetViaUrlAsString(args[0], Boolean.getBoolean(args[1]));
			}
			break;
		case "htmltoswing":
			input = getInput(null, "what do you want as input? <html,url,file>",
					Arrays.asList(new String[] { "html", "url", "file" }), 0, 0, false, null, 0);
			if (input.equals("html")) {
				input = getInput(null, "type your html-code", null, 0, 0, false, null, 0);
				htmlToSwing(null, input, null);
				return "";
			}
			if (input.equals("url")) {
				input = getInput(null, "type your url", null, 0, 0, false, null, 0);
				htmlToSwing(input, null, null);
				return "";
			}
			if (input.equals("file")) {
				input = getInput(null, "type your path", null, 0, 0, false, null, 0);
				htmlToSwing(null, null, new File(input));
				return "";
			}
			break;
		case "htmlwget":
			if (args.length == 1) {
				htmlWget(args[0], null);
				return "";
			} else if (args.length == 2) {
				htmlWget(args[0], args[1]);
				return "";
			} else {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 1/2.");
			}
			break;
		case "imageaddspace":
			if (args.length != 4) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 4.");
			} else {
				imageAddSpace(ImageIO.read(new File(args[0])), parseStringSecure(args[1], Integer.class),
						parseStringSecure(args[2], Integer.class),
						new Color(parseStringSecure(args[3], Integer.class)));
				return "";
			}
			break;
		case "imagecalculatematch":
			if (args.length != 2) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 2.");
			} else {
				return "" + imageMatch(ImageIO.read(new File(args[0])), ImageIO.read(new File(args[1])), null);
			}
			break;
		case "imagecut":
			if (args.length != 5) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 5.");
			} else {
				final Rectangle rect = new Rectangle(parseStringSecure(args[1], Integer.class),
						parseStringSecure(args[2], Integer.class), parseStringSecure(args[3], Integer.class),
						parseStringSecure(args[4], Integer.class));
				final BufferedImage img = imageSubImage(ImageIO.read(new File(args[0])), rect);
				final int dot = args[0].indexOf('.');
				final String format = args[0].substring(dot + 1);
				ImageIO.write(img, format, new File(addSuffixToPath(args[0], "Cut")));
				return "";
			}
			break;
		case "imagedrawfrompixels":
			if (args.length < 2) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 2+.");
			} else {
				final File f = new File(args[0]);
				final HashSet<Point> set = new HashSet<>();
				for (int i = 1; i < args.length; i++) {
					final String[] xy = args[i].replaceAll("[^0-9,]", "").split(",");
					set.add(new Point(Integer.parseInt(xy[0]), Integer.parseInt(xy[1])));
				}
				if (f.exists()) {
					final BufferedImage img = imageDrawFromPixels(set, ImageIO.read(f));
					ImageIO.write(img, getFileEndingOfPath(args[0]), new File(addSuffixToPath(args[0], "Draw")));
				} else {
					final BufferedImage img = imageDrawFromPixels(set, null);
					ImageIO.write(img, ".png", f);
				}
				return "";
			}
			break;
		case "imagejoin":
			if (args.length != 2) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 2.");
			} else {
				BufferedImage out;
				out = imageJoin(ImageIO.read(new File(args[0])), ImageIO.read(new File(args[1])));
				ImageIO.write(out, getFileEndingOfPath(args[0]), new File(addSuffixToPath(args[0], "Join")));
				return "";
			}
			break;
		case "imagemostdominantcolor":
			if (args.length != 1) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 1.");
			} else {
				final Color c = imageMostDominantColor(ImageIO.read(new File(args[0])));
				return "" + c.getRed() + "," + c.getGreen() + "," + c.getBlue() + "," + c.getRGB();
			}
			break;
		case "imagetoblackandwhite":
			if (args.length != 1) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 1.");
			} else {
				final BufferedImage out = imageToBlackAndWhite(ImageIO.read(new File(args[0])));
				ImageIO.write(out, getFileEndingOfPath(args[0]), new File(addSuffixToPath(args[0], "Black")));
				return "";
			}
			break;
		case "ispalindrom":
			if (args.length != 1) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 1.");
			} else {
				return "" + isPalindrom(new BigInteger(args[0]));
			}
			break;
		case "ispandigital":
			if (args.length != 3) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 1.");
			} else {
				return "" + isPandigital(args[0], parseStringSecure(args[1], Boolean.class));
			}
			break;
		case "ispermutation":
			if (args.length != 2) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 2.");
			} else {
				return "" + isPermutation(args[0], args[1]);
			}
			break;
		case "listprimes":
			if (args.length == 1) {
				return listPrimes(parseStringSecure(args[0], Integer.class)).toString();
			} else if (args.length == 2) {
				return listPrimes(parseStringSecure(args[0], Integer.class), parseStringSecure(args[1], Integer.class))
						.toString();
			} else {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 1/2.");
			}
			break;
		case "normalize":
			if (args.length != 3) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 3.");
			} else {
				return "" + normalize(parseStringSecure(args[0], Double.class),
						parseStringSecure(args[1], Double.class), parseStringSecure(args[2], Double.class));
			}
			break;
		case "phi":
			if (args.length != 1) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 1.");
			} else {
				return "" + phi(parseStringSecure(args[0], Integer.class));
			}
			break;
		case "primefactorization":
			if (args.length != 1) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 1.");
			} else {
				return Arrays.toString(primeFactorization(parseStringSecure(args[0], Integer.class), null));
			}
			break;
		case "processrunning":
			if (args.length != 2) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 2.");
			} else {
				return "" + processRunning(args[0], parseStringSecure(args[1], Boolean.class));
			}
			break;
		case "solvesudoku":
			if (args.length != 1) {
				System.out.println("Wrong number of parameters. Found " + args.length + " but need 1.");
			} else {
				return "" + printArray(solveSudoku(args[0]), false);
			}
			break;
		default:
			System.out.println(functionName + " " + Arrays.toString(args) + " not supported");
		}
		return null;
	}

	/**
	 * @param path   The Filepath which gets changed.
	 * @param suffix The suffix we want to add.
	 * @return The input path with a suffix added.
	 */
	private String addSuffixToPath(final String path, final String suffix) {
		final int dot = path.indexOf('.');
		final String format = path.substring(dot + 1);
		return path.replace("." + format, suffix + "." + format);
	}

	/**
	 * @param number A binary number as String.
	 * @return 32bit Blocks as 32Bit number seperated by "-".
	 */
	private String binaryTo32Bit(String number) {
		String result = "";
		while (number.length() > 0) {
			result += OesingLibMath.baseConversion(number.substring(0, Math.min(number.length(), 32)), 2, 32) + "-";
			number = number.substring(Math.min(number.length(), 32));
		}
		return result.substring(0, result.length() - 1);
	}

	/**
	 * @param board The Sodoku Board.
	 * @param pos   The position on the Board.
	 * @param value The value (1-9) for given Position.
	 * @return Returns if the value already exists in the 3x3 box.
	 */
	private boolean boxCheck(final int[][] board, final int pos, final int value) {
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
	 * @param board The Sodoku Board.
	 * @param pos   The position on the Board.
	 * @param value The value (1-9) for given Position.
	 * @return Returns if the value can be inserted into given position.
	 */
	private boolean checkIfCorrect(final int[][] board, final int pos, final int value) {
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
	 * Uses linearsearch to find the index containing the number closed to the
	 * Inputvalue.
	 *
	 * @param value The value that gets searched for.
	 * @param arr   Input array.
	 * @return Index in arr.
	 */
	private int closestValue(final double value, final double[] arr) {
		double maxDistance = Math.abs(value - arr[0]);
		int pos = 0;
		for (int i = 1; i < arr.length; i++) {
			final double d = Math.abs(value - arr[i]);
			if (d < maxDistance) {
				maxDistance = d;
				pos = i;
			}
		}
		return pos;
	}

	public long concatLongs(long a, final long b) {
		long c = b;
		while (c > 0) {
			a *= 10;
			c /= 10;
		}
		return a + b;
	}

	/**
	 * @param current Current progress.
	 * @param max     Maximal achievable progress.
	 */
	private void consoleProgressBar(final int current, int max) {
		max -= 1;
		final int percent = (int) ((current * 1.0 / max * 1.0) * 100);
		final int lengthOfBar = 100;
		final StringBuilder sb = new StringBuilder();
		sb.append(percent + "%" + " [");
		for (int i = 0; i < lengthOfBar; i++) {
			if (i < (percent / (100 / lengthOfBar))) {
				sb.append("#");
			} else {
				sb.append(" ");
			}
		}
		sb.append("]");
		if (percent == 100) {
			sb.append(" Done!");
		}
		consoleUpdate(sb.toString());
		if (current == max + 1) {
			System.out.println();
			consoleOut = null;
		}
	}

	/**
	 * Prints a line into console and sets the pointer to the start of that line,
	 * this way the line can be overwritten. Only works for single line.
	 *
	 * @param line The line that gets printed.
	 */
	private void consoleUpdate(final String line) {
		if (line != null && line.equals(consoleOut)) {
			return;
		}
		consoleOut = line;
		final StringBuilder sb = new StringBuilder();
		for (int j = 0; j < consoleOut.length() + 2; j++) {
			sb.append(" ");
		}
		final String empty = sb.toString();
		System.out.print("\r" + empty);
		System.out.print("\r" + line);
	}

	public int countDigits(BigInteger n) {
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

	public int countDigits(long n) {
		int count = 0;
		while (n > 0) {
			n /= 10;
			count++;
		}
		return count;
	}

	/**
	 * Typical XOR decryption.
	 *
	 * @param path The path to the file that gets decrypted.
	 * @param code The values that gets used for XOR´ing.
	 */
	public void decrypt(final Path path, final char[] code) throws IOException {
		final byte[] text = Files.readAllBytes(path);
		final byte[] out = new byte[text.length];
		for (int i = 0; i < text.length; i++) {
			final byte b = (byte) (text[i] ^ code[i % code.length]);
			if (debug) {
				System.out.println(text[i] + " xor " + code[i % code.length] + " = " + b);
			}
			out[i] = b;
		}
		final FileOutputStream fos = new FileOutputStream(addSuffixToPath(path.toString(), "decryp"));
		fos.write(out);
		fos.close();
	}

	/**
	 * @see #digitSum(int)
	 * @see #digitSum(long)
	 * @param number BigInteger to get the digit sum from.
	 * @return Sum of all individual integers forming a number.
	 */
	public int digitSum(final BigInteger number) {
		return digitSum(number.toString());
	}

	/**
	 * @see #digitSum(long)
	 * @see #digitSum(BigInteger)
	 * @param number Integer to get the digit sum from.
	 * @return Sum of all individual integers forming a number.
	 */
	public int digitSum(final int number) {
		return digitSum("" + number);
	}

	/**
	 * @see #digitSum(int)
	 * @see #digitSum(BigInteger)
	 * @param number Long to get the digit sum from.
	 * @return Sum of all individual integers forming a number.
	 */
	public int digitSum(final long number) {
		return digitSum("" + number);
	}

	/**
	 * @see #digitSum(int)
	 * @see #digitSum(long)
	 * @see #digitSum(BigInteger)
	 * @param s String to get the digit sum from.
	 * @return Sum of all individual integers forming a number.
	 */
	public int digitSum(final String s) {
		int sum = 0;
		for (final Character c : s.toCharArray()) {
			sum += Character.getNumericValue(c);
			if (debug) {
				System.out.println(sum + " " + Character.getNumericValue(c));
			}
		}
		return sum;
	}

	/**
	 * Downloads preflopcharts from: http://www.pokersnowie.com/pftapp/index.html
	 * and alters them for perfect use.
	 */
	@SuppressWarnings("unused")
	private void downloadPreflopAdvice() throws MalformedURLException, IOException {
		final String url = "http://www.pokersnowie.com/pftapp/tables/";
		final String[] keywords = new String[] { "UTG_Open.html", "HJ_Open.html", "CO_Open.html", "BTN_Open.html",
				"SB_Open.html", "HJ_UTGR2.5BB.html", "HJ_UTGR3BB.html", "CO_UTGR2.5BB.html", "CO_UTGR3BB.html",
				"CO_HJR2.5BB.html", "CO_HJR3BB.html", "BTN_UTGR2.5BB.html", "BTN_UTGR3BB.html", "BTN_HJR2.5BB.html",
				"BTN_HJR3BB.html", "BTN_COR2.5BB.html", "BTN_COR3BB.html", "SB_UTGR2.5BB.html", "SB_UTGR3BB.html",
				"SB_HJR2.5BB.html", "SB_HJR3BB.html", "SB_COR2.5BB.html", "SB_COR3BB.html", "SB_BTNR2.5BB.html",
				"SB_BTNR3BB.html", "BB_UTGR2.5BB.html", "BB_UTGR3BB.html", "BB_HJR2.5BB.html", "BB_HJR3BB.html",
				"BB_COR2.5BB.html", "BB_COR3BB.html", "BB_BTNR2.5BB.html", "BB_BTNR3BB.html", "BB_SBR2.5BB.html",
				"BB_SBR3BB.html", "SB_UTGR3BBC2nd.html", "SB_UTGR3BBC3rd.html", "SB_HJR3BBC3rd.html",
				"BB_UTGR3BBC2nd.html", "BB_UTGR3BBCSB.html", "BB_UTGR3BBC3rd.html", "BB_HJR3BBCSB.html",
				"BB_HJR3BBC3rd.html", "BB_COR3BBCSB.html", "BB_UTGR3BBC.html", "BB_HJR3BBC.html", "BB_COR3BBC.html",
				"BB_BTNR3BBC.html", "SB_UTGR3BBC.html", "SB_HJR3BBC.html", "SB_COR3BBC.html", "UTG_HJRPOT.html",
				"UTG_CORPOT.html", "UTG_BTNRPOT.html", "UTG_SBRPOT.html", "UTG_BBRPOT.html", "HJ_UTGRPOT.html",
				"HJ_CORPOT.html", "HJ_BTNRPOT.html", "HJ_SBRPOT.html", "HJ_BBRPOT.html", "CO_UTGRPOT.html",
				"CO_HJRPOT.html", "CO_BTNRPOT.html", "CO_SBRPOT.html", "CO_BBRPOT.html", "BTN_UTGRPOT.html",
				"BTN_HJRPOT.html", "BTN_CORPOT.html", "BTN_SBRPOT.html", "BTN_BBRPOT.html", "SB_UTGRPOT.html",
				"SB_HJRPOT.html", "SB_CORPOT.html", "SB_BTNRPOT.html", "SB_BBRPOT.html", "BB_UTGRPOT.html",
				"BB_HJRPOT.html", "BB_CORPOT.html", "BB_BTNRPOT.html", "BB_SBRPOT.html" };
		for (final String key : keywords) {
			final StringBuilder sb = new StringBuilder();
			String line;
			final HttpURLConnection connection = (HttpURLConnection) new URL(url + key).openConnection();
			connection.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");
			connection.setRequestMethod("GET");
			final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			boolean firstDiv = true;
			while ((line = reader.readLine()) != null) {
				if (line.contains("div") && firstDiv) {
					line = reader.readLine();
					line = reader.readLine();
					line = reader.readLine();
					firstDiv = false;
				}
				sb.append(line.replaceAll("<td", "<td onclick=\"a(this.innerHTML,this.className)\"") + '\n');
			}
			reader.close();
			final String output = "<link href=\"./e80f250a.css\" rel=\"stylesheet\" type=\"text/css\" media=\"all\">"
					+ "\n"
					+ "<script>function a(percentage,action){\nif(percentage !== undefined && percentage != \"\"){\n var x = Math.round(Math.random() * 100+1);\nif(x <= percentage){\nconsole.log(\"raise \" + x + \"/\" + percentage);\n} else{\naction == \"c\" ? console.log(\"call \"  + x + \"/\" + percentage) : console.log(\"fold \" + x + \"/\" + percentage);\n}\n}\n}\n</script>\n"
					+ sb.toString();
			Files.write(Paths.get("D:/BotPics/" + "Preflop/" + key), output.getBytes(), StandardOpenOption.CREATE);
		}
	}

	/**
	 * Typical XOR encryption.
	 *
	 * @param path The path to the file that gets encrypted.
	 * @param code The values that gets used for XOR´ing.
	 */
	public void encrypt(final Path path, final char[] code) throws IOException {
		final byte[] text = Files.readAllBytes(path);
		final byte[] out = new byte[text.length];
		for (int i = 0; i < text.length; i++) {
			final byte b = (byte) (text[i] ^ code[i % code.length]);
			if (debug) {
				System.out.println(text[i] + " xor " + code[i % code.length] + " = " + b);
			}
			out[i] = b;
		}
		final FileOutputStream fos = new FileOutputStream(addSuffixToPath(path.toString(), "cryp"));
		fos.write(out);
		fos.close();
	}

	/**
	 * This method is risky to use. It poses a security risk so use with discretion.
	 * Surrounds the given code with a temporary class file, compiles it and
	 * executes the code.
	 *
	 * @param code Any Javacode. Printing to output is necessary.
	 */
	public void evalCode(final String code) throws IOException {
		System.out.println("NO");
		System.exit(1337);
		final File temporary = File.createTempFile("eval", ".java");
		final String className = temporary.getName().replace(".java", "");
		final String surroundingCode = "import java.util.*;" + "import java.math.*;" + "import java.nio.file.Files;"
				+ "import java.nio.file.Paths;" + "public class " + className + " {"
				+ "public static void main(String[] args){ try{" + code + "}catch(Exception e){e.printStackTrace();}}"
				+ "}";
		final FileWriter writer = new FileWriter(temporary);
		writer.write(surroundingCode);
		writer.close();
		final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		compiler.run(null, null, null, temporary.getPath());
		executeCmd(true, " cd C:/Users/Naix/AppData/Local/Temp & java " + className);
	}

	/**
	 * @param output   If true output gets printed to console.
	 * @param commands The commands that get executed.
	 * @return Outputs the console.
	 */
	public String executeCmd(final boolean output, final String... commands) throws IOException {
		final StringBuilder sb = new StringBuilder();
		final String[] cmdCommands = new String[commands.length + 2];
		System.arraycopy(commands, 0, cmdCommands, 2, commands.length);
		cmdCommands[0] = "cmd.exe";
		cmdCommands[1] = "/C";
		final Runtime rt = Runtime.getRuntime();
		Process proc;
		proc = rt.exec(cmdCommands);

		BufferedReader inPut = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		// read the output from the command
		String line;
		while ((line = inPut.readLine()) != null) {
			if (!line.equals("")) {
				sb.append(line + "\n");
				if (output) {
					System.out.println(line);
				}
			}
		}
		// read error from the command
		inPut = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
		boolean error = false;
		while ((line = inPut.readLine()) != null) {
			if (!error) {
				error = true;
				System.out.println("Error:");
			}
			sb.append(line + "\n");
			if (output) {
				System.out.println(line);
			}
		}
		inPut.close();
		return sb.toString();
	}

	/**
	 * @param text  The text that gets parsed.
	 * @param regex The regex that is used to parse the text.
	 * @return Returns all found Strings that gets returned by the regex.
	 */
	public List<String> findAllRegex(final String text, final String regex) {
		final List<String> streams = new ArrayList<>();
		final Pattern p = Pattern.compile(regex);
		final Matcher m = p.matcher(text);
		while (m.find()) {
			streams.add(m.group());
		}
		return streams;
	}

	/**
	 * This method is used to initialize and alter a dictionary for shortcuts.
	 */
	public void functionShortcuts() throws IOException {
		final Path path = Paths.get("").toAbsolutePath().resolve("shortcuts.txt");
		if (shortcuts == null) {
			shortcuts = new HashMap<>();
			final List<String> shorts = Files.readAllLines(path);
			for (final String line : shorts) {
				final String[] parts = line.split(":");
				shortcuts.put(parts[1].toLowerCase(), parts[0].toLowerCase());
			}
			if (!Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
				Files.write(path, "".getBytes(), StandardOpenOption.CREATE);
				openFile(path, "Enter lines this way \n<functionName>:<shortcut>");
			}
		} else {
			openFile(path, "Enter lines this way \n<functionName>:<shortcut>");
			shortcuts = null;
			functionShortcuts();
		}
		if (debug) {
			System.out.println(shortcuts);
		}
	}

	/**
	 * Prints out all Methods that this class implements.
	 *
	 * @param print If true prints the functions to console.
	 * @return Returns a list of all public methods of this class, excluding lambda
	 *         functions.
	 */
	private List<String> getAllFunctions(final boolean print) {
		final List<String> out = new ArrayList<>();
		final Method[] methods = this.getClass().getDeclaredMethods();
		Arrays.sort(methods, new Comparator<Method>() {

			@Override
			public int compare(final Method m1, final Method m2) {
				return m1.getName().compareTo(m2.getName());
			}
		});
		for (final Method m : methods) {
			if (Modifier.isPrivate(m.getModifiers()) || m.getName().equals("main")) {
				continue;
			}
			if (print) {
				final StringBuilder sb = new StringBuilder();
				sb.append(m.getName());
				sb.append("(");
				final Parameter[] parameters = m.getParameters();
				for (final Parameter p : parameters) {
					sb.append(p.getType().getSimpleName() + ",");
				}
				if (parameters.length > 0) {
					sb.deleteCharAt(sb.length() - 1);
				}
				sb.append(")");
				sb.append(":");
				sb.append(m.getReturnType().getSimpleName());
				System.out.println(sb.toString());
			}
			out.add(m.getName());
		}
		return out;
	}

	/**
	 * @param pos The position on the Board.
	 * @return The 3x3 box the value is in.
	 */
	private int getBox(final int pos) {
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
	 * @param order
	 * @return Returns an Comparator that allows comparison on a given order.
	 */
	private <T> Comparator<T> getComparatorForGivenOrder(final List<T> order) {
		final Comparator<T> comp = new Comparator<T>() {
			@Override
			public int compare(final T o1, final T o2) {
				final int pos1 = order.indexOf(o1);
				final int pos2 = order.indexOf(o2);
				if (pos1 < 0 || pos2 < 0) {
					throw new IllegalArgumentException(o1 + "," + o2);
				}
				return Integer.compare(pos1, pos2);
			}
		};
		return comp;
	}

	/**
	 * @param n Number that gets all proper divisors calculated
	 * @return Returns a list from 1 to n inclusive that contains all divisors.
	 */
	public List<Integer> getDivisors(final int n) {
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
	 * @param path The Filepath to parse for Fileending.
	 * @return The Fileending of the input. (txt,mp4,exe..)
	 */
	private String getFileEndingOfPath(final String path) {
		final int dot = path.indexOf('.');
		final String format = path.substring(dot + 1);
		return format;
	}

	private File[] getFilesInDirectory(final String directory) {
		final File f = new File(directory);
		return f.listFiles();
	}

	/**
	 * @see #getInput(Scanner, String, List, int, int, boolean, String, double)
	 * @return Uses System.in to get input from console
	 * @throws IOException
	 */
	public String getInput() throws IOException {
		return getInput(null, "", null, 0, 0, false, "", 0);
	}

	/**
	 *
	 * @param scan                          Scanner for console input, can be null.
	 * @param message                       Message that gets printed before asking
	 *                                      for input.
	 * @param acceptedInput                 A list of accepted inputs.
	 * @param suggestionsByDistance         Adds n values from the acceptedInput
	 *                                      list into suggestions by edit distance
	 *                                      to the input.
	 * @param suggestionsByLongestSubstring Adds n values from the acceptedInput
	 *                                      list into suggestions by calculating the
	 *                                      word with the longest substring.
	 * @param suggestionsGiven              Indicates if the user has made a mistake
	 *                                      and has been given suggestions.
	 * @param lastInput                     Stores the input from the original call.
	 * @param timeoutAfter                  Returns null after given amount of
	 *                                      seconds with 500ms precision. Zero
	 *                                      equals infinite time.
	 * @return Uses System.in to get input from console
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public String getInput(Scanner scan, final String message, final List<String> acceptedInput,
			final int suggestionsByDistance, final int suggestionsByLongestSubstring, final boolean suggestionsGiven,
			final String lastInput, final double timeoutAfter) throws IOException {
		if (scan == null) {
			scan = new Scanner(System.in);
		}
		if (message != null && message.length() > 0) {
			System.out.println(message);
		}
		String line;
		if (acceptedInput != null && acceptedInput.size() > 0) {
			if (timeoutAfter == 0.0) {
				line = scan.nextLine();
			} else {
				String in = "";
				final Scanner fscan = scan;
				final FutureTask<String> readLineTask = new FutureTask<>(new Callable<String>() {
					@Override
					public String call() throws Exception {
						return fscan.nextLine();
					}
				});
				final ExecutorService exe = Executors.newFixedThreadPool(1);
				exe.execute(readLineTask);
				try {
					in = readLineTask.get((int) timeoutAfter, TimeUnit.SECONDS);
				} catch (final TimeoutException | InterruptedException | ExecutionException e) {
					return null;
				}
				line = in;
			}
			if (suggestionsGiven) {
				if (line.matches("\\d+") && Integer.parseInt(line) < acceptedInput.size()) {
					final String[] words = lastInput.split(" ", 2);
					words[0] = acceptedInput.get(Integer.parseInt(line));
					final String out = StringUtils.join(words, " ");
					System.out.println(out);
					return out;
				} else {
					return line;
				}
			}
			Collections.sort(acceptedInput, String.CASE_INSENSITIVE_ORDER);
			final int position = Collections.binarySearch(acceptedInput, line.split(" ")[0],
					String.CASE_INSENSITIVE_ORDER);
			if (position >= 0) {
				return line;
			} else {
				final Set<String> suggestions = new HashSet<>(stringsWithPrefix(line, acceptedInput));
				Collections.sort(acceptedInput, new Comparator<String>() {

					@Override
					public int compare(final String s1, final String s2) {
						final int d1 = OesingLibString.editDistance(s1, line, 1, 1, 1);
						final int d2 = OesingLibString.editDistance(s2, line, 1, 1, 1);
						return Integer.compare(d1, d2);
					}
				});
				for (int i = 0; i < suggestionsByDistance; i++) {
					final String s = acceptedInput.get(i);
					if (!suggestions.contains(s)) {
						suggestions.add(s);
					}
				}
				Collections.sort(acceptedInput, new Comparator<String>() {
					@Override
					public int compare(final String s1, final String s2) {
						final int d1 = longestCommonSubstring(s1, line);
						final int d2 = longestCommonSubstring(s2, line);
						return -Integer.compare(d1, d2);
					}
				});
				for (int i = 0; i < suggestionsByDistance; i++) {
					final String s = acceptedInput.get(i);
					if (!suggestions.contains(s)) {
						suggestions.add(s);
					}
				}
				return getInput(scan,
						"Did you meant any of those? Try again or give the position of the word you meant.\n"
								+ suggestions.toString(),
								new ArrayList<>(suggestions), 0, 0, true, line, 0);
			}
		} else {
			line = scan.nextLine();
		}
		return line;
	}

	/**
	 * @param path Path to a given file.
	 * @return The date when the given file was last altered.
	 */
	public String getModifiedDate(final Path path) throws IOException {
		final File f = path.toFile();
		final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		return sdf.format(f.lastModified());
	}

	/**
	 * @param s The string to get all permutations from.
	 * @return Returns all permutations of the given string.
	 */
	public ArrayList<String> getPermutations(final String s) {
		return getPermutations("", s);
	}

	private ArrayList<String> getPermutations(final String prefix, final String s) {
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
	 * Initializes a push fold chart.
	 *
	 * <pre>
	 * Jennifears Push/Fold Charts
	 * https://docs.google.com/spreadsheets/d/
	 * 1hHM04qRKysOVj0IoiW6EZpWFSVB2U3Oscg4-B2_0xog
	 * </pre>
	 *
	 * @throws IOException
	 */
	private void getPushFoldChart() throws IOException {
		final File f = new File("D:/BotPics/jennifearPushFold.ser");
		if (f.exists()) {
			final String input = getInput(null, "Push/Fold Map already exists, do you want to override? [yes/no]",
					Arrays.asList(new String[] { "yes", "no" }), 0, 0, false, null, 0);
			if (input.equals("yes")) {
				f.delete();
			} else {
				return;
			}
		}
		System.out.println("Trying to parse Jennifears Push/Fold chart.");
		// BB => Position => Cards
		pushFold = new HashMap<>();
		Document doc = null;
		try {
			doc = Jsoup.connect(
					"https://docs.google.com/spreadsheets/d/1hHM04qRKysOVj0IoiW6EZpWFSVB2U3Oscg4-B2_0xog/htmlview?hl=en_US&sle=true#")
					.get();
		} catch (final IOException e1) {
			e1.printStackTrace();
			System.exit(-1);
		}
		final Elements[] ante = new Elements[4];
		ante[0] = doc.select("#0"); // 0
		ante[1] = doc.select("#2"); // 10
		ante[2] = doc.select("#3"); // 12.5
		ante[3] = doc.select("#6"); // 20
		for (int a = 0; a < ante.length; a++) { // ante
			final HashMap<Integer, HashMap<Integer, String>> mapAnte = new HashMap<>();
			pushFold.put(a, mapAnte);
			final Elements el = ante[a].select(" div > table > tbody > tr");
			int ignored = 0;
			for (int i = 3; i <= 25; i++) { // bb
				if (i == 8 || i == 14 || i == 20) {
					ignored++;
					continue;
				}
				final HashMap<Integer, String> mapBB = new HashMap<>();
				mapAnte.put(i - 2 - ignored, mapBB);
				final Element e = el.get(i);
				final Elements positions = e.select("td");
				for (int j = 1; j <= 8; j++) { // position
					mapBB.put(j, positions.get(j).text());
				}
			}
		}
		try {
			final ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(new File("D:/BotPics/jennifearPushFold.ser")));
			out.writeObject(pushFold);
			out.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public String getWhatsApp(final long phoneNumber) throws IOException {
		// TODO Login on web.whatsapp
		final Document doc = htmlGetViaUrlAsSoup("https://web.whatsapp.com");
		System.out.println(doc.toString());
		return doc.toString();
	}

	/**
	 * @param first  Number to compare.
	 * @param second Number to compare.
	 * @return Returns the greatest comman divisor.
	 * @see #ggT(long, long)
	 */
	public long ggT(final BigInteger first, final BigInteger second) {
		if (first.compareTo(BigInteger.ZERO) < 1 || second.compareTo(BigInteger.ZERO) < 1) {
			return -1;
		}
		if (first.subtract(BigInteger.valueOf(Long.MAX_VALUE)).signum() == -1) {
			if (debug) {
				System.out.println("Small enough for long.");
			}
			return ggT(first.longValue(), second.longValue());
		}
		BigInteger zw1 = BigInteger.ZERO;
		zw1 = zw1.add(first);
		BigInteger zw2 = BigInteger.ZERO;
		zw2 = zw2.add(second);
		BigInteger remain = BigInteger.ZERO;
		while (zw1.mod(zw2) != BigInteger.ZERO) {
			while (zw1.max(zw2) == zw1) {
				zw1 = zw1.subtract(zw2);
			}
			remain = remain.add(zw1);
			zw1 = zw2;
			zw2 = remain;
			if (debug) {
				System.out.println(zw2);
			}
			remain = BigInteger.ZERO;
		}
		return zw2.longValue();
	}

	/**
	 * @param first  Number to compare.
	 * @param second Number to compare.
	 * @return Returns the greatest comman divisor.
	 * @see #ggT(BigInteger, BigInteger)
	 */
	public long ggT(long first, long second) {
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

	public void guiGenerate(final String function) {
		// TODO:
		final Method[] methods = this.getClass().getMethods();
		Method func = null;
		for (final Method m : methods) {
			if (m.getName().toLowerCase().equals(function.toLowerCase())) {
				func = m;
				break;
			}
		}
		final JFrame frame = new JFrame();
		frame.setSize(200, 200);
		frame.setBackground(Color.CYAN);
		final JPanel panel = new JPanel();
		frame.add(panel);
		final JTextField text = new JTextField("TEST KAPPA 123");
		text.setSize(100, 100);
		panel.add(text);
		frame.setVisible(true);

		sleep(100);
		System.exit(0);
	}

	private int highCard(String hand) {
		int result = 0;
		hand = hand.toLowerCase().replaceAll("[dhsc]", "");
		final List<String> list = new ArrayList<>(Arrays.asList(hand.split("")));
		Collections.sort(list, getComparatorForGivenOrder(
				Arrays.asList(new String[] { "a", "k", "q", "j", "t", "9", "8", "7", "6", "5", "4", "3", "2" })));
		for (int i = 0; i < 5; i++) {
			if (i < list.size()) {
				final String s = list.get(i);
				if (s.equals("a")) {
					result += 14;
				} else if (s.equals("k")) {
					result += 13;
				} else if (s.equals("q")) {
					result += 12;
				} else if (s.equals("j")) {
					result += 11;
				} else if (s.equals("t")) {
					result += 10;
				} else {
					result += Integer.parseInt(s);
				}
			}
			if (i < 4) {
				result *= 100;
			}
		}
		return result;
	}

	/**
	 * Builds and executes a HTML Request.
	 *
	 * @param targetURL     The URL the HTML Request gets send to.
	 * @param requestMethod GET/POST
	 * @param requestBody   The body of a post request.
	 * @param urlParameters Parameters for the HTML Request. Formated like this
	 *                      (Key1:Value1) (Key2:Value2)..
	 * @return Returns the response of the target.
	 */
	public String htmlBuildRequest(final String targetURL, final String requestMethod, final String requestBody,
			final String... urlParameters) {
		HttpURLConnection connection = null;
		try {
			final URL url = new URL(targetURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(requestMethod);
			connection.setRequestProperty("Content-Type", "*/*");
			connection.setRequestProperty("Content-Length", Integer.toString(requestBody.getBytes().length));
			for (final String s : urlParameters) {
				final String[] parts = s.split(":");
				connection.addRequestProperty(parts[0], parts[1]);
			}
			connection.setUseCaches(false);
			if (requestMethod.equalsIgnoreCase("POST")) {
				connection.setDoOutput(true);
				final DataOutputStream out = new DataOutputStream(connection.getOutputStream());
				out.writeBytes(requestBody);
				out.close();
			}
			if (connection.getResponseCode() != 200) {
				System.out.println("Connection failed : " + connection.getResponseCode());
				return "";
			} else {
				final InputStream is = connection.getInputStream();
				final BufferedReader rd = new BufferedReader(new InputStreamReader(is));
				final StringBuilder response = new StringBuilder();
				String line;
				while ((line = rd.readLine()) != null) {
					response.append(line);
					response.append('\r');
				}
				rd.close();
				return response.toString();
			}
		} catch (final Exception e) {
			e.printStackTrace();
			return "";
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	/**
	 * @param url       The input url we want to get the html Code from.
	 * @param selectors A list of HTML-selectors that the document get parsed with.
	 * @return A JSoup-list containing the html code, each element represents one
	 *         HTML-Element.
	 */
	public Elements htmlGetViaUrlAndSelectors(final String url, final List<String> selectors) {
		final String document = htmlGetViaUrlAsString(url, false);
		final Elements elements = new Elements();
		for (final String selector : selectors) {
			elements.addAll(Jsoup.parse(document).select(selector));
		}
		return elements;
	}

	/**
	 * @param url The input url we want to get the html Code from.
	 * @return A list containing the html code, each element represents one line.
	 * @param waitForJS If true the function tries everything to wait for all
	 *                  Javascript.
	 * @see #htmlGetViaUrlAsString(String, boolean)
	 */
	@SuppressWarnings("serial")
	public List<String> htmlGetViaUrlAsList(String url, final boolean waitForJS) {
		if (!url.contains("http")) {
			url = "http://" + url;
		}
		final List<String> list = new ArrayList<>();
		LogManager.getLogManager().reset();
		try (final WebClient webClient = new WebClient(BrowserVersion.BEST_SUPPORTED)) {
			webClient.getCookieManager().setCookiesEnabled(true);
			webClient.getOptions().setThrowExceptionOnScriptError(false);
			webClient.getOptions().setCssEnabled(false);
			webClient.getOptions().setUseInsecureSSL(true);
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
			webClient.waitForBackgroundJavaScript(5000);
			webClient.setAjaxController(new AjaxController() {
				@Override
				public boolean processSynchron(final HtmlPage page, final WebRequest request, final boolean async) {
					return true;
				}
			});
			final HtmlPage page = webClient.getPage(url);
			webClient.getCurrentWindow().setInnerHeight(Integer.MAX_VALUE);
			if (waitForJS) {
				String old = null;
				while (old == null || !old.equals(page.asXml())) {
					old = page.asXml();
					synchronized (page) {
						page.executeJavaScript("window.scrollTo(0,document.body.scrollHeight);");
						page.wait(10000);
					}
				}
			}
			final BufferedReader reader = new BufferedReader(new StringReader(page.asXml()));
			String line;
			while ((line = reader.readLine()) != null) {
				list.add(line);
			}
			webClient.close();
		} catch (InterruptedException | FailingHttpStatusCodeException | IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * @param url The input url we want to get the html Code from.
	 *
	 * @return A JSOUP-HTML Document.
	 */
	public Document htmlGetViaUrlAsSoup(final String url) throws IOException {
		return Jsoup.connect(url).userAgent(
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36")
				.referrer("http://www.google.com").get();
	}

	/**
	 * @param url       The input url we want to get the html Code from.
	 * @param waitForJS If true the function tries everything to wait for all
	 *                  Javascript.
	 * @return A string containing the html code. #htmlGetCodeFromUrlList(String)
	 */
	public String htmlGetViaUrlAsString(final String url, final boolean waitForJS) {
		return String.join(", ", htmlGetViaUrlAsList(url, waitForJS));
	}

	/**
	 * This method prints every Series which got a new episode.
	 */
	public void htmlNewEpisodes() throws IOException {
		final Path path = Paths.get("").toAbsolutePath().resolve("series.txt");
		if (!Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
			Files.write(path, "".getBytes(), StandardOpenOption.CREATE);
			openFile(path, "Enter lines this way \n<link>,<#season>,<#episode>,<selector1;selector2;selector3;...>");
		} else if (getInput(null, "Do you want to edit your choice of series? [y/!y]", null, 0, 0, false, null, 0)
				.equalsIgnoreCase("y")) {
			openFile(path, "Enter lines this way \n<link>,<#season>,<#episode>,<selector1;selector2;selector3;...>");
			return;
		}
		for (final String series : Files.readAllLines(path)) {
			final String[] parts = series.split(",");
			final ArrayList<String> list = new ArrayList<>();
			list.add(parts[3]);
			final Elements elements = htmlGetViaUrlAndSelectors(parts[0], list);
			final Element el = elements.get(0);
			for (final Element e : elements) {
				if (!e.equals(el)) {
					System.out.println("The Selectors returned different results.");
					return;
				}
			}
			final String[] episodes = el.attr("rel").split(",");
			final String s = el.val() + "," + episodes[episodes.length - 1];
			if (s.equals(parts[1] + "," + parts[2])) {
				System.out.println("The series: " + parts[0] + " has no new episodes");
			} else {
				System.out.println("The series: " + parts[0] + " has some new episodes, you watched till: " + parts[1]
						+ "," + parts[2] + " the newest is " + s);
			}
		}
	}

	/**
	 * @param urls The URL of the streams that get recorded in parallel.
	 */
	public void htmlRecordStream(final String[] urls) throws IOException, InterruptedException, ExecutionException {
		List<String> streams;
		if (urls.length == 0) {
			final Path path = Paths.get("").toAbsolutePath().resolve("streams.txt");
			if (!Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
				Files.write(path, "".getBytes(), StandardOpenOption.CREATE);
				openFile(path, "Enter lines this way <www.abc.de/fg>");
			} else if (getInput(null, "Do you want to edit your choice of streams? [y/!y]", null, 0, 0, false, null, 0)
					.equalsIgnoreCase("y")) {
				openFile(path, "Enter lines this way \n<link>");
				return;
			}
			streams = Files.readAllLines(path);
		} else {
			streams = Arrays.asList(urls);
		}
		final List<Callable<Object>> threads = new ArrayList<>();
		for (final String stream : streams) {
			threads.add(new Callable<Object>() {
				@Override
				public Object call() throws Exception {
					final String name = Paths.get(stream).getFileName().toString();
					if (stream.contains("twitch")) {
						executeCmd(true, new String[] {
								"cd \"C:/Program Files (x86)/livestreamer-v1.12.2\" && livestreamer --twitch-oauth-token "
										+ twitchLoadAuth().get(0) + " --retry-streams 30 --retry-open 20 " + stream
										+ " best -o \"C:/Users/Naix/Desktop/" + name + ".mp4\"" });
					} else {
						executeCmd(true, new String[] {
								"cd \"C:/Program Files (x86)/livestreamer-v1.12.2\" && livestreamer --retry-streams 30 --retry-open 20 "
										+ stream + " best -o \"C:/Users/Naix/Desktop/" + name + ".mp4\"" });
					}
					return null;
				}
			});
		}
		multiThread(threads, Runtime.getRuntime().availableProcessors());
	}

	/**
	 * Displays the url or htmlCode in that priority in a new swing element.
	 *
	 * @param url      Url with or without protocol. Can be null.
	 * @param htmlCode HtmlCode to be displayed.
	 * @param file     File containing html code.
	 * @return A JFrame that can be handled elsewhere.
	 */
	public JFrame htmlToSwing(final String url, final String htmlCode, final File file) {
		final JFrame jFrame = new JFrame();
		jFrame.setSize(800, 800);
		jFrame.setVisible(true);
		final JFXPanel jfxPanel = new JFXPanel();
		jFrame.add(jfxPanel);
		Platform.runLater(() -> {
			final WebView webView = new WebView();
			webView.getEngine().getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
				final JSObject window = (JSObject) webView.getEngine().executeScript("window");
				final JavaBridge bridge = new JavaBridge();
				window.setMember("java", bridge);
				webView.getEngine()
				.executeScript("console.log = function(message)\n" + "{\n" + "    java.log(message);\n" + "};");
			});
			jfxPanel.setScene(new Scene(webView));
			if (url != null && url != "") {
				if (!url.contains("http")) {
					webView.getEngine().load("http://" + url);
				} else {
					webView.getEngine().load(url);
				}
				System.out.println("Loaded " + url);
			} else {
				if (htmlCode != null && htmlCode != "") {
					webView.getEngine().loadContent(htmlCode);
					System.out.println("Loaded HTML");
				} else {
					if (file != null && file.isFile()) {
						try {
							webView.getEngine().load(file.toURI().toURL().toString());
						} catch (final Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
		return jFrame;
	}

	/**
	 * Downloads a file to the desktop.
	 *
	 * @param url     The input url we want to get the file from.
	 * @param newName Alternative name for the file, if null or empty the default
	 *                name is used.
	 */
	public void htmlWget(String url, final String newName) throws MalformedURLException, IOException {
		final String fileName = Paths.get(url.replaceAll(":", "")).getFileName().toString();
		if (!url.contains("http")) {
			url = "http://" + url;
		}
		final InputStream in = new URL(url).openStream();
		if (newName != null && !newName.equals("")) {
			Files.copy(in, Paths.get("C:/Users/Naix/Desktop/" + newName));
		} else {
			Files.copy(in, Paths.get("C:/Users/Naix/Desktop/" + fileName));
		}
		in.close();
	}

	/**
	 * @param image       Original image
	 * @param extraWidth  Amount of extra width
	 * @param extraHeight Amount of extra height
	 * @return Returns an image with added white boarder.
	 */
	public BufferedImage imageAddSpace(final BufferedImage image, final int extraWidth, final int extraHeight) {
		final Color c = Color.WHITE;
		return imageAddSpace(image, extraWidth, extraHeight, c);
	}

	/**
	 * @param image       Original image
	 * @param extraWidth  Amount of extra width
	 * @param extraHeight Amount of extra height
	 * @param c           Color to add.
	 * @return Returns an image with added boarder.
	 */
	public BufferedImage imageAddSpace(final BufferedImage image, final int extraWidth, final int extraHeight,
			final Color c) {
		final BufferedImage bigImage = new BufferedImage(image.getWidth() + extraWidth, image.getHeight() + extraHeight,
				BufferedImage.TYPE_INT_RGB);
		final Graphics2D g2 = bigImage.createGraphics();
		g2.setColor(c);
		g2.fillRect(0, 0, bigImage.getWidth(), bigImage.getHeight());
		g2.drawImage(image, extraWidth / 2, extraHeight / 2, null);
		g2.dispose();
		return bigImage;
	}

	/**
	 * @param c      Character that gets drawn.
	 * @param width  Width of the image.
	 * @param height Height of the image.
	 * @param font   Front that gets printed.
	 * @return Returns an image containing the given character in the given font.
	 */
	private BufferedImage imageChar(final Character c, final int width, final int height, final Font font)
			throws IOException {
		final BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
		final Graphics2D g2 = img.createGraphics();
		final FontMetrics fm = g2.getFontMetrics();
		fm.getDescent();
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, img.getWidth(), img.getHeight());
		g2.setPaint(Color.BLACK);
		g2.setFont(font);
		g2.drawString("" + c, 0, height - fm.getDescent());
		g2.dispose();
		return img;
	}

	/**
	 * @param pixels A Set of pixels to be drawn.
	 * @param img    The image that gets drawn on.
	 * @return Black and white image with black pixels and white background, or
	 *         draws black pixels onto the given image.
	 */
	public BufferedImage imageDrawFromPixels(final HashSet<Point> pixels, BufferedImage img) {
		int width = 0;
		int height = 0;
		if (img != null) {
			width = img.getWidth();
			height = img.getHeight();
		} else {
			final Iterator<Point> it = pixels.iterator();
			while (it.hasNext()) {
				final Point p = it.next();
				width = Math.max(width, p.x);
				height = Math.max(height, p.y);
			}
			img = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
		}
		final Graphics2D g2 = img.createGraphics();
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, width, height);
		g2.dispose();
		for (final Point pix : pixels) {
			img.setRGB(pix.x, pix.y, Color.BLACK.getRGB());
		}
		return img;
	}

	/**
	 * Uses the mouse to draw a given picture. Paint or similiar should be used.
	 *
	 * @param pathOrUrl A weblinklink or a filepath of a picture.
	 */
	public void imageDrawViaMouse(final String pathOrUrl) throws AWTException, MalformedURLException, IOException {
		BufferedImage img;
		if (pathOrUrl.contains(".com") || pathOrUrl.contains(".org") || pathOrUrl.contains(".de")
				|| pathOrUrl.contains(".net") || pathOrUrl.contains(".am")) {
			img = ImageIO.read(new URL(pathOrUrl));
		} else {
			img = ImageIO.read(new File(pathOrUrl));
		}
		img = imageToBlackAndWhite(img);
		// Get place to draw.
		final Robot robot = new Robot();
		getInput(null, "Place your mouse in the top left and type anything", null, 0, 0, false, null, 0);
		final Point tl = MouseInfo.getPointerInfo().getLocation();
		getInput(null, "Place your mouse in the bottom right and type anything", null, 0, 0, false, null, 0);
		final Point br = MouseInfo.getPointerInfo().getLocation();
		// Resize Image
		img = imageResize(img, br.x - tl.x, br.y - tl.y);
		// Get black pixels
		int width, height;
		width = img.getWidth();
		height = img.getHeight();
		final boolean[][] fill = new boolean[width + 2][height + 2];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				final int x = img.getRGB(i, j);
				fill[i][j] = x <= -2;
			}
		}
		// draw frame
		robot.mouseMove(tl.x, tl.y);
		robot.mousePress(InputEvent.BUTTON1_MASK);
		robot.mouseMove(br.x, tl.y);
		sleep(0.025);
		robot.mouseMove(br.x, br.y);
		sleep(0.025);
		robot.mouseMove(tl.x, br.y);
		sleep(0.025);
		robot.mouseMove(tl.x, tl.y);
		sleep(0.025);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);

		// Draw Image
		boolean clicked = false;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (fill[i][j]) {
					if (!clicked) {
						robot.mouseMove(tl.x + i, tl.y + j);
						robot.mousePress(InputEvent.BUTTON1_MASK);
						clicked = true;
					}
				} else if (clicked) {
					robot.mouseMove(tl.x + i - 1, tl.y + j - 1);
					robot.mouseRelease(InputEvent.BUTTON1_MASK);
					clicked = false;
					sleep(0.005);
				}
			}
			robot.mouseMove(tl.x + i, tl.y + (height - 1));
			robot.mouseRelease(InputEvent.BUTTON1_MASK);
			clicked = false;
		}
	}

	/**
	 * @param bigImg   The bigger image that gets searched.
	 * @param smallImg The smaller subimage.
	 * @return A rectangle indicating where the smaller image got found.
	 */
	@SuppressWarnings("unused")
	private Rectangle imageFindSubimage(final BufferedImage bigImg, final BufferedImage smallImg) {
		int sameColor = 1;
		final int firstColor = smallImg.getRGB(0, 0);
		for (int i = 1; i < smallImg.getWidth(); i++) {
			if (smallImg.getRGB(i, 0) == firstColor) {
				sameColor++;
			} else {
				break;
			}
		}
		for (int h = 0; h < bigImg.getHeight(); h++) {
			for (int w = 0; w < bigImg.getWidth(); w += sameColor) {
				if (bigImg.getRGB(w, h) == firstColor) {
					int w2 = w;
					while (bigImg.getRGB(w2, h) == firstColor) {
						w2--;
					}
					for (int i = 0; i < smallImg.getWidth(); i++) {
						for (int j = 0; j < smallImg.getHeight(); j++) {
							if (smallImg.getRGB(i, j) != bigImg.getRGB(w2 + i, h + j)) {
								i = smallImg.getWidth();
								break;
							}
						}
					}
					return new Rectangle(w2, h, smallImg.getWidth(), smallImg.getHeight());
				}
			}
		}
		return null;
	}

	/**
	 * Help function to identify all cards.
	 *
	 * @param directory Path to the directory that the identifiyed images gets safed
	 *                  to.
	 * @throws InterruptedException
	 */
	private void imageIdentify(final String directory, final BufferedImage img)
			throws IOException, InterruptedException {
		final BufferedImage blackImg = imageToBlackAndWhite(img);
		int i = 0;
		if (1 == 0) {
			imageShow("Identify please.", 15, imageResize(blackImg, 100, 100));
			final String input = getInput();
			File fNew = new File(Paths.get(directory).toAbsolutePath() + "\\" + input + "_" + i + ".png");
			while (fNew.exists()) {
				i++;
				fNew = new File(Paths.get(directory).toAbsolutePath() + "\\" + input + "_" + i + ".png");
			}
			imageSave(fNew.getAbsolutePath().replace(".png", ""), blackImg, "png");
			System.out.println("Saved file as: " + fNew.getName());
		} else {
			final File[] files = getFilesInDirectory("D:/BotPics/Cards");
			double bestPercent = 0;
			File best = null;
			double x = 0.0;
			for (final File f : files) {
				x = imageMatch(blackImg, ImageIO.read(f), null);
				if (x > bestPercent) {
					bestPercent = x;
					best = f;
				}
			}
			final String outPath = best.getAbsolutePath().replaceAll("_\\d*.png", "");
			best = new File(outPath + "_" + i + ".png");
			while (best.exists()) {
				i++;
				best = new File(outPath + "_" + i + ".png");
			}
			imageSave(best.getAbsolutePath().replace(".png", ""), blackImg, "png");
			System.out.println("Saved file as: " + best.getName());
		}
		initHashToCard(); // reload
	}

	/**
	 * @param img1 First image
	 * @param img2 Second image
	 * @return Returns both images in one bigger image.
	 */
	public BufferedImage imageJoin(final BufferedImage img1, final BufferedImage img2) {
		// do some calculate first
		final int offset = 5;
		final int wid = img1.getWidth() + img2.getWidth() + offset;
		final int height = Math.max(img1.getHeight(), img2.getHeight()) + offset;
		// create a new buffer and draw two image into the new image
		final BufferedImage newImage = new BufferedImage(wid, height, BufferedImage.TYPE_INT_ARGB);
		final Graphics2D g2 = newImage.createGraphics();
		final Color oldColor = g2.getColor();
		// fill background
		g2.setPaint(Color.WHITE);
		g2.fillRect(0, 0, wid, height);
		// draw image
		g2.setColor(oldColor);
		g2.drawImage(img1, null, 0, 0);
		g2.drawImage(img2, null, img1.getWidth() + offset, 0);
		g2.dispose();
		return newImage;
	}

	/**
	 * @param image1 First image that gets compared
	 * @param image2 Second image that gets compared
	 * @param ignore A list of colors, that get ignored in the comparison.
	 * @return Returns the percent of same pixels considering the smaller
	 *         dimensions.
	 */
	public double imageMatch(final BufferedImage image1, final BufferedImage image2, final List<Color> ignore) {
		int counter = 0;
		final int smallerHeight = Math.min(image1.getHeight(), image2.getHeight());
		final int smallerWidth = Math.min(image1.getWidth(), image2.getWidth());
		for (int i = 0; i < smallerWidth; i++) {
			for (int j = 0; j < smallerHeight; j++) {
				final Color c = new Color(image1.getRGB(i, j));
				if ((ignore == null || !ignore.contains(c)) && image1.getRGB(i, j) == image2.getRGB(i, j)) {
					counter++;
				}
			}
		}
		final double denominator = smallerHeight * smallerWidth;
		return counter / denominator;
	}

	/**
	 * @param image Original image
	 * @return Calculates the color that is most presented in the image.
	 */
	public Color imageMostDominantColor(final BufferedImage image) {
		int most = 0;
		Color dominantColor = null;
		final HashMap<Color, Integer> count = new HashMap<>();
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				final Color c = new Color(image.getRGB(i, j));
				if (count.containsKey(c)) {
					final int x = count.get(c);
					if (x > most) {
						dominantColor = c;
						most = x;
					}
					count.remove(c);
					count.put(c, x + 1);
				} else {
					count.put(c, 1);
				}
			}
		}
		return dominantColor;
	}

	/**
	 * @param img       Original image.
	 * @param newWidth  The width of the rescaled image.
	 * @param newHeight The height of the rescaled image.
	 * @return Rescaled version of the original image.
	 */
	public BufferedImage imageResize(final BufferedImage img, final int newWidth, final int newHeight) {
		final BufferedImage scaled = new BufferedImage(newWidth, newHeight, img.getType());
		final Graphics2D g = scaled.createGraphics();
		g.setComposite(AlphaComposite.Src);
		g.drawImage(img, 0, 0, newWidth, newHeight, null);
		g.dispose();
		return scaled;
	}

	/**
	 * @param path  A path presented as a string where the image gets saved.
	 * @param image The image that gets saved.
	 * @param type  Which type is the image (jpg/png).
	 */
	private void imageSave(final String path, final BufferedImage image, String type) throws IOException {
		type = type.replaceAll("\\.", "");
		final File file = new File(path + "." + type);
		if (!file.exists()) {
			ImageIO.write(image, type, file);
		} else {
			if (getInput(null, "Image exists, override? [y/n]", Arrays.asList(new String[] { "y", "n" }), 0, 0, false,
					null, 0).equals("y")) {
				ImageIO.write(image, type, file);
			}
		}
	}

	/**
	 * Opens a simple window that showes multiple pictures,
	 *
	 * @param s         A string that gets printed on the frame.
	 * @param closeTime Time in seconds till the window autocloses.
	 * @param images    Array of Images, possible to give them one by one.
	 *                  (image1,image2...)
	 */
	public void imageShow(final String s, final int closeTime, final BufferedImage... images) {
		final JFrame frame = new JFrame();
		frame.setSize(800, 200);
		frame.setBackground(Color.CYAN);
		final JPanel panel = new JPanel();
		frame.add(panel);
		for (int i = 0; i < images.length; i++) {
			images[i] = imageAddSpace(images[i], images[i].getWidth(), images[i].getHeight(), Color.ORANGE);
			final ImageIcon icon = new ImageIcon(images[i]);
			final JLabel label = new JLabel();
			label.setIcon(icon);
			panel.add(label);
		}
		final JLabel textLabel = new JLabel();
		textLabel.setText(s);
		panel.add(textLabel);
		frame.setVisible(true);
		if (closeTime > 0) {
			final Timer timer = new Timer(closeTime * 1000, new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					frame.setVisible(false);
					frame.dispose();
				}
			});
			timer.setRepeats(false);
			timer.start();
		}
	}

	/**
	 * @param encode   A Boolean value if the user wants to encode or decode.
	 * @param imgPath  Path to the image.
	 * @param hidePath Path to the binary to hide.
	 */
	public void imageSteganography(final boolean encode, final String imgPath, final String hidePath)
			throws IOException {
		final File f = new File(imgPath);
		BufferedImage img;
		img = ImageIO.read(f);
		if (encode) {
			byte[] textBytes;
			textBytes = Files.readAllBytes(Paths.get(hidePath));
			String bits = "";
			for (final byte b : textBytes) {
				String s = Integer.toBinaryString(b);
				final int leadingZeros = 8 - s.length();
				switch (leadingZeros) {
				case 1:
					s = "0" + s;
					break;
				case 2:
					s = "00" + s;
					break;
				case 3:
					s = "000" + s;
					break;
				case 4:
					s = "0000" + s;
					break;
				case 5:
					s = "00000" + s;
					break;
				case 6:
					s = "000000" + s;
					break;
				case 7:
					s = "0000000" + s;
					break;
				default:
					break;
				}
				bits += s;
			}
			for (int i = 0; i < img.getWidth() && bits.length() > 0; i++) {
				for (int j = 0; j < img.getHeight() && bits.length() > 0; j++) {
					int x = img.getRGB(i, j);
					int red = (x >> 16) & 0x000000FF;
					int green = (x >> 8) & 0x000000FF;
					int blue = (x) & 0x000000FF;

					if (bits.length() >= 2) {
						red = (red & 252) | Integer.parseInt(bits.substring(0, 2), 2);
						bits = bits.substring(2);
					}
					if (bits.length() >= 2) {
						green = (green & 252) | Integer.parseInt(bits.substring(0, 2), 2);
						bits = bits.substring(2);
					}
					if (bits.length() >= 2) {
						blue = (blue & 252) | Integer.parseInt(bits.substring(0, 2), 2);
						bits = bits.substring(2);
					}

					x = (red << 16 | green << 8 | blue);
					img.setRGB(i, j, x);
				}
			}
			String outPath = imgPath;
			outPath = outPath.replace("." + getFileEndingOfPath(outPath), "Stego");
			imageSave(outPath, img, getFileEndingOfPath(outPath));
		} else {
			final StringBuilder sb = new StringBuilder();
			for (int i = 0; i < img.getWidth(); i++) {
				for (int j = 0; j < img.getHeight(); j++) {
					final int x = img.getRGB(i, j);
					int red = (x >> 16) & 0x000000FF;
					int green = (x >> 8) & 0x000000FF;
					int blue = (x) & 0x000000FF;

					String s1, s2, s3;
					red &= 3;
					s1 = Integer.toBinaryString(red);
					if (s1.length() == 1) {
						s1 = "0" + s1;
					}
					green &= 3;
					s2 = Integer.toBinaryString(green);
					if (s2.length() == 1) {
						s2 = "0" + s2;
					}
					blue &= 3;
					s3 = Integer.toBinaryString(blue);
					if (s3.length() == 1) {
						s3 = "0" + s3;
					}

					sb.append(s1 + s2 + s3);
				}
			}
			final String message = sb.toString();
			for (int i = 0; i < message.length() / 8; i++) {
				final int a = Integer.parseInt(message.substring(8 * i, (i + 1) * 8), 2);
				final char c = (char) a;
				if (!Character.toString(c).matches("[a-zA-Z,. \n\r-]")) {
					break;
				}
			}
		}
	}

	/**
	 * @param image Original image that gets cut
	 * @param rect  The rectangle that gets cut from the image.
	 * @return Returns a subimage given by the input.
	 *
	 * @see {@link java.awt.image.BufferedImage#getSubimage(int, int, int, int)}
	 */
	public BufferedImage imageSubImage(final BufferedImage image, final Rectangle rect) {
		final BufferedImage cut = image.getSubimage(rect.x, rect.y, rect.width, rect.height);
		return cut;
	}

	/**
	 * @param image    Image that gets turned into Ascii-Art.
	 * @param alphabet Symbols that get used.
	 * @param size     Size of each individual block of pixels. 4-5 is default.
	 * @param fontName Font of the Symbols.
	 * @return Returns a string of Ascii-Art.
	 */
	public String imageToAscii(final BufferedImage image, final String alphabet, final int size, final String fontName)
			throws IOException {
		final Font font = new Font(fontName, Font.BOLD, size);
		final BufferedImage blackImg = imageToBlackAndWhite(image);
		final HashMap<Character, BufferedImage> charImgs = new HashMap<>();
		final OesingCountMap<Character> charUsed = new OesingCountMap<>();
		final Point measure = measureFont(font, alphabet);
		for (final Character c : alphabet.toCharArray()) {
			charImgs.put(c, imageChar(c, measure.x, measure.y, font));
		}
		final StringBuilder sb = new StringBuilder();
		for (int j = 0; (j + 1) * measure.y < blackImg.getHeight(); j++) {
			for (int i = 0; (i + 1) * measure.x < blackImg.getWidth(); i++) {
				final BufferedImage subImg = imageSubImage(blackImg,
						new Rectangle(i * measure.x, j * measure.y, measure.x, measure.y));
				double bestMatch = 0;
				char bestMatchChar = ' ';
				for (final Character c : alphabet.toCharArray()) {
					final double match = imageMatch(charImgs.get(c), subImg,
							Arrays.asList(new Color[] { Color.WHITE }));
					if (bestMatch < match) {
						bestMatch = match;
						bestMatchChar = c;
					}
				}
				charUsed.countUp(bestMatchChar);
				sb.append(bestMatchChar);
			}
			sb.append('\n');
		}
		final List<Entry<Character, Integer>> sortedList = charUsed.getSortedListByValue();
		System.out.println(sortedList);
		return sb.toString();
	}

	/**
	 * @param image Image in Byte style.
	 * @return A black and white Image.
	 */
	public BufferedImage imageToBlackAndWhite(final BufferedImage image) {
		final BufferedImage blackWhite = new BufferedImage(image.getWidth(), image.getHeight(),
				BufferedImage.TYPE_BYTE_BINARY);
		final Graphics2D g = blackWhite.createGraphics();
		g.drawImage(image, 0, 0, null);
		return blackWhite;
	}

	/**
	 * @param image Image that gets converted to grayscale.
	 * @return Returns image in grayscale.
	 */
	public BufferedImage imageToGrayscale(final BufferedImage image) {
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				final Color c = new Color(image.getRGB(i, j));
				final int avg = (c.getRed() + c.getGreen() + c.getBlue()) / 3;
				final int gray = (c.getAlpha() << 24) | (avg << 16) | (avg << 8) | avg;
				image.setRGB(i, j, gray);
			}
		}
		return image;
	}

	private String imageToHash(final BufferedImage image) throws InterruptedException, IOException {
		final int[] arr = imageToBlackAndWhite(image).getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0,
				image.getWidth());
		String s = "";
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] == -1) {
				s += 0;
			} else if (arr[i] == -16777216) {
				s += 1;
			}
		}
		return "" + binaryTo32Bit(s);
	}

	private void initHashToCard() throws InterruptedException, IOException {
		hashToCard = new HashMap<>();
		final File[] cards = getFilesInDirectory("D:/BotPics/Cards");
		for (final File f : cards) {
			hashToCard.put(imageToHash(ImageIO.read(f)), f.getName().replaceAll("_\\d*\\.png", ""));
		}
	}

	public boolean isAnagram(final String s1, final String s2) {
		final OesingCountMap<Character> charCount = new OesingCountMap<>();
		if (s1.length() != s2.length()) {
			return false;
		}
		for (int i = 0; i < s1.length(); i++) {
			charCount.countUp(s1.charAt(i));
		}
		for (int i = 0; i < s2.length(); i++) {
			charCount.countDown(s2.charAt(i));
		}
		return charCount.size() == 0;
	}

	private int isFlush(String hand) {
		hand = hand.toLowerCase();
		final OesingCountMap<Character> countSuits = new OesingCountMap<>();
		final HashMap<Character, Integer> biggestSuit = new HashMap<>();
		while (hand.length() != 0) {
			final char value = hand.charAt(0);
			int numValue;
			if (value == 't') {
				numValue = 10;
			} else if (value == 'j') {
				numValue = 11;
			} else if (value == 'q') {
				numValue = 12;
			} else if (value == 'k') {
				numValue = 13;
			} else if (value == 'a') {
				numValue = 14;
			} else {
				numValue = Character.digit(value, 10);
			}
			final char suit = hand.charAt(1);
			biggestSuit.put(suit, Math.max(biggestSuit.getOrDefault(suit, 0), numValue));
			if (countSuits.countUp(suit) == 5) {
				return biggestSuit.get(suit);
			}
			hand = hand.substring(2);
		}
		return 0;
	}

	private int isFullHouse(String hand) {
		hand = hand.toLowerCase().replaceAll("[hcsd]", "");
		final OesingCountMap<Character> counter = new OesingCountMap<>();
		while (hand.length() > 0) {
			counter.countUp(hand.charAt(0));
			hand = hand.substring(1);
		}
		final List<Entry<Character, Integer>> list = counter.getSortedListByValue();
		final Entry<Character, Integer> first = list.get(0);
		final Entry<Character, Integer> second = list.get(1);
		if (first.getValue() == 3 && second.getValue() == 2) {
			int result = 0;
			char key1 = first.getKey();
			char key2 = second.getKey();
			key1 = Character.toLowerCase(key1);
			if (key1 == 'a') {
				result = 14 * 100;
			} else if (key1 == 'k') {
				result = 13 * 100;
			} else if (key1 == 'q') {
				result = 12 * 100;
			} else if (key1 == 'j') {
				result = 11 * 100;
			} else if (key1 == 't') {
				result = 10 * 100;
			} else {
				result = Character.digit(key1, 10) * 100;
			}
			key2 = Character.toLowerCase(key2);
			if (key2 == 'a') {
				result += 14;
			} else if (key2 == 'k') {
				result += 13;
			} else if (key2 == 'q') {
				result += 12;
			} else if (key2 == 'j') {
				result += 11;
			} else if (key2 == 't') {
				result += 10;
			} else {
				result += Character.digit(key1, 10);
			}
			return result;
		} else {
			return 0;
		}
	}

	/**
	 * @param n
	 * @return
	 */
	private boolean IsNumber(final String n) {
		try {
			return Double.parseDouble(n) != Double.NaN;
		} catch (final Exception e) {
			return false;
		}
	}

	private int isPair(String hand) {
		hand = hand.toLowerCase().replaceAll("[hcsd]", "");
		final OesingCountMap<Character> counter = new OesingCountMap<>();
		while (hand.length() > 0) {
			counter.countUp(hand.charAt(0));
			hand = hand.substring(1);
		}
		final List<Entry<Character, Integer>> list = counter.getSortedListByValue();
		final Entry<Character, Integer> first = list.get(0);
		if (first.getValue() == 2) {
			char key = first.getKey();
			key = Character.toLowerCase(key);
			if (key == 'a') {
				return 14;
			} else if (key == 'k') {
				return 13;
			} else if (key == 'q') {
				return 12;
			} else if (key == 'j') {
				return 11;
			} else if (key == 't') {
				return 10;
			} else {
				return Character.digit(key, 10);
			}
		} else {
			return 0;
		}
	}

	/**
	 * @param number String to check.
	 * @return Boolean value representing if the input is a palindrom.
	 * @see #digitSum(int)
	 * @see #digitSum(long)
	 * @see #digitSum(String)
	 */
	public boolean isPalindrom(final BigInteger number) {
		return isPalindrom(number.toString());
	}

	/**
	 * @param number String to check.
	 * @return Boolean value representing if the input is a palindrom.
	 * @see #digitSum(long)
	 * @see #digitSum(BigInteger)
	 * @see #digitSum(String)
	 */
	public boolean isPalindrom(final int number) {
		return isPalindrom("" + number);
	}

	/**
	 * @param number String to check.
	 * @return Boolean value representing if the input is a palindrom.
	 * @see #digitSum(int)
	 * @see #digitSum(BigInteger)
	 * @see #digitSum(String)
	 */
	public boolean isPalindrom(final long number) {
		return isPalindrom("" + number);
	}

	/**
	 * @param s String to check.
	 * @return Boolean value representing if the input is a palindrom.
	 * @see #digitSum(int)
	 * @see #digitSum(long)
	 * @see #digitSum(BigInteger)
	 */
	public boolean isPalindrom(final String s) {
		return s.equals(reverseString(s));
	}

	/**
	 * Calculates if a given number contains all numbers from 0-differentDigits.
	 *
	 * @param s        String to be checked
	 * @param withZero Should the zero be considered.
	 * @return Is the Input a pandigital number.
	 */
	public boolean isPandigital(int n, final boolean withZero) {
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

	/**
	 * Calculates if a given string contains all numbers from 0-differentDigits.
	 *
	 * @param s        String to be checked
	 * @param withZero Should the zero be considered.
	 * @return Is the Input a pandigital number.
	 */
	public boolean isPandigital(final String s, final boolean withZero) {
		return isPandigital(Integer.parseInt(s), withZero);
	}

	public boolean isPermutation(long n1, long n2) {
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

	/**
	 * @param s1 String one to compare to string 2.
	 * @param s2 String two to compare to string 1.
	 * @return If s1 and s2 are permutations of each other it returns true.
	 */
	public boolean isPermutation(final String s1, final String s2) {
		if (s1.length() != s2.length()) {
			return false;
		}
		final int[] arr = new int[10];
		for (int i = 0; i < s1.length(); i++) {
			arr[Character.getNumericValue(s1.charAt(i))]++;
		}
		for (int i = 0; i < s2.length(); i++) {
			arr[Character.getNumericValue(s2.charAt(i))]--;
			if (arr[Character.getNumericValue(s2.charAt(i))] < 0) {
				return false;
			}
		}
		return true;
	}

	/*
	 * http://www.mathblog.dk/files/euler/Problem60.cs
	 */
	public boolean isPrime(final int n) {
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

	private int isQuads(String hand) {
		hand = hand.toLowerCase().replaceAll("[hcsd]", "");
		final OesingCountMap<Character> counter = new OesingCountMap<>();
		while (hand.length() > 0) {
			counter.countUp(hand.charAt(0));
			hand = hand.substring(1);
		}
		final List<Entry<Character, Integer>> list = counter.getSortedListByValue();
		final Entry<Character, Integer> first = list.get(0);
		if (first.getValue() == 4) {
			char key = first.getKey();
			key = Character.toLowerCase(key);
			if (key == 'a') {
				return 14;
			} else if (key == 'k') {
				return 13;
			} else if (key == 'q') {
				return 12;
			} else if (key == 'j') {
				return 11;
			} else if (key == 't') {
				return 10;
			} else {
				return Character.digit(key, 10);
			}
		} else {
			return 0;
		}
	}

	private int isStraight(String hand) {
		hand = hand.toLowerCase().replaceAll("[dhcs]", "");
		final List<String> values = new ArrayList<>(Arrays.asList(hand.toLowerCase().split("")));
		removeDuplicates(values);
		final String[] order = new String[] { "2", "3", "4", "5", "6", "7", "8", "9", "t", "j", "q", "k", "a" };
		final Comparator<String> comp = getComparatorForGivenOrder(Arrays.asList(order));
		Collections.sort(values, comp);
		if (values.contains("a") && values.size() > 1) {
			values.add(0, "a");
		}
		int biggestValue = 0;
		int inOrder = 1;
		for (int i = values.size() - 1; i > 0; i--) {
			if ((OesingLibArray.linearSearch(order, values.get(i - 1)) + 1 == OesingLibArray.linearSearch(order,
					values.get(i))) || (values.get(i - 1).equals("a") && values.get(i).equals("2"))) {
				if (values.get(i).equals("a")) {
					biggestValue = Math.max(biggestValue, 14);
				} else if (values.get(i).equals("k")) {
					biggestValue = Math.max(biggestValue, 13);
				} else if (values.get(i).equals("q")) {
					biggestValue = Math.max(biggestValue, 12);
				} else if (values.get(i).equals("j")) {
					biggestValue = Math.max(biggestValue, 11);
				} else if (values.get(i).equals("t")) {
					biggestValue = Math.max(biggestValue, 10);
				} else {
					biggestValue = Math.max(biggestValue, Integer.parseInt(values.get(i)));
				}
				inOrder++;
				if (inOrder == 5) {
					return biggestValue;
				}
			} else {
				inOrder = 1;
				biggestValue = 0;
			}
		}
		return 0;
	}

	private int isStraightFlush(final String hand) {
		final int flush = isFlush(hand);
		final int straight = isStraight(hand);
		if ((flush == straight || (flush == 14 && straight == 5)) && flush != 0) {
			return straight;
		} else {
			return 0;
		}
	}

	private int isTrips(String hand) {
		hand = hand.toLowerCase().replaceAll("[hcsd]", "");
		final OesingCountMap<Character> counter = new OesingCountMap<>();
		while (hand.length() > 0) {
			counter.countUp(hand.charAt(0));
			hand = hand.substring(1);
		}
		final List<Entry<Character, Integer>> list = counter.getSortedListByValue();
		final Entry<Character, Integer> first = list.get(0);
		if (first.getValue() == 3) {
			char key = first.getKey();
			key = Character.toLowerCase(key);
			if (key == 'a') {
				return 14;
			} else if (key == 'k') {
				return 13;
			} else if (key == 'q') {
				return 12;
			} else if (key == 'j') {
				return 11;
			} else if (key == 't') {
				return 10;
			} else {
				return Character.digit(key, 10);
			}
		} else {
			return 0;
		}
	}

	private int isTwoPair(String hand) {
		hand = hand.toLowerCase().replaceAll("[hcsd]", "");
		final OesingCountMap<Character> counter = new OesingCountMap<>();
		while (hand.length() > 0) {
			counter.countUp(hand.charAt(0));
			hand = hand.substring(1);
		}
		final List<Entry<Character, Integer>> list = counter.getSortedListByValue();
		final Entry<Character, Integer> first = list.get(0);
		final Entry<Character, Integer> second = list.get(1);
		if (first.getValue() == 2 && second.getValue() == 2) {
			int result = 0;
			char key1 = first.getKey();
			char key2 = second.getKey();
			key1 = Character.toLowerCase(key1);
			if (key1 == 'a') {
				result = 14 * 100;
			} else if (key1 == 'k') {
				result = 13 * 100;
			} else if (key1 == 'q') {
				result = 12 * 100;
			} else if (key1 == 'j') {
				result = 11 * 100;
			} else if (key1 == 't') {
				result = 10 * 100;
			} else {
				result = Character.digit(key1, 10) * 100;
			}
			key2 = Character.toLowerCase(key2);
			if (key2 == 'a') {
				result += 14;
			} else if (key2 == 'k') {
				result += 13;
			} else if (key2 == 'q') {
				result += 12;
			} else if (key2 == 'j') {
				result += 11;
			} else if (key2 == 't') {
				result += 10;
			} else {
				result += Character.digit(key1, 10);
			}
			return result;
		} else {
			return 0;
		}
	}

	public long kgV(final long n, final long b) {
		final double x = n / ggT(n, b);
		return (long) Math.abs(x * b);
	}

	public long[] listPhi(final int limit) {
		return listPhi(2, limit);
	}

	/**
	 * @param limit  The last value in the array is phi(limit).
	 * @param lowest The first value in the array is phi(lowest)
	 * @return
	 */
	public long[] listPhi(final int lowest, final int limit) {
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
	public List<List<Long>> listPossibleMultiplicants(final int n) {
		final List<Long> factors = primeFactorizationAsList(n);
		return listPossibleMultiplicants(n, factors);
	}

	/**
	 * @param n
	 * @param primeFactors
	 * @return
	 */
	public List<List<Long>> listPossibleMultiplicants(final int n, final List<Long> primeFactors) {
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

	/**
	 * Using the Sieve of Eratosthenes to generate all primes to a specified limit.
	 *
	 * @param limit The biggest possible prime that gets returned, excluded limit
	 *              exactly.
	 * @return A List of primes.
	 */
	public List<Integer> listPrimes(final int limit) {
		return listPrimes(2, limit);
	}

	/**
	 * @param s1 First String
	 * @param s2 Second String
	 * @return The size of the longest common substring.
	 */
	public int longestCommonSubstring(final String s1, final String s2) {
		final int[][] arr = new int[s1.length() + 1][s2.length() + 1];
		int max = 0;
		for (int i = 0; i < arr.length; i++) {
			arr[i][0] = 0;
		}
		for (int i = 0; i < arr[0].length; i++) {
			arr[0][i] = 0;
		}
		for (int i = 1; i < arr.length; i++) {
			for (int j = 1; j < arr[0].length; j++) {
				if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
					arr[i][j] = 1 + arr[i - 1][j - 1];
					max = Math.max(max, arr[i][j]);
				}
			}
		}
		return max;
	}

	/**
	 * @param n
	 * @param e
	 * @param m
	 * @return n^e mod m
	 */
	public long mathModPow(final long n, final long e, final long m) {
		int product = 1;
		for (int i = 0; i < e; i++) {
			product *= n;
			product %= m;
		}
		return product;
	}

	/**
	 * @param font     Used Font.
	 * @param alphabet Alphabet we want to measure.
	 * @return A point representing (maxWidth,maxHeight).
	 */
	private Point measureFont(final Font font, final String alphabet) {
		final BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_BINARY);
		final Graphics2D g2 = image.createGraphics();
		int maxWidth = 0;
		int maxHeight = 0;
		for (final char c : alphabet.toCharArray()) {
			final TextLayout tl = new TextLayout("" + c, font, g2.getFontRenderContext());
			maxWidth = (int) Math.ceil(Math.max(maxWidth, tl.getBounds().getWidth()));
			maxHeight = (int) Math.ceil(Math.max(maxWidth, tl.getAscent() + tl.getDescent()));
		}
		return new Point(maxWidth, maxHeight);
	}

	/*
	 * http://www.mathblog.dk/files/euler/Problem60.cs
	 */
	private long modularExp(final int a, final int b, final int n) {
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
	 * @param listOfThreads   A list of callables that will all get executed in
	 *                        parallel.
	 * @param parallelThreads Number of parallel threads supported.
	 * @return Returns an array with the output of the callables.
	 */
	private Object[] multiThread(final List<Callable<Object>> listOfThreads, final int parallelThreads)
			throws InterruptedException, ExecutionException {
		int highestActiveThread = 0;
		final int startThreads = listOfThreads.size();
		final Object[] output = new Object[listOfThreads.size()];
		final List<Pair<Future<Object>, Integer>> runningThreads = new ArrayList<>();
		final ExecutorService executor = Executors.newCachedThreadPool();
		int done = 0;
		while ((listOfThreads.size() + runningThreads.size()) > 0) {
			for (int i = runningThreads.size(); i < parallelThreads && !listOfThreads.isEmpty(); i++) {
				runningThreads.add(new Pair<>(executor.submit(listOfThreads.get(0)), highestActiveThread));
				listOfThreads.remove(0);
				highestActiveThread++;
			}
			for (int j = 0; j < runningThreads.size(); j++) {
				if (runningThreads.get(j).getKey().isDone()) {
					final Pair<Future<Object>, Integer> p = runningThreads.get(j);
					if (p.getKey().get() != null) {
						output[p.getValue()] = p.getKey().get();
					}
					runningThreads.remove(j);
					done++;
				}
			}
			consoleProgressBar(done, startThreads);
		}
		executor.shutdown();
		executor.awaitTermination(1, TimeUnit.MINUTES);
		return output;
	}

	/**
	 * Normalizes an input.
	 *
	 * @param number The number that gets normalized.
	 * @param min    The minimum of the domain.
	 * @param max    The maximum of the domain.
	 * @return (x -min(x))/(max(x)-min(x)
	 */
	public double normalize(final double number, final double min, final double max) {
		double num, denum;
		num = number - min;
		denum = max - min;
		return (num / denum);
	}

	/**
	 * Opens given file in Notepad.
	 *
	 * @param path    Path to the file that gets opened.
	 * @param message Optional message that gets printed.
	 */
	private void openFile(final Path path, final String message) throws IOException {
		System.out.println(message);
		executeCmd(false, "notepad " + path);
	}

	/**
	 * @param s       String to parse.
	 * @param parseTo Type the string gets parsed to.
	 * @return Returns an Object of the given class.
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private <T> T parseStringSecure(final String s, final Class<T> parseTo) throws IOException {
		T out = null;
		if (parseTo.isInstance(new String())) {
			return (T) s;
		}
		try {
			if (parseTo.isAssignableFrom(s.getClass())) {
				return parseTo.cast(s);
			} else if (parseTo.isInstance(new Long(0))) {
				return parseTo.cast(Long.parseLong(s));
			} else if (parseTo.isInstance(new Integer(0))) {
				return parseTo.cast(Integer.parseInt(s));
			} else if (parseTo.isInstance(new Double(0))) {
				return parseTo.cast(Double.parseDouble(s));
			} else if (parseTo.isInstance(new Boolean(false))) {
				return parseTo.cast(Boolean.parseBoolean(s));
			} else if (parseTo.equals(int.class)) {
				return parseTo.cast(Integer.parseInt(s));
			}
			out = parseTo.cast(s);
		} catch (final Exception e) {
			final String updated = getInput(null,
					"Error input " + s + " has wrong type. Expected: " + parseTo.getName() + ". Try again.", null, 0, 0,
					false, "", 0);
			ignoreCall = true;
			return parseStringSecure(updated, parseTo);
		}
		return out;
	}

	/**
	 * Calculates The number of natural numbers that are relative prime to n.
	 *
	 * @param n The Number to calculate phi for.
	 * @return {a € N|1 <= a <= n, ggT(a,n) = 1}
	 */
	public long phi(int n) {
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

	/**
	 * @param width
	 * @param height
	 * @param fps
	 */
	public void playAsciiTetris(int width, int height, int fps) {
		// TODO:
		width = 12;
		height = 20;
		fps = 20;
		final String[][] field = new String[height + 2][width + 2];
		for (int r = 0; r < height + 2; r++) {
			for (int c = 0; c < width + 2; c++) {
				if (c == 0 || c == width + 1 || r == height + 1) {
					field[r][c] = "x";
				} else {
					field[r][c] = "_";
				}
			}
		}
		final ArrayList<Point> blockBottomRight = new ArrayList<>();
		blockBottomRight.add(new Point(width / 2 + 1, -1));
		while (!blockBottomRight.isEmpty()) {// Gameloop
			for (int i = 0; i < blockBottomRight.size(); i++) {
				final Point p = blockBottomRight.get(i);
				if (field[p.y + 1][p.x].equals("_") && p.y < height) {
					field[p.y + 1][p.x] = "o";
					blockBottomRight.remove(p);
					blockBottomRight.add(new Point(p.x, p.y + 1));
					if (p.y + 1 >= 4) {
						field[p.y + 1 - 4][p.x] = "_";
					}
				} else {
					consoleUpdate(printArray(field).replaceAll(".", " "));
					break;
				}
			}
			consoleUpdate(printArray(field));
			try {
				final String in = getInput(null, "", Arrays.asList(new String[] { "a", "d" }), 0, 0, false, "", 1);
				final Point p = blockBottomRight.get(0);
				if (in != null && in.equals("a")) {
					for (int i = 0; i < 4; i++) {
						field[Math.max(0, p.y - i)][p.x] = "_";
						field[Math.max(0, p.y - i)][Math.max(0, p.x - 1)] = "o";
					}
					blockBottomRight.remove(p);
					blockBottomRight.add(new Point(p.x - 1, p.y));
				} else if (in != null && in.equals("d")) {
					for (int i = 0; i < 4; i++) {
						field[Math.max(0, p.y - i)][p.x] = "_";
						field[Math.max(0, p.y - i)][Math.min(width + 1, p.x + 1)] = "o";
					}
					blockBottomRight.remove(p);
					blockBottomRight.add(new Point(p.x + 1, p.y));
				}
			} catch (final IOException e) {
				e.printStackTrace();
			}
			sleep(0.5);
		}
	}

	/**
	 * Queries a fast C Library.
	 *
	 * @param range1 First range of hands.
	 * @param range2 Second range of hands.
	 * @param board  Board "" if empty.
	 * @param dead   Deadcards "" if empty.
	 * @param reps   Repetitions that gets computed.
	 * @return Equity of range1 vs range2 on given board, with consideration of dead
	 *         cards.
	 */
	public String pokerCalculator(final String range1, String range2, String board, String dead, String reps)
			throws IOException, InterruptedException {
		Process proc;
		if (board.equals("")) {
			board = "*";
		}
		if (dead.equals("")) {
			dead = "*";
		}
		if (reps.equals("")) {
			reps = "500000";
		}
		if (range2.isEmpty()) {
			range2 = "xx";
			System.out.println("Empty range assume 100%.");
		}
		final Runtime rt = Runtime.getRuntime();
		final String[] commands = {
				"C:" + File.separator + "pbots_calc" + File.separator + "example" + File.separator + "calculator.bat",
				range1 + ":" + range2, board, dead, reps };
		proc = rt.exec(commands);
		proc.waitFor();
		final BufferedReader inPut = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		String line = null;
		while ((line = inPut.readLine()) != null) {
			if (!line.contains("C:") && !line.equals("") && !line.contains("Monte Carlo")) {
				return line;
			}
		}
		inPut.close();
		return "";
	}

	/**
	 * @param hand1 First hand.
	 * @param hand2 Second hand.
	 * @param board Board cards.
	 * @return Returns -1,0,1 if hand1 is worse,equal or better than hand2, by high
	 *         rules.
	 */
	public int pokerDecideWinner(String hand1, String hand2, final String board) {
		if (board != null && board != "") {
			hand1 = hand1 + board;
			hand2 = hand2 + board;
		}
		int compare = Integer.compare(isStraightFlush(hand1), isStraightFlush(hand2));
		if (compare != 0) {
			return compare;
		} else {
			compare = Integer.compare(isQuads(hand1), isQuads(hand2));
			if (compare != 0) {
				return compare;
			} else {
				compare = Integer.compare(isFullHouse(hand1), isFullHouse(hand2));
				if (compare != 0) {
					return compare;
				} else {
					compare = Integer.compare(isFlush(hand1), isFlush(hand2));
					if (compare != 0) {
						return compare;
					} else {
						compare = Integer.compare(isStraight(hand1), isStraight(hand2));
						if (compare != 0) {
							return compare;
						} else {
							compare = Integer.compare(isTrips(hand1), isTrips(hand2));
							if (compare != 0) {
								return compare;
							} else {
								compare = Integer.compare(isTwoPair(hand1), isTwoPair(hand2));
								if (compare != 0) {
									return compare;
								} else {
									compare = Integer.compare(isPair(hand1), isPair(hand2));
									if (compare != 0) {
										return compare;
									} else {
										return Integer.compare(highCard(hand1), highCard(hand2));
									}
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Shortcut for pokerCalculator.
	 *
	 * @see #pokerCalculator(String, String, String, String, String)
	 * @param ranges range1:range2.
	 * @param board  Board "" if empty.
	 * @return Equity of range1 vs range2 on optional board.
	 */
	public String pokerEquity(final String ranges, final String board) throws IOException, InterruptedException {
		if (board.equals("")) {
			final String[] range = ranges.split(":");
			return pokerCalculator(range[0], range[1], "", "", "");
		} else {
			final String[] range = ranges.split(":");
			return pokerCalculator(range[0], range[1], board, "", "");
		}
	}

	/*
	 * <handNr> = 170370696470
	 */
	public void pokerFakeSnowieHand(final String action) throws IOException {
		final long handNr = (long) (Math.random() * (Long.MAX_VALUE - 170370696470L) + 170370696470L);
		final String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss"));
		// TODO: final List<String> lines =
		// Files.readAllLines(Paths.get("./hand.txt"));
		System.out.println(handNr + " , " + currentDate);
	}

	/**
	 * @param handle
	 * @return
	 * @throws InterruptedException
	 */
	@SuppressWarnings("unused")
	private String pokerGetCardsFromWindow(final HWND handle) throws IOException, InterruptedException {
		BufferedImage img = windowScreenshot(handle);
		img = imageResize(img, 290, 200);
		final BufferedImage hCard1 = img.getSubimage(128, 118, 18, 22);
		final BufferedImage hCard2 = img.getSubimage(147, 118, 18, 22);
		final BufferedImage bCard1 = img.getSubimage(94, 63, 18, 22);
		final BufferedImage bCard2 = img.getSubimage(115, 63, 18, 22);
		final BufferedImage bCard3 = img.getSubimage(136, 63, 18, 22);
		final BufferedImage bCard4 = img.getSubimage(157, 63, 18, 22);
		final BufferedImage bCard5 = img.getSubimage(178, 63, 18, 22);
		if (debug) {
			imageShow("", 35, hCard1, hCard2, bCard1, bCard2, bCard3, bCard4, bCard5);
		}
		if (hashToCard == null) {
			initHashToCard();
		}
		final String[] hashOrCard = new String[7];
		hashOrCard[0] = imageToHash(hCard1);
		hashOrCard[1] = imageToHash(hCard2);
		hashOrCard[2] = imageToHash(bCard1);
		hashOrCard[3] = imageToHash(bCard2);
		hashOrCard[4] = imageToHash(bCard3);
		hashOrCard[5] = imageToHash(bCard4);
		hashOrCard[6] = imageToHash(bCard5);
		final String[] suit = new String[7];
		suit[0] = pokerSuit(hCard1);
		suit[1] = pokerSuit(hCard2);
		suit[2] = pokerSuit(bCard1);
		suit[3] = pokerSuit(bCard2);
		suit[4] = pokerSuit(bCard3);
		suit[5] = pokerSuit(bCard4);
		suit[6] = pokerSuit(bCard5);
		for (int i = 0; i < hashOrCard.length; i++) {
			hashOrCard[i] = hashToCard.get(hashOrCard[i]);
			if (hashOrCard[i] == null) {
				if (i == 0) {
					imageIdentify("D:/BotPics/Cards/", hCard1);
				}
				if (i == 1) {
					imageIdentify("D:/BotPics/Cards/", hCard2);
				}
				if (i == 2) {
					imageIdentify("D:/BotPics/Cards/", bCard1);
				}
				if (i == 3) {
					imageIdentify("D:/BotPics/Cards/", bCard2);
				}
				if (i == 4) {
					imageIdentify("D:/BotPics/Cards/", bCard3);
				}
				if (i == 5) {
					imageIdentify("D:/BotPics/Cards/", bCard4);
				}
				if (i == 6) {
					imageIdentify("D:/BotPics/Cards/", bCard5);
				}
			} else {
				hashOrCard[i] = hashOrCard[i].replace("nothing", "_") + suit[i];
			}
		}
		return Arrays.toString(hashOrCard);
	}

	/**
	 * @param ratio         A ratio like this bet:pot.
	 * @param equityPercent The equity the player got.
	 * @return The percent of hands we can profitable call or how many chips we need
	 *         to get on later streets to make it profitable.
	 */
	public String pokerOdds(final String ratio, final String equityPercent) {
		if (equityPercent.equals("")) {
			final String[] odds = ratio.split(":");
			final double x = Double.parseDouble(odds[0]);
			final double y = Double.parseDouble(odds[1]);
			return Double.toString(x / (x + y));
		} else {
			final String[] odds = ratio.split(":");
			final double x = Double.parseDouble(odds[0]);
			final double y = Double.parseDouble(odds[1]);
			final double oddsLayed = x / (x + y);
			final double oddsHaving = Double.parseDouble(equityPercent);
			return Double.toString(y * ((oddsLayed / oddsHaving) - 1));
		}
	}

	/**
	 * @param request Returns advice for a No limit Holdem Preflop situation,
	 *                reading from a suggested range from Pokersnowie.
	 * @return An Poker advice for preflop action.
	 */
	public String pokerPreFlopAdvice(final String[] request) throws IOException {
		final String folder = "D:/BotPics/Preflop/";
		if (request.length == 0 || request[0].equals("")) {
			System.out.println(
					"pokerPreFlopAdvice <UTG,HJ,CO,BTN,SB,BB> <Hand>/ <Open/R3/R2.5/3BET/4Bet/Squeeze> <UTG,HJ,CO,BTN,SB,BB> <UTG,HJ,CO,BTN,SB,BB> <HJ,CO,BTN,SB> <Hand>");
		}
		final String[] positions = new String[] { "UTG", "HJ", "CO", "BTN", "SB", "BB" };
		File f;
		Element table;
		JFrame frame;
		switch (request[0].toLowerCase()) {
		case "open":
			if (debug) {
				System.out.println("Option: open");
			}
			f = new File(folder + request[1].toUpperCase() + "_Open.html");
			table = Jsoup.parse(f, "UTF-8").select("table").get(0);
			if (request.length < 3) {
				frame = htmlToSwing(null, null, f);
				frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
				while (frame.isVisible()) {
					sleep(1);
				} // Wait for it.
				return "Frame closed";
			} else {
				final StringBuilder out = new StringBuilder();
				out.append(readHandChart(request[2].toUpperCase(), table));
				if (out.toString().toLowerCase().contains("raise!")) {
					out.append("\nIf you get 3Bet:");
					for (int i = OesingLibArray.linearSearch(positions, request[1].toUpperCase())
							+ 1; i < positions.length; i++) {
						f = new File(folder + request[1].toUpperCase() + "_" + positions[i] + "RPOT.html");
						table = Jsoup.parse(f, "UTF-8").select("table").get(0);
						if (positions[i].length() == 3) {
							out.append("\n" + positions[i] + " " + readHandChart(request[2].toUpperCase(), table));
						} else {
							out.append("\n" + positions[i] + "  " + readHandChart(request[2].toUpperCase(), table));
						}
					}
				}
				return out.toString();
			}
		case "r2.5":
			if (debug) {
				System.out.println("Option: r2.5");
			}
			if (OesingLibArray.linearSearch(positions, request[1].toUpperCase()) < OesingLibArray
					.linearSearch(positions, request[2].toUpperCase())) {
				swapArray(request, 1, 2);
			} else if (OesingLibArray.linearSearch(positions, request[1].toUpperCase()) == OesingLibArray
					.linearSearch(positions, request[2].toUpperCase())) {
				return "Same position";
			}
			f = new File(folder + request[1].toUpperCase() + "_" + request[2].toUpperCase() + "R2.5BB.html");
			table = Jsoup.parse(f, "UTF-8").select("table").get(0);
			if (request.length < 4) {
				frame = htmlToSwing(null, null, f);
				frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
				while (frame.isVisible()) {
					sleep(1);
				} // Wait for it.
				return "Frame closed";
			} else {
				final StringBuilder out = new StringBuilder();
				out.append(readHandChart(request[3], table));
				if (out.toString().toLowerCase().contains("raise!")) {
					out.append("\nIf you get 4Bet:");
					f = new File(folder + request[1].toUpperCase() + "_" + request[2].toUpperCase() + "RPOT.html");
					table = Jsoup.parse(f, "UTF-8").select("table").get(0);
					out.append("\n" + readHandChart(request[3], table));
				}
				return out.toString();
			}
		case "r3":
			if (debug) {
				System.out.println("Option: r3");
			}
			if (OesingLibArray.linearSearch(positions, request[1].toUpperCase()) < OesingLibArray
					.linearSearch(positions, request[2].toUpperCase())) {
				swapArray(request, 1, 2);
			} else if (OesingLibArray.linearSearch(positions, request[1].toUpperCase()) == OesingLibArray
					.linearSearch(positions, request[2].toUpperCase())) {
				return "Same position";
			}
			f = new File(folder + request[1].toUpperCase() + "_" + request[2].toUpperCase() + "R3BB.html");
			table = Jsoup.parse(f, "UTF-8").select("table").get(0);
			if (request.length < 4) {
				frame = htmlToSwing(null, null, f);
				frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
				while (frame.isVisible()) {
					sleep(1);
				} // Wait for it.
				return "Frame closed";
			} else {
				final StringBuilder out = new StringBuilder();
				out.append(readHandChart(request[3], table));
				if (out.toString().toLowerCase().contains("raise!")) {
					out.append("\nIf you get 4Bet:");
					f = new File(folder + request[1].toUpperCase() + "_" + request[2].toUpperCase() + "RPOT.html");
					table = Jsoup.parse(f, "UTF-8").select("table").get(0);
					out.append("\n" + readHandChart(request[3], table));
				}
				return out.toString();
			}
		case "3bet":
			if (debug) {
				System.out.println("Option: 3bet");
			}
			if (OesingLibArray.linearSearch(positions, request[1].toUpperCase()) == OesingLibArray
					.linearSearch(positions, request[2].toUpperCase())) {
				return "Same position";
			}
			if (OesingLibArray.linearSearch(positions, request[1].toUpperCase()) > OesingLibArray
					.linearSearch(positions, request[2].toUpperCase())) {
				return "You cant 3Bet from those positions";
			}
			f = new File(folder + request[1].toUpperCase() + "_" + request[2].toUpperCase() + "RPOT.html");
			table = Jsoup.parse(f, "UTF-8").select("table").get(0);
			if (request.length < 4) {
				frame = htmlToSwing(null, null, f);
				frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
				while (frame.isVisible()) {
					sleep(1);
				} // Wait for it.
				return "Frame closed";
			} else {
				final String out = readHandChart(request[3], table);
				return out;
			}
		case "4bet":
			if (debug) {
				System.out.println("Option: 4bet");
			}
			if (OesingLibArray.linearSearch(positions, request[1].toUpperCase()) == OesingLibArray
					.linearSearch(positions, request[2].toUpperCase())) {
				return "Same position";
			}
			if (OesingLibArray.linearSearch(positions, request[1].toUpperCase()) < OesingLibArray
					.linearSearch(positions, request[2].toUpperCase())) {
				return "You cant 4Bet from those positions";
			}
			f = new File(folder + request[2].toUpperCase() + "_" + request[1].toUpperCase() + "RPOT.html");
			table = Jsoup.parse(f, "UTF-8").select("table").get(0);
			if (request.length < 4) {
				frame = htmlToSwing(null, null, f);
				frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
				while (frame.isVisible()) {
					sleep(1);
				} // Wait for it.
				return "Frame closed";
			} else {
				final String out = readHandChart(request[3], table);
				return out;
			}
		case "squeeze":
			if (debug) {
				System.out.println("Option: squeeze");
			}
			if ((!request[1].toUpperCase().equals("BB") && !request[1].toUpperCase().equals("SB"))
					|| OesingLibArray.linearSearch(positions, request[1].toUpperCase()) <= 1
					+ OesingLibArray.linearSearch(positions, request[2].toUpperCase())
					|| OesingLibArray.linearSearch(positions, request[2].toUpperCase()) >= OesingLibArray
					.linearSearch(positions, request[3].toUpperCase())) {
				return "You cant Squeeze from those positions";
			}
			switch (request[3].toUpperCase()) {
			case "HJ":
				request[3] = "2nd";
				break;
			case "CO":
				request[3] = "3rd";
				break;
			case "BTN":
				request[3] = "";
				break;
			case "SB":
				request[3] = "SB";
				break;
			}
			f = new File(folder + request[1].toUpperCase() + "_" + request[2].toUpperCase() + "R3BBC" + request[3]
					+ ".html");
			table = Jsoup.parse(f, "UTF-8").select("table").get(0);
			if (request.length < 5) {
				frame = htmlToSwing(null, null, f);
				frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
				while (frame.isVisible()) {
					sleep(1);
				} // Wait for it.
				return "Frame closed";
			} else {
				final String out = readHandChart(request[4], table);
				return out;
			}
		case "utg":
		case "hj":
		case "co":
		case "btn":
		case "sb":
		case "bb":
			if (debug) {
				System.out.println("Option: Complete guide");
			}
			if (request.length != 2) {
				System.out.println("No hand <Position> <Hand>");
			}
			final String pos = request[0];
			final String hand = request[1];
			final StringBuilder out = new StringBuilder();
			out.append("Open:" + "\n" + pokerPreFlopAdvice(new String[] { "open", pos, hand }));
			out.append("\n" + "Raise 2.5bb:" + "\n");
			for (int i = 0; i < OesingLibArray.linearSearch(positions, pos.toUpperCase()); i++) {
				out.append(positions[i] + ": " + pokerPreFlopAdvice(new String[] { "r2.5", pos, positions[i], hand }));
				if (i < OesingLibArray.linearSearch(positions, pos.toUpperCase()) - 1) {
					out.append("\n");
				}
			}
			out.append("\n" + "Raise 3bb:" + "\n");
			for (int i = 0; i < OesingLibArray.linearSearch(positions, pos.toUpperCase()); i++) {
				out.append(positions[i] + ": " + pokerPreFlopAdvice(new String[] { "r3", pos, positions[i], hand }));
				if (i < OesingLibArray.linearSearch(positions, pos.toUpperCase()) - 1) {
					out.append("\n");
				}
			}
			return out.toString();
		default:
			System.out.println(
					"Error no action found: pokerPreFlopAdvice <Open/R3/R2.5/3BET/4Bet/Squeeze> <UTG,HJ,CO,BTN,SB,BB> <UTG,HJ,CO,BTN,SB,BB> <HJ,CO,BTN,SB> <Hand>");
			break;
		}
		return null;
	}

	/**
	 * @param ante     Size of the ante. (0%/10%/12.5%/20%)
	 * @param blind    Number of blinds the player has.
	 * @param position Position of the player. (0 = Smallblind 8 = UTG)
	 * @return All hands the player should open push.
	 */
	@SuppressWarnings("unchecked") // It is always a map.
	public String pokerPushFoldAdvice(double ante, final double blind, final int position) {
		if (blind > 20 || position > 8) {
			System.out.println("Error blind to high: " + (blind > 20) + ", position to high: " + (position > 8));
			return "";
		}
		if (pushFold == null) {
			try {
				final File f = new File("D:/BotPics/jennifearPushFold.ser");
				if (f.exists()) {
					final ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
					pushFold = (HashMap<Integer, HashMap<Integer, HashMap<Integer, String>>>) in.readObject();
					in.close();
				} else {
					getPushFoldChart();
				}
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		final double[] possibleAnte = new double[] { 0, 10, 12.5, 20 };
		ante = closestValue(ante, possibleAnte);
		if (debug) {
			System.out.println("Ante: " + ante + " Blind: " + blind + " Position: " + position);
		}
		final String advice = pushFold.get((int) ante).get((int) blind).get(position);
		return advice;
	}

	/**
	 * @param board        Board "" if empty.
	 * @param topPercent   Only prints top X percent of hands. 100% = all 0% = no
	 *                     hands.
	 * @param handsInRange Starting range.
	 * @return A List of Pokerhands by strength.
	 */
	public List<String> pokerRankHands(final String board, final int topPercent, final List<String> handsInRange)
			throws InterruptedException, ExecutionException {
		final char[] values = new char[] { 'A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2' };
		final char[] suits = new char[] { 'c', 's', 'h', 'd' };
		final List<String> cards = new ArrayList<>();
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < suits.length; j++) {
				cards.add("" + values[i] + suits[j]);
			}
		}
		ArrayList<String> hands;
		if (handsInRange == null) { // consider every Hand.
			hands = new ArrayList<>();
			for (int i = 0; i < cards.size(); i++) {
				for (int j = i + 1; j < cards.size(); j++) {
					if (i != j) {
						if (board != null && !board.contains(cards.get(i)) && !board.contains(cards.get(j))) {
							hands.add("" + cards.get(i) + cards.get(j));
						}
					}
				}
			}
		} else {
			for (final String s : handsInRange) {
				if (board.contains(s)) {
					handsInRange.remove(s);
				}
			}
			hands = new ArrayList<>(handsInRange);
		}
		final String[] handsArr = hands.toArray(new String[0]);
		sortInsertionArray(handsArr, true, new Function<String, Double>() {
			@Override
			public Double apply(final String hand) {
				try {
					final Double d = Double.parseDouble(pokerCalculator(hand, "xx", board, "", "10000").split(" ")[1]);
					return d;
				} catch (NumberFormatException | IOException | InterruptedException e) {
					e.printStackTrace();
					return null;
				}
			}
		});
		hands = new ArrayList<>(Arrays.asList(handsArr));
		final int delete = (int) (1.0 * hands.size() * ((100.0 - topPercent) / 100.0));
		for (int i = 0; i < delete; i++) {
			hands.remove(hands.size() - 1);
		}
		return hands;
	}

	private String pokerSuit(final BufferedImage img) {
		final Color c = new Color(img.getRGB(img.getWidth() - 2, 2));
		if (suitColors == null) {
			suitColors = new Color[4];
			suitColors[0] = new Color(26, 55, 77); // diamond
			suitColors[1] = new Color(119, 35, 35); // heart
			suitColors[2] = new Color(28, 95, 28); // club
			suitColors[3] = new Color(63, 63, 63); // spades
		}
		switch (OesingLibArray.linearSearch(suitColors, c)) {
		case 0:
			return "d";
		case 1:
			return "h";
		case 2:
			return "c";
		case 3:
			return "s";
		default:
			return "";
		}
	}

	/**
	 * Calculates the primefactorization of the input.
	 *
	 * @param n The number that gets factorized
	 * @return An array of the factors in increasing order.
	 */
	public long[] primeFactorization(final long n) {
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
	public long[] primeFactorization(final long n, final List<Integer> primes) {
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
	 *
	 * @param n      The number that gets factorized
	 * @param primes An array of primes up to the square root of the number or
	 *               bigger.
	 * @return A list of the factors in increasing order.
	 */
	public List<Long> primeFactorizationAsList(final long n) {
		final List<Integer> primes = listPrimes((int) (Math.sqrt(n) + 1));
		return primeFactorizationAsList(n, primes);
	}

	/**
	 *
	 * @param n      The number that gets factorized
	 * @param primes An array of primes up to the square root of the number or
	 *               bigger.
	 * @return A list of the factors in increasing order.
	 */
	public List<Long> primeFactorizationAsList(final long n, final List<Integer> primes) {
		final long[] factors = primeFactorization(n, primes);
		final List<Long> out = new ArrayList<>();
		for (final Long factor : factors) {
			out.add(factor);
		}
		return out;
	}

	/**
	 * @param arr     The array that should get printed.
	 * @param inLines If true every element is printed to a new line.
	 * @see #printArray(Object[], boolean)
	 */
	public void printArray(final int[] arr, final boolean inLines) {
		if (inLines) {
			for (final int i : arr) {
				System.out.println(i);
			}
		} else {
			System.out.println(Arrays.toString(arr));
		}
	}

	/**
	 * @param arr     The array that should get printed.
	 * @param inLines If true every element is printed to a new line.
	 * @see #printArray(int[], boolean)
	 */
	private void printArray(final Object[] arr, final boolean inLines) {
		if (inLines) {
			for (final Object o : arr) {
				System.out.println(o);
			}
		} else {
			System.out.println(Arrays.toString(arr));
		}
	}

	/**
	 * Prints a 2D Array in a block style.
	 *
	 * @param arr A 2D Array of Object.
	 *
	 * @return Returns the 2D Array as String.
	 */
	private String printArray(final Object[][] arr) {
		final StringBuilder out = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			out.append(Arrays.toString(arr[i]) + "\n");
			System.out.println(Arrays.toString(arr[i]));
		}
		return out.toString();
	}

	/**
	 * @param functionName Prints the javadoc for the given function. It does that
	 *                     by parsing the source file.
	 */
	private void printJavadoc(final String functionName) throws IOException {
		final String regex = ".*(public|private).*" + functionName + "\\(.*";
		final Path src = Paths.get("").resolve("src/de/oesing/all/OesingLibrary.java");
		final List<String> lines = Files.readAllLines(src);
		String line;
		for (int i = 0; i < lines.size(); i++) {
			line = lines.get(i);
			if (line.toLowerCase().matches(regex)) {
				i -= 2;
				final Stack<String> stack = new Stack<>();
				line = lines.get(i);
				while (!line.contains("/**")) {
					stack.push(line.trim());
					i--;
					line = lines.get(i);
				}
				while (!stack.isEmpty()) {
					System.out.println(stack.pop());
				}
				return;
			}
		}
		System.out.println("Function not found");
	}

	/**
	 * Windows only.
	 *
	 * @param processName     Name of process very likely a .exe
	 * @param showProcesslist True if the current processlist should be printed to
	 *                        console.
	 * @return True if process is running.
	 */
	public boolean processRunning(final String processName, final boolean showProcesslist) throws IOException {
		String line;
		final StringBuilder pidInfo = new StringBuilder();
		final Process p = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe");
		final BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
		while ((line = input.readLine()) != null) {
			pidInfo.append(line + '\n');
		}
		input.close();
		final String processlist = pidInfo.toString();
		if (showProcesslist) {
			System.out.println(processlist);
		}
		if (processlist.toLowerCase().contains(processName.toLowerCase())) {
			return true;
		}
		return false;
	}

	/**
	 * @param hand  The hand that gets extracted out of the handchart.
	 * @param table HTML Table representing the handchart.
	 * @return Returns an advice reading from a handChart.
	 */
	private String readHandChart(String hand, final Element table) {
		hand = hand.toUpperCase();
		final Character[] cardValue = new Character[] { 'A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3',
		'2' };
		final boolean suited = hand.contains("S");
		Point position = null;
		if (suited) {
			if (debug) {
				System.out.println("ReadHandChart: suited");
			}
			final int x = OesingLibArray.linearSearch(cardValue, hand.charAt(0));
			final int y = OesingLibArray.linearSearch(cardValue, hand.charAt(1));
			position = new Point(y + 1, x + 1);
		} else if ((hand.charAt(0) == hand.charAt(1)) && 0 <= OesingLibArray.linearSearch(cardValue, hand.charAt(0))) { // pair
			if (debug) {
				System.out.println("ReadHandChart: pair");
			}
			final char c = hand.charAt(0);
			final int i = OesingLibArray.linearSearch(cardValue, c);
			position = new Point(i + 1, i + 1);
		} else if (hand.charAt(0) != hand.charAt(1)) { // offsuit non pair
			if (debug) {
				System.out.println("ReadHandChart: offsuit");
			}
			final int x = OesingLibArray.linearSearch(cardValue, hand.charAt(0));
			final int y = OesingLibArray.linearSearch(cardValue, hand.charAt(1));
			position = new Point(x + 1, y + 1); // mirrored to suited
		}
		if (position == null) {
			System.out.println("Not a Hand: " + hand);
			return "";
		} else {
			final Element value = table.select("tr:nth-child(" + position.y + ") > td:nth-child(" + position.x + ")")
					.get(0);
			final String text = value.text();
			final String clas = value.className();
			final StringBuilder sb = new StringBuilder();
			if (!text.equals("")) {
				final int r = ThreadLocalRandom.current().nextInt(100) + 1;
				final boolean agressive = r <= Integer.parseInt(text);
				sb.append("You should ");
				if (clas.equals("c")) {
					sb.append("raise or call, this time: ");
					if (agressive) {
						sb.append("raise! (" + r + "/" + text + ")");
					} else {
						sb.append("call! (" + r + "/" + text + ")");
					}
				} else {
					sb.append("raise or fold, this time: ");
					if (agressive) {
						sb.append("raise! (" + r + "/" + text + ")");
					} else {
						sb.append("fold! (" + r + "/" + text + ")");
					}
				}
			} else {
				if (clas.equals("c")) {
					sb.append("You should always call!");
				} else if (clas.equals("r")) {
					sb.append("You should always raise!");
				} else {
					sb.append("You should always fold!");
				}
			}
			return sb.toString();
		}
	}

	private <T> List<T> removeDuplicates(final List<T> values) {
		final LinkedHashSet<T> set = new LinkedHashSet<>();
		for (final T t : values) {
			set.add(t);
		}
		final List<T> out = new ArrayList<>();
		out.addAll(set);
		return out;
	}

	/**
	 * @param s String to reverse
	 * @return Reversed Input
	 */
	public String reverseString(final String s) {
		return new StringBuilder(s).reverse().toString();
	}

	/*
	 * X and Y are two different whole numbers greater than 1. Their sum is no
	 * greater than 100, and Y is greater than X. S and P are two mathematicians
	 * (and consequently perfect logicians); S knows the sum X + Y and P knows the
	 * product X * Y. Both S and P know all the information in this paragraph.
	 *
	 * The following conversation occurs:
	 *
	 * S says "P does not know X and Y." P says "Now I know X and Y." S says
	 * "Now I also know X and Y." What are X and Y?
	 */
	public void solveLuziferPuzzle() {
		final List<Integer> primes = listPrimes(50);
		final ArrayList<Integer> possibleSums = new ArrayList<>();
		for (int i = 4; i < 50; i++) {
			boolean add = true;
			for (final Integer prime : primes) {
				if (prime > i - 2) {
					break;
				}
				if (Collections.binarySearch(primes, i - prime) >= 0) {
					add = false;
					break;
				}
			}
			if (add) {
				possibleSums.add(i);
			}
		}
		System.out.println("This are all sums created by atleast one non prime below 50: " + "\n" + possibleSums);
		for (final Integer sum : possibleSums) {
			final ArrayList<Integer> products = new ArrayList<>();
			System.out.println("Trying: " + sum + "\n" + " Can be summed by: ");
			for (int i = 2; i <= sum / 2; i++) {
				System.out.print(" " + i + "+" + (sum - i));
				products.add(i * (sum - i));
			}
			System.out.println("\n" + " Giving us this possible products: " + "\n " + products);
			int solutions = 0;
			String solution = "";
			for (final Integer product : products) {
				System.out.print("  " + product);
				int end = product / 2;
				int exclamationMarks = 0;
				for (int j = 2; j < end; j++) {
					final double x = (1.0 * product) / (1.0 * j);
					if (Math.floor(x) == Math.ceil(x)) {
						System.out.print(" = " + j + "*" + (int) x + " (" + (int) (j + x) + ")");
						if (possibleSums.contains((int) (j + x))) {
							System.out.print("!");
							if (solution == "") {
								solution = "" + j + "+" + (int) x + " = " + (j + (int) x) + " | " + j + "*" + (int) x
										+ " = " + (j * (int) x);
							}
							exclamationMarks++;
						}
						end = (int) x;
					}
				}
				if (exclamationMarks == 1) {
					System.out.print("  <-- Possible solution");
					solutions++;
					if (solutions > 1) {
						solution = "";
					}
				} else {
					System.out.print("  <-- Not unique sum");
					if (solutions != 1) {
						solution = "";
					}
				}
				System.out.println("");
			}
			if (solutions == 1) {
				System.out.println("Solution  => " + solution);
				break;
			}
		}

	}

	/**
	 * Solves a 9x9 sudoku by the traditional rules.
	 *
	 * @param board A representation of the sudoku that is going to be solved.
	 * @see #solveSudoku(String)
	 * @return Returns a single solution to the given sudoku board.
	 */
	public int[][] solveSudoku(final int[][] board) {
		final List<Integer> alreadyPlaced = new ArrayList<>();
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (board[i][j] != 0) {
					alreadyPlaced.add(9 * i + j);
				}
			}
		}
		// solveSudoku
		// 031020000400060080000098040004001000710000052000600900040750000020030006000010370
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
	 * @param board A String representation of the sudoku that is going to be
	 *              solved. Zeros should be placed in empty fields.
	 * @return Returns a single solution to the given sudoku board as a int array
	 *         9x9.
	 */
	public int[][] solveSudoku(final String board) {
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
	 * First translates every object of the array into a double value, and sorts the
	 * resulting double array and the input array in parallel with Insertionsort.
	 *
	 * @param <T>       Generic type, to use this method there has to be a way to
	 *                  turn the type into a double.
	 * @param inArr     Input array, that gets sorted.
	 * @param reverse   true = high-> low, false = low-> high sorting.
	 * @param operation Operation :: T -> Double
	 * @return the sorted array.
	 */
	private <T> T[] sortInsertionArray(final T[] inArr, final boolean reverse, final Function<T, Double> operation)
			throws InterruptedException, ExecutionException {
		Object[] arr = new Double[inArr.length];
		final ArrayList<Callable<Object>> threads = new ArrayList<>();
		for (final T t : inArr) {
			threads.add(new Callable<Object>() {
				@Override
				public Object call() throws Exception {
					return operation.apply(t);
				}
			});
		}
		arr = multiThread(threads, Runtime.getRuntime().availableProcessors());
		for (int i = 0; i < arr.length - 1; i++) {
			double minMax = (double) arr[i];
			int minMaxPos = i;
			for (int j = i + 1; j < arr.length; j++) {
				final double x = (double) arr[j];
				if (reverse) {
					if (x > minMax) {
						minMax = x;
						minMaxPos = j;
					}
				} else {
					if (x < minMax) {
						minMax = x;
						minMaxPos = j;
					}
				}
			}
			swapArray(arr, i, minMaxPos);
			swapArray(inArr, i, minMaxPos);
		}
		return inArr;
	}

	/**
	 * First translates every object of the list into a double value, and sorts the
	 * resulting double array and the input list in parallel with Insertionsort.
	 *
	 * @param <T>       Generic type, to use this method there has to be a way to
	 *                  turn the type into a double.
	 * @param list      Input list, that gets sorted.
	 * @param reverse   true = high -> low, false = low -> high sorting.
	 * @param operation Operation :: T -> Double
	 * @return the sorted list.
	 */
	@SuppressWarnings("unused")
	private <T> List<T> sortInsertionList(final List<T> list, final boolean reverse,
			final Function<T, Double> operation) throws InterruptedException, ExecutionException {
		Object[] arr = new Double[list.size()];
		final ArrayList<Callable<Object>> threads = new ArrayList<>();
		for (final T t : list) {
			threads.add(new Callable<Object>() {
				@Override
				public Object call() throws Exception {
					return operation.apply(t);
				}
			});
		}
		arr = multiThread(threads, Runtime.getRuntime().availableProcessors());
		for (int i = 0; i < arr.length - 1; i++) {
			double minMax = (double) arr[i];
			int minMaxPos = i;
			for (int j = i + 1; j < arr.length; j++) {
				final double x = (double) arr[j];
				if (reverse) {
					if (x > minMax) {
						minMax = x;
						minMaxPos = j;
					}
				} else {
					if (x < minMax) {
						minMax = x;
						minMaxPos = j;
					}
				}
			}
			swapArray(arr, i, minMaxPos);
			swapList(list, i, minMaxPos);
		}
		return list;
	}

	/**
	 *
	 */
	public void startLocalServer(final String root) throws IOException {
		executeCmd(true, "start", "cmd", "/k", "cd " + root + "&& \"C:/Program Files (x86)/PHP/php.exe\"", "-S",
				"localhost:8000");
		sleep(2);
		if (processRunning("php.exe", false)) {
			System.out.println("Server didnt correctly shutdown.");
		}
	}

	/**
	 * @param prefix       A string representing a prefix.
	 * @param listOfString A list of strings, that get tested if they start with the
	 *                     given prefix.
	 * @return Returns all strings starting with the prefix.
	 */
	private List<String> stringsWithPrefix(final String prefix, final List<String> listOfString) {
		final List<String> out = new ArrayList<>();
		for (final String s : listOfString) {
			if (s.toLowerCase().startsWith(prefix.toLowerCase())) {
				out.add(s);
			}
		}
		return out;
	}

	// youtube-dl https://www.youtube.com/watch?v=iGtEH1i78sI -x --audio-format
	// mp3

	/**
	 * Swaps two elements in an generic array.
	 *
	 * @param T    Type of the object in the array.
	 * @param arr  Given array.
	 * @param pos1 First position.
	 * @param pos2 Second position.
	 */
	public <T> void swapArray(final T[] arr, final int pos1, final int pos2) {
		final T t = arr[pos1];
		arr[pos1] = arr[pos2];
		arr[pos2] = t;
	}

	/**
	 * Swaps two elements in an generic list.
	 *
	 * @param T    Type of the object in the list.
	 * @param list Given list.
	 * @param pos1 First position.
	 * @param pos2 Second position.
	 */
	public <T> void swapList(final List<T> list, final int pos1, final int pos2) {
		Collections.swap(list, pos1, pos2);
	}

	/**
	 * @param channel The Twitchchannel we want to monitor.
	 */
	public void twitchChat(final String[] channel) throws NickAlreadyInUseException, IOException, IrcException {
		final ChatBot bot = new ChatBot();
		bot.setVerbose(false);
		bot.connect("irc.twitch.tv", 6667, twitchLoadAuth().get(1).toLowerCase());
		bot.joinChannel("#" + channel[0].toLowerCase());
		System.out.println("Opened channel to " + "#" + channel[0].toLowerCase());
		String line = "";
		while (true) {
			line = getInput(null, "", null, 0, 0, false, null, 0);
			if (line.equals("exit")) {
				bot.disconnect();
				bot.dispose();
				break;
			} else {
				bot.sendMessage("#" + channel[0].toLowerCase(), line);
			}
		}
	}

	/**
	 * @return Authentifaction String 1. Livestreamer 2. Chat.
	 */
	private List<String> twitchLoadAuth() throws IOException {
		List<String> lines;
		lines = Files.readAllLines(Paths.get("C:/Users/Naix/desktop/config.txt"));
		final List<String> output = new ArrayList<>();
		String auth = lines.get(lines.size() - 2);
		String chat = lines.get(lines.size() - 1);
		auth = auth.split("=")[1];
		chat = chat.split("=")[1];
		final String[] parts = auth.split(",");
		final String[] parts2 = chat.split(",");
		final StringBuilder sb = new StringBuilder();
		final StringBuilder sb2 = new StringBuilder();
		for (final String s : parts) {
			sb.append(Character.toString((char) Integer.parseInt(s)));
		}
		for (final String s : parts2) {
			sb2.append(Character.toString((char) Integer.parseInt(s)));
		}
		output.add(sb.toString());
		output.add(sb2.toString());
		return output;
	}

	/**
	 * Opens a command line tool to watch streams from twitch.tv without registering
	 * in their api program, but a key for Livestreamer is necessary.
	 */
	public void twitchStart() throws InterruptedException, ExecutionException, IOException {
		System.out.println("Listing online channels.");
		final String s = htmlBuildRequest(
				"https://api.twitch.tv/kraken/users/schnorzel/follows/channels?direction=DESC&limit=1500&offset=0&sortby=created_at&on_site=1",
				"GET",
				"Connection: keep-alive" + "X-CSRF-Token: je/QQfiPtjIWxtJxSRKi9AbVpircp0ViW+iLY/TvNu0="
						+ "User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36"
						+ "Accept: application/vnd.twitchtv.v3+json"
						+ "Twitch-Api-Token: de588a4470a7b2729d810d5ff4ca865f" + "X-Requested-With: XMLHttpRequest"
						+ "Client-ID: jzkbprff40iqj646a697cyrvl0zt2m6"
						+ "Referer: https://api.twitch.tv/crossdomain/receiver.html?v=2"
						+ "Accept-Encoding: gzip, deflate, sdch, br"
						+ "Accept-Language: de-DE,de;q=0.8,en-US;q=0.6,en;q=0.4");
		final List<String> streams = findAllRegex(s,
				"https://api\\.twitch\\.tv/kraken/users/schnorzel/follows/channels/[a-zA-Z0-9_]*");
		for (int i = 0; i < streams.size(); i++) {
			streams.set(i, Paths.get(streams.get(i).replaceAll(":", "")).getFileName().toString());
		}
		final String auth = twitchLoadAuth().get(0);
		final List<Callable<Object>> threads = new ArrayList<>();
		for (final String stream : streams) {
			threads.add(new Callable<Object>() {
				@Override
				public Object call() throws IOException {
					final String s = executeCmd(false, new String[] {
							"cd \"C:/Program Files (x86)/livestreamer-v1.12.2\" & livestreamer --twitch-oauth-token "
									+ auth + " www.twitch.tv/" + stream }).toLowerCase();
					if (!s.contains("no streams found on this url")) {
						System.out.println("Channel live: " + stream);
					}
					return null;
				}

				@Override
				public String toString() {
					return stream;
				}
			});
		}
		multiThread(threads, Runtime.getRuntime().availableProcessors());
		System.out.println("Ready");
		while (true) {
			final String in = getInput(null, "Which channel do you want to watch", null, 0, 0, false, null, 0);
			if (in.equalsIgnoreCase("exit")) {
				break;
			} else if (in.contains(",")) {
				final String[] channels = in.split(",");
				threads.clear();
				for (final String channel : channels) {
					threads.add(new Callable<Object>() {

						@Override
						public Object call() throws IOException {
							executeCmd(true, new String[] {
									"cd \"C:/Program Files (x86)/livestreamer-v1.12.2\" & livestreamer --twitch-oauth-token "
											+ auth + " www.twitch.tv/" + channel + " best" });
							return null;
						}

						@Override
						public String toString() {
							return channel;
						}

					});
				}
				multiThread(threads, Runtime.getRuntime().availableProcessors());
			} else {
				executeCmd(true, new String[] {
						"cd \"C:/Program Files (x86)/livestreamer-v1.12.2\" & livestreamer --twitch-oauth-token " + auth
						+ " www.twitch.tv/" + in + " best" });
			}
		}
	}

	/**
	 * @param regex Typical regex, that gets applied to every windows name.
	 * @return Returns a Handle for every window that statisfies the given regex.
	 */
	private List<HWND> windowGetHandles(final String regex) throws IOException {
		windows = new ArrayList<>();
		final User32 user32 = User32.INSTANCE;
		user32.EnumWindows(new WNDENUMPROC() {
			@Override
			public boolean callback(final HWND arg0, final Pointer arg1) {
				final char[] windowText = new char[512];
				user32.GetWindowText(arg0, windowText, 512);
				final String wText = Native.toString(windowText);
				if (debug) {
					System.out.println(wText);
				}
				// No name
				if (wText.isEmpty()) {
					return true;
				}
				if (wText.toLowerCase().matches(regex) || wText.toLowerCase().contains(regex.toLowerCase())) {
					windows.add(arg0);
				}
				return true;
			}
		}, null);
		if (windows.isEmpty()) {
			System.out.println("No Window associating with given regex");
			windows = null;
			return new ArrayList<>();
		}
		return windows;
	}

	/**
	 * http://stackoverflow.com/questions/4433994/java-window-image/4682351# 4682351
	 *
	 * @param handle Handle of the window that gets captured.
	 * @return Screenshot of the window.
	 */
	public BufferedImage windowScreenshot(final HWND handle) {
		// TODO: Broken.
		final HDC hdcWindow = User32.INSTANCE.GetDC(handle);
		final HDC hdcMemDC = GDI32.INSTANCE.CreateCompatibleDC(hdcWindow);

		final RECT bounds = new RECT();
		// User32Extra.INSTANCE.GetClientRect(handle, bounds);

		final int width = bounds.right - bounds.left;
		final int height = bounds.bottom - bounds.top;

		final HBITMAP hBitmap = GDI32.INSTANCE.CreateCompatibleBitmap(hdcWindow, width, height);

		final HANDLE hOld = GDI32.INSTANCE.SelectObject(hdcMemDC, hBitmap);
		// GDI32Extra.INSTANCE.BitBlt(hdcMemDC, 0, 0, width, height, hdcWindow,
		// 0, 0, WinGDIExtra.SRCCOPY);

		GDI32.INSTANCE.SelectObject(hdcMemDC, hOld);
		GDI32.INSTANCE.DeleteDC(hdcMemDC);

		final BITMAPINFO bmi = new BITMAPINFO();
		bmi.bmiHeader.biWidth = width;
		bmi.bmiHeader.biHeight = -height;
		bmi.bmiHeader.biPlanes = 1;
		bmi.bmiHeader.biBitCount = 32;
		bmi.bmiHeader.biCompression = WinGDI.BI_RGB;

		final Memory buffer = new Memory(width * height * 4);
		GDI32.INSTANCE.GetDIBits(hdcWindow, hBitmap, 0, height, buffer, bmi, WinGDI.DIB_RGB_COLORS);

		final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		image.setRGB(0, 0, width, height, buffer.getIntArray(0, width * height), 0, width);

		GDI32.INSTANCE.DeleteObject(hBitmap);
		User32.INSTANCE.ReleaseDC(handle, hdcWindow);

		return image;

	}

	/**
	 * Tiles all windows that statisfy the given regex.
	 *
	 * @param regex Typical regex, that gets applied to every windows name.
	 */
	public void windowTile(final String regex) throws InterruptedException, IOException {
		final List<HWND> handles = windowGetHandles(regex.toLowerCase());
		windows = null;
		final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		final double width = screen.getWidth();
		final double height = screen.getHeight();
		final double[] squares = new double[] { 0, 1, 4, 9, 16, 25, 36, 49, 64, 81, 100 };
		int tiles = 0;
		while (squares[tiles] < handles.size()) {
			tiles++;
		}
		final int tileW = (int) (width / tiles);
		final int tileH = (int) (height / tiles);
		for (int i = 0; i < tiles; i++) {
			for (int j = 0; j < tiles; j++) {
				final HWND h = handles.get(i * tiles + j);
				User32.INSTANCE.MoveWindow(h, 0 + i * tileW, 0 + j * tileH, tileW, tileH, true);
			}
		}
	}

	/*
	 * http://www.mathblog.dk/files/euler/Problem60.cs
	 */
	private boolean witness(final int a, final int n) {
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

	/**
	 * Uses the youtube-dl Library to download YT videos.
	 *
	 * @param url Youtube Link.
	 */
	public void youtubeDownload(final String url) throws IOException {
		String format = "";
		int mp = 4; // Default mp4.
		if (getInput(null, "Do you want audio or video? [audio/video]",
				Arrays.asList(new String[] { "audio", "video" }), 2, 0, false, null, 0).equals("audio")) {
			format = "-x --audio-format mp3";
			mp = 3;
		} else {
			format = "-f 'bestvideo[ext=mp4] ";
		}
		final String name = getInput(null, "What should be the name of the file?", null, 0, 0, false, null, 0);
		if (debug) {
			System.out.println("cd C:/Users/Naix/Desktop/ && youtube-dl " + url + " " + format + " -o " + "\"" + name
					+ ".mp" + mp + "\"");
		}
		executeCmd(true, new String[] { "cd C:/Users/Naix/Desktop/ && youtube-dl " + url + " " + format + " -o " + "\""
				+ name + ".mp" + mp + "\"" });
	}

}
