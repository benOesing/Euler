package de.oesing.all;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class OesingLFUCache<K, T> {
	class CacheEntry {
		public T data;
		public int frequency;
		public long timestamp;

		private CacheEntry(final T data) {
			this.data = data;
		}

		private CacheEntry(final T data, final int frequency) {
			this.data = data;
			this.frequency = frequency;
			this.timestamp = System.currentTimeMillis();
		}

		private CacheEntry(final T data, final int frequency, final long timestamp) {
			this.data = data;
			this.frequency = frequency;
			this.timestamp = timestamp;
		}
	}

	private final int maxElements;

	private final LinkedHashMap<K, CacheEntry> map = new LinkedHashMap<>();

	public OesingLFUCache(final int maxElements) {
		this.maxElements = maxElements;
	}

	public void addCacheEntry(final K key, final T data) {
		final CacheEntry existingEntry = map.get(key);
		if (existingEntry != null) {
			addCacheEntry(key, data, existingEntry.frequency++, existingEntry.timestamp);
		} else {
			addCacheEntry(key, data, 0, System.currentTimeMillis());
		}
	}

	public void addCacheEntry(final K key, final T data, final int frequency, final long timestamp) {
		final CacheEntry existingEntry = map.get(key);
		if (existingEntry != null) {
			existingEntry.data = data;
			existingEntry.frequency = frequency;
			existingEntry.timestamp = timestamp;
		}
		if (map.size() >= maxElements) {
			final K keyToRemove = getLFUEntry();
			map.remove(map.get(keyToRemove));
		}
		final CacheEntry entry = new CacheEntry(data, frequency, timestamp);
		map.put(key, entry);
	}

	public T getCacheEntry(final K key) {
		final CacheEntry entry = map.get(key);
		if (entry != null) {
			entry.frequency++;
			return entry.data;
		}
		return null;
	}

	private K getLFUEntry() {
		// TODO: O(n) -> O(log n)
		K key = null;
		long smallestTimeStamp = Integer.MAX_VALUE;
		int smallestFrequency = Integer.MAX_VALUE;
		for (final Entry<K, OesingLFUCache<K, T>.CacheEntry> entry : map.entrySet()) {
			final int frequency = entry.getValue().frequency;
			if (frequency < smallestFrequency) {
				smallestFrequency = frequency;
				key = entry.getKey();
			} else if (frequency == smallestFrequency) {
				final long timeStamp = entry.getValue().timestamp;
				if (timeStamp < smallestTimeStamp) {
					smallestTimeStamp = timeStamp;
					key = entry.getKey();
				}
			}
		}
		return key;
	}
}
