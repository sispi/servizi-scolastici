package it.emilia_romagna.fonteraccoglitore.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hamcrest.Matcher;

public final class MatcherEvaluator {

	public static <T> boolean the(T element, Matcher<?> matcher) {
		return matcher!= null && matcher.matches(element);
	}

	public static <T> List<T> select(Iterable<T> collection, Matcher<?> matcher) {
		List<T> result = new ArrayList<T>();
		if (collection != null && matcher != null) {
			for (T element : collection) {
				if (matcher.matches(element)) {
					result.add(element);
				}
			}
		}
		return result;
	}

	public static <K, E> Map<K, E> select(Map<K, E> map, Matcher<?> matcher) {
		Map<K, E> result = new HashMap<K, E>();

		if (map != null) {
			List<K> keys = select(map.keySet(), matcher);
			for (K k : keys) {
				result.put(k, map.get(k));
			}
		}

		return result;
	}

}
