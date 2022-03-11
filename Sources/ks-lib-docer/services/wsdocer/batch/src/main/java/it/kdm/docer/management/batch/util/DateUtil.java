package it.kdm.docer.management.batch.util;

import it.kdm.docer.sdk.classes.KeyValuePair;
import it.kdm.docer.sdk.interfaces.ISearchItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vaio
 * Date: 30/10/15
 * Time: 17.43
 * To change this template use File | Settings | File Templates.
 */
public class DateUtil {

    // Formato 2015-10-10T23:15:32.000+0200Z
    public static String toSolrDate(Date date) {

        if (date == null)
            return "*";

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sssZ");
        String dateStr = dateFormat.format(cal.getTime());

        return dateStr;
    }

    // Formato [2015-10-10T23:15:32Z TO 2015-10-20T35:0059:59Z
    public static String toSolrInterval(Date date1, Date date2) {

        return "[" + toSolrDate(date1) + " TO " + toSolrDate(date2) + "]";
    }


    public static Date fromSolrDate(String solrDate) {
        try {
            return org.apache.solr.common.util.DateUtil.parseDate(solrDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Incrementa di un secondo una data in formato Solr
    public static Date incrementDate(Date oldDate, int type, int value) {
        if (oldDate != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(oldDate);
            cal.add(type, value);
            return cal.getTime();
        }

        return null;
    }

}
