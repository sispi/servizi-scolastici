package it.kdm.solr.components.common;

import it.kdm.solr.common.FieldUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.util.Date;

import static org.junit.Assert.assertEquals;


/**
 * Created by Paolo_2 on 09/07/15.
 */
public class UtilsTest {

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(UtilsTest.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        if (result.wasSuccessful()) {
            System.out.println("All Tests finished successfully...");
        }
    }

    @BeforeClass
    public static void initTest()
    {

    }

    @Test
    public void testParseTime() throws Exception {

        String ts1 = "00:00:00";
        String ts2 = "T00:01:00";
        String ts3 = "T01";
        String ts4 = "23:00:12T01";

        assertEquals(FieldUtils.parseTime(ts1), 0);
        assertEquals(FieldUtils.parseTime(ts2),60*1000);
        assertEquals(FieldUtils.parseTime(ts3), 60*60*1000);
        assertEquals(FieldUtils.parseTime(ts4), 60*60*1000);

    }

    /*@Test
    public void testParseInterval() throws Exception
    {
        String ts0 = "PT00:00:01";

        FieldUtils.DateInterval interval = FieldUtils.parseInterval(ts0);

        assertEquals(interval.period, 1000);
        assertEquals(interval.start , 0);
        assertEquals(interval.end, 24*60*60*1000);

        String ts1 = "PT00:01:00/T02:00:00";

        interval = FieldUtils.parseInterval(ts1);

        assertEquals(interval.period, 60*1000);
        assertEquals(interval.start , 0);
        assertEquals(interval.end, 2*60*60*1000);

        String ts2 = "T02:00:00/PT00:01:00";

        interval = FieldUtils.parseInterval(ts2);

        assertEquals(interval.period, 60*1000);
        assertEquals(interval.start , 2*60*60*1000);
        assertEquals(interval.end, 24*60*60*1000);

        String ts3 = "T03:00:00/PT00:01:00/T22:00:30";

        interval = FieldUtils.parseInterval(ts3);

        assertEquals(interval.period, 60*1000);
        assertEquals(interval.start , 3*60*60*1000);
        assertEquals(interval.end, 22*60*60*1000);

        ts3 = "T22/PT00:01:00/T03:00:30";

        interval = FieldUtils.parseInterval(ts3);

        assertEquals(interval.period, 60*1000);
        assertEquals(interval.start , 22*60*60*1000);
        assertEquals(interval.end, 3*60*60*1000);

        String ts4 = "R10/PT00:01:00/T03:00:30";

        interval = FieldUtils.parseInterval(ts4);

        assertEquals(interval.period, 60*1000);
        assertEquals(interval.start , 3*60*60*1000+30*1000-10*60*1000);
        assertEquals(interval.end, 3*60*60*1000+30*1000);

        String ts5 = "T03:00:30";

        interval = FieldUtils.parseInterval(ts5);

        assertEquals(interval.period, 0);
        assertEquals(interval.start , 3*60*60*1000+30*1000);
        assertEquals(interval.end, 3*60*60*1000+30*1000);

        String ts6 = "03:00:30/P00:01/R10";

        interval = FieldUtils.parseInterval(ts6);

        assertEquals(interval.period, 60*1000);
        assertEquals(interval.start , 3*60*60*1000+30*1000);
        assertEquals(interval.end, 3*60*60*1000+30*1000+10*60*1000);

        String ts7 = "03:30:00/R10/04:00:00";

        interval = FieldUtils.parseInterval(ts7);

        assertEquals(interval.period, 3*60*1000);
        assertEquals(interval.start , 3*60*60*1000+30*60*1000);
        assertEquals(interval.end, 3*60*60*1000+30*60*1000 + 3*60*1000*10);

    }*/

    @Test
    public void testParseDate() throws Exception
    {
        Date now = new Date();

        String ts1 = FieldUtils.formatDate(now);

        assertEquals(FieldUtils.parseDate(ts1), now);
    }

    @AfterClass
    public static void finishTest()
    {

    }
}
