package it.kdm.utils;

import org.apache.commons.collections.ListUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by microchip on 19/09/17.
 */
public class DocerUtils {
    public static List getFascicoliDelta(List<String> newList, List<String> oldList) {

        Set<String> set = new HashSet<String>();
        set.addAll(ListUtils.subtract(newList, oldList));
        set.addAll(ListUtils.subtract(oldList,newList));

        return new ArrayList(set);
    }
}
