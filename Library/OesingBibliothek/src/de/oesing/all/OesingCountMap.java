package de.oesing.all;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The intention of this class is simply put, counting the occurences of an
 * object.
 * @see {@link HashMap}
 */
public class OesingCountMap<K> {

	private final HashMap<K,Integer> map = new HashMap<>();

	/**
	 * @see {@link HashMap#clear()}
	 */
	public void clear(){
		map.clear();
	}

	/**
	 * @see {@link HashMap#containsKey(Object)}
	 */
	public boolean containsKey(final K key){
		return map.containsKey(key);
	}

	/**
	 * Decrements the count for the given key.
	 *
	 * @param key
	 *            The key of any type that will get counted down.
	 * @return Returns the new count for the given key or -1 if the object doesnt exist.
	 */
	public int countDown(final K key) {
		Integer x = map.remove(key);
		if(x != null){
			x--;
			map.put(key, x);
			return x;
		} else{
			return -1;
		}
	}

	/**
	 * Increments the count for the given key or inserts the key to be counted.
	 *
	 * @param key
	 *            The key of any type that will get counted up.
	 * @return Returns the new count for the given key.
	 */
	public int countUp(final K key) {
		Integer x = map.remove(key);
		if (x != null) {
			x++;
			map.put(key, x);
			return x;
		} else {
			map.put(key, 1);
			return 1;
		}
	}

	/**
	 * @see {@link HashMap#get(Object)}
	 */
	public int get(final K key){
		return map.get(key);
	}

	/**
	 * @return Returns a list of entries sorted by value.
	 */
	public List<Map.Entry<K, Integer>> getSortedListByValue() {
		final List<Map.Entry<K, Integer>> list = new ArrayList<>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, Integer>>() {
			@Override
			public int compare(final Map.Entry<K, Integer> o1, final Map.Entry<K, Integer> o2) {
				int i1, i2;
				i1 = o1.getValue();
				i2 = o2.getValue();
				return -Integer.compare(i1, i2);
			}
		});
		return list;
	}
	/**
	 * @see {@link HashMap#isEmpty}
	 */
	public boolean isEmpty(){
		return map.isEmpty();
	}
	/**
	 * @see {@link HashMap#keySet()}
	 */
	public Set<K> keySet(){
		return map.keySet();
	}

	/**
	 * @see {@link HashMap#put(Object, Object)}
	 */
	public void put(final K key, final Integer value){
		map.put(key, value);
	}

	/**
	 * @see {@link HashMap#remove(Object)}
	 */
	public int remove(final K key){
		return map.remove(key);
	}

	/**
	 * @see {@link HashMap#size()}
	 */
	public int size(){
		return map.size();
	}

	/**
	 * @see {@link HashMap#values()}
	 */
	public Collection<Integer> values(){
		return map.values();
	}
}