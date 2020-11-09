package de.oesing.all;

public class OesingLibArray {

	/**
	 * @param arr
	 * @return
	 */
	public static Object[][] copyArray(final int[][] arr) {
		final Object[][] copy = new Object[arr.length][arr[0].length];
		for(int i=0;i<arr.length;i++){
			for(int j=0;j<arr[0].length;j++){
				copy[i][j] = arr[i][j];
			}
		}
		return copy;
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
}
