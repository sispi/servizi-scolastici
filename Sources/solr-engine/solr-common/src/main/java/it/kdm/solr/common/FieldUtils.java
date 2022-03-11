package it.kdm.solr.common;

import com.google.common.base.Strings;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
//import org.apache.solr.search.SearchUtils;
import org.noggit.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.CharacterIterator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.util.*;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
/**
 * Created by Paolo_2 on 07/07/15.
 */
public class FieldUtils {

    public static final long DAYMS = 24*60*60*1000L;

    public static String encrypt(String strToEncrypt, String secret)
    {
        try
        {
            byte[] key = secret.getBytes();

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            final String encryptedString = Base64.encodeBase64String(cipher.doFinal(strToEncrypt.getBytes()));
            return encryptedString;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;

    }

    public static String decrypt(String strToDecrypt, String secret)
    {
        try
        {
            byte[] key = secret.getBytes();

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            final String decryptedString = new String(cipher.doFinal(Base64.decodeBase64(strToDecrypt)));
            return decryptedString;
        }
        catch (Exception e)
        {
            e.printStackTrace();

        }
        return null;
    }

    public static final String VIRTUALPATH = "VIRTUALPATH";
    private transient static Logger log = LoggerFactory.getLogger(FieldUtils.class);

    public static final String fsChars = "\\/:*?\"<>|";
    public static final String idChars = "\\/:*?\"<>!.@";
    public static final char encIdChar = '!';
    public static final char encFsChar = '%';
    public static final char encPerc = '%';

    private static final String isoDateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static final String isoDatePlaceHolder = "0000-00-00T00:00:00.000Z";

    private static final SimpleDateFormat ddf = new SimpleDateFormat(isoDateFormat);
    private static final SimpleDateFormat tdf = new SimpleDateFormat(isoDateFormat);

    static{
        ddf.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public static String formatDate(Date date)
    {
        //Date d2 = new Date( date.getTime() - date.getTimezoneOffset() );
        return ddf.format(date);
    }

    public static File resolvePath( String path )
    {
        path = path.replaceAll("\\\\+","/");

        if (path.startsWith("/"))
        {
            return new File(path);
        }
        else
        {
            return new File( System.getProperty("jetty.home"),path);
        }
    }

    public static Date parseDate(String date)
    {
        try {
            return ddf.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static long getTime( Date dt )
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        int milli = cal.get(Calendar.MILLISECOND);

        return (second + minute*60 + hour*60*60)*1000 + milli;
    }

    public static long parseTime(String time)
    {
        int idx = time.indexOf("T");

        if (idx>=0)
            time = time.substring(idx+1);

        String date = isoDatePlaceHolder.substring(0,11) + time;

        if (date.length()<isoDatePlaceHolder.length())
        {
            if (date.charAt(date.length()-1) == 'Z')
                date = date.substring(0,date.length()-1);

            date += isoDatePlaceHolder.substring(date.length());
        }

        try {
            Date dt = tdf.parse(date);

            return getTime(dt) % DAYMS;

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /*public static boolean checkInterval( long seconds, String interval )
    {
        boolean result = checkInterval(seconds, parseInterval(interval));

        Date date = new Date( seconds * 1000 );

        log.info("checkInterval date:{} tick:{} period:{} result:{}" , formatDate(date), seconds, interval , result);

        return result;
    }

    public static boolean checkInterval( long seconds, DateInterval dateInterval)
    {
        long start = dateInterval.start;
        long end = dateInterval.end;
        long period = dateInterval.period;

        if (start==end)
            return seconds==start;

        long delay = seconds-start;

        if ( end>start && (seconds<start || seconds>end) )
            return false;

        if ( start>end && seconds>start && seconds<end )
            return false;

        return (delay % period == 0);
    }*/

    public static DateInterval parseInterval( String interval )
    {
        String[] parts = interval.split("/");

        long start = 0;
        long end = DAYMS;
        long period = 0;
        long repetitions = 0;

        for( int i=0; i<parts.length; i++ )
        {
            String part = parts[i];

            if (part.startsWith("P"))
            {
                period = parseTime(part.substring(1));
            }
            else if (part.startsWith("R"))
            {
                repetitions = Long.parseLong(part.substring(1));
            }
            else if (i>0 && !parts[i-1].startsWith("P") && !parts[i-1].startsWith("R") )
            {
                end = parseTime(part);
            }
            else
            {
                start = parseTime(part);
            }
        }

        if (period==0 || period>DAYMS)
            period = DAYMS;

        if (repetitions>0)
            end = (start + period * repetitions) ;

        if (end==0 || end>DAYMS)
            end = DAYMS;

        /*if ( period!=null && repetitions!=null)
        {
            if (end!=null)
                start = end - period * repetitions;
        }
        else
        {
            if (end==null)
                end = 24*60*60*1000L;

            if (period==null)
                period = 0L;

            if (repetitions==null)
                repetitions = 0L;

            if (period>0)
                repetitions = (end - start)/period;
            else if (repetitions>0)
                period = (end - start)/repetitions;

            if (start>end)
                repetitions--;
        }

        end = start + period * repetitions ;*/

        return new DateInterval(start,end,period);
    }

    public static String encodeId(String token)
    {
        return encode(token,0,true);
    }

    public static String encodeFs(String token)
    {
        return encode(token,0,false);
    }

    public static String encode(String token, int hash, boolean isId)
	{
		if (token==null)
			return null;

		String specialChars;
		if (isId)
			specialChars = idChars + encPerc;
		else
			specialChars = fsChars + encPerc;

		StringBuilder s = new StringBuilder(token.length());

		CharacterIterator it = new StringCharacterIterator(token);
		for (char ch = it.first(); ch != CharacterIterator.DONE; ch = it.next()) {
			if (specialChars.indexOf(ch)!=-1)
				s.append("" + encPerc).append(Integer.toHexString((int) ch));
			else
				s.append(ch);
		}

		token = s.toString();

		if (hash>0)
		{
			String hex = "~"+String.format("%8s", Integer.toHexString(hash) ).replace(' ', '0');
			int idx = token.lastIndexOf('.');
			if (idx==-1)
				token += hex;
			else
				token = token.substring(0,idx) + hex + token.substring(idx);
		}

		return token;
	}

    public static String decode(String token)
    {
        if (token==null)
            return null;

        token = token.replaceAll( "~[a-f0-9]{8}($|\\.[^\\.]+$)" , "$1" );

        StringBuilder s = new StringBuilder(token.length());

        CharacterIterator it = new StringCharacterIterator(token);
        for (char ch = it.first(); ch != CharacterIterator.DONE; ch = it.next()) {
            if (ch== encPerc)
            {
                char c1 = it.next();
                char c2 = it.next();
                String hex = ""+c1+c2;
                int cint = Integer.parseInt(hex,16);

                s.append( (char) cint );
            }
            else
                s.append(ch);
        }

        token = s.toString();
        return token;
        //return str.replaceAll("%7","/");
    }

    public static Properties getVersionProperties(String versionString)
    {
        Properties props = new Properties();

        if (versionString==null)
            return props;

        //String prov = null;
        //String uuid = null;

        int idx = versionString.indexOf("://");

        if (idx!=-1)
        {
            String prov = versionString.substring(0,idx);
            versionString = versionString.substring(idx+3);

            props.setProperty("provider",prov);

            /*int idx1 = doVersionString.indexOf("?");

            if (idx1!=-1)
            {
                uuid = doVersionString.substring(0,idx1);
                doVersionString = doVersionString.substring(idx1+1);
            }
            else
            {
                uuid = doVersionString;
                doVersionString = null;
            }*/
        }

        //props.setProperty("provider_id" , uuid);

        if (versionString != null)
        {
            try
            {
                List<NameValuePair> pairs = URLEncodedUtils.parse(new java.net.URI("http://dummy?" + versionString), StandardCharsets.UTF_8.name());

                for( NameValuePair pair : pairs )
                    props.setProperty( pair.getName() , pair.getValue() );
            }
            catch(Exception e)
            {
                throw new RuntimeException(e);
            }
        }

        //String id = props.getProperty("id");
        //String version = props.getProperty("version");

        //if (id==null || version==null)
        //    throw new SolrException(SolrException.ErrorCode.CONFLICT, "invalid provider response" );

        //int idx1 = doVersionString.indexOf("?");
        //String prov = doVersionString.substring(0,idx);
        //String uuid = doVersionString.substring(idx+3,idx1);

        //if (idx!=-1)
        //	version_id = doVersionString.substring(idx1+1);


        //prop.setProperty("version_id" , version_id);

        return props;
    }

    public static String doVersionString(String id, String version, String path)
    {
        List<NameValuePair> pairs = new ArrayList<>();

        path = path.replaceAll( "[\\\\\\:\\*\\?\\\"\\<\\>\\|]" , "_" );

        pairs.add ( new BasicNameValuePair( "id" , id ) );
        pairs.add ( new BasicNameValuePair( "version" , version ) );
        pairs.add ( new BasicNameValuePair( "path" , path ) );

        String versionString = URLEncodedUtils.format( pairs, StandardCharsets.UTF_8.name() );

        return versionString;
    }

    public static String doVersionString(Properties props)
    {
        List<NameValuePair> pairs = new ArrayList<>();

        for( Object key : props.keySet() )
        {
            Object value = props.get(key);

            if (value instanceof Date)
                value = FieldUtils.formatDate((Date) value);

            if (value!=null && !"".equals(value))
                pairs.add ( new BasicNameValuePair(key.toString() , value.toString() ) ) ;
        }

        String versionString = URLEncodedUtils.format( pairs, StandardCharsets.UTF_8.name() );

        return versionString;
    }

    public static String getMetaDataField(int idx)
    {
        return String.format("version_%04d",idx);
    }

    public static String encodeUrlPart(String name, String value)
    {
        List<NameValuePair> pairs = new ArrayList<>();

        pairs.add ( new BasicNameValuePair( name , value ) );

        String versionString = URLEncodedUtils.format( pairs, StandardCharsets.UTF_8.name() );

        return versionString;
    }

    public static class DateInterval
    {
        public final long start;
        public final long end;
        public final long period;

        DateInterval(long start, long end, long period)
        {
            this.start = start;
            this.end = end;
            this.period = period;
        }
    }

    public static String buildVirtualPATH(SolrQueryRequest req, String name)
    {
        String virtualpath = req.getParams().get(VIRTUALPATH);

        if ( Strings.isNullOrEmpty(virtualpath) )
            return null;

        if (virtualpath.endsWith("/*"))
            virtualpath = virtualpath.replaceFirst("\\*$","")+name;

        return virtualpath;
    }

    public static boolean parseBool( Object bool , boolean def  )
    {
        if (bool==null)
            return def;

        if (bool.equals(false) || bool.equals(true))
            return (boolean) bool;

        if (bool.toString().toUpperCase().equals("TRUE") || bool.toString().toUpperCase().equals("T") )
            return true;
        if (bool.toString().toUpperCase().equals("FALSE") || bool.toString().toUpperCase().equals("F") )
            return false;

        return def;
    }

    /*public static String writeJSON( NamedList<Object> nl )
    {
        return SearchUtils.writeJSON(nl);
    }*/

    final private static String OBJECT_END = UUID.randomUUID().toString();
    final private static String ARRAY_END = UUID.randomUUID().toString();

    public static NamedList<Object> parseJSON( String JSON ) throws IOException
    {
        JSONParser parser = new JSONParser(JSON);
        return (NamedList<Object>) parseJSON( parser );
    }

    public static Collection<Object> parseJSONList( String JSON ) throws IOException
    {
        JSONParser parser = new JSONParser(JSON);
        return (Collection<Object>) parseJSON( parser );
    }

    private static Object parseJSON( JSONParser parser ) throws IOException
    {
        int ev = parser.nextEvent();

        switch( ev )
        {
            case JSONParser.OBJECT_START:

                NamedList<Object> namedList = new NamedList<>();

                while( true ) {

                    Object name = parseJSON(parser);

                    if (name.equals(OBJECT_END))
                        return namedList;

                    if (!parser.wasKey())
                        throw new IOException("key expected");

                    Object val = parseJSON(parser);

                    namedList.add(name.toString(),val);
                }

            case JSONParser.ARRAY_START:

                Collection<Object> array = new ArrayList<>();

                while( true ) {

                    Object val = parseJSON(parser);

                    if (val.equals(ARRAY_END))
                        return array;

                    array.add(val);
                }

            case JSONParser.STRING:
                return parser.getString();

            case JSONParser.LONG:
                return parser.getLong();

            case JSONParser.NUMBER:
                return parser.getDouble();

            case JSONParser.BOOLEAN:
                return parser.getBoolean();

            case JSONParser.NULL:
                return null;

            case JSONParser.BIGNUMBER:
                throw new IOException("number too big");

            case JSONParser.OBJECT_END:
                return OBJECT_END;

            case JSONParser.ARRAY_END:
                return ARRAY_END;

            //case JSONParser.EOF:

            default:
                 throw new IOException("invalid token here");
        }
    }

    public static boolean diffCollections(Collection<Object> c1, Collection<Object> c2)
    {
        boolean changed = false;

        if (c1==null && c2==null)
            return false;

        if (c1==null || c2==null)
            return true;

        if (c1.size() == c2.size())
        {
            log.trace("comparison old:{} new:{}",c2,c1);

            final Set<Object> s1 = new HashSet<>(c1);
            final Set<Object> s2 = new HashSet<>(c2);

            changed = !s1.equals(s2);

            /*Iterator<Object> nv = c1.iterator();
            Iterator<Object> ov = c2.iterator();

            while (ov.hasNext())
            {
                Object nval = nv.next();
                Object oval = ov.next();

                if (nval==null && oval==null)
                    continue;
                else if (nval==null || oval==null)
                    changed = true;
                else
                    changed = !nval.toString().equals(oval.toString());

                if (changed)
                    break;
            }*/
        }
        else
        {
            changed = true;
        }

        return changed;
    }






}
