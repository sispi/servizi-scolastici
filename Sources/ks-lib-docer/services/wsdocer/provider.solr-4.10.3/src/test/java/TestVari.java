import org.apache.commons.collections.ListUtils;
import org.junit.Test;

import java.util.*;

/**
 * Created by microchip on 19/09/17.
 */

public class TestVari {
    @Test
    public void testGetFascicoliDelta() {
        List<String> one = new ArrayList<String>();
        one.add("1.0.0/2015/3");
        one.add("1.1.0/2016/13");
        one.add("2.0.0/2015/34");

        List<String> two = new ArrayList<String>();
        two.add("1.1.0/2016/13");
        two.add("2.0.0/2015/34");
        two.add("1.0.0/2015/3");

        List resultList = getFascicoliDelta(one,two);

        one = new ArrayList<String>();
        one.add("1.0.0/2015/3");
        one.add("1.1.0/2016/13");
        one.add("2.0.0/2015/34");

        two = new ArrayList<String>();
        two.add("1.0.0/2015/34");
        two.add("1.1.0/2016/130");
        two.add("2.0.0/2015/34");

        resultList = getFascicoliDelta(one,two);

    }

    private List getFascicoliDelta(List<String> newList, List<String> oldList) {

        Set<String> set = new HashSet<String>();
        set.addAll(ListUtils.subtract(newList,oldList));
        set.addAll(ListUtils.subtract(oldList,newList));

        return new ArrayList(set);
    }
}
