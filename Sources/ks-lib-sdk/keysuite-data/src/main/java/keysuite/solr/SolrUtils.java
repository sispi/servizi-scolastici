package keysuite.solr;

import it.kdm.orchestratore.session.Session;
import it.kdm.orchestratore.session.UserInfo;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.MultiMapSolrParams;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.*;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.*;

/**
 * Created by Paolo_2 on 28/04/17.
 */
public class SolrUtils {

    private static final Charset CHARSET_US_ASCII = Charset.forName("US-ASCII");

    public static final String INPUT_ENCODING_KEY = "ie";
    private static final byte[] INPUT_ENCODING_BYTES = INPUT_ENCODING_KEY.getBytes(CHARSET_US_ASCII);

    public static String encode(String token, String chars){
        if (token==null)
            return null;
        StringBuilder s = new StringBuilder(token.length());
        CharacterIterator it = new StringCharacterIterator(token);
        for (char ch = it.first(); ch != CharacterIterator.DONE; ch = it.next()) {
            if (chars.indexOf(ch)!=-1)
                s.append("%").append(Integer.toHexString((int) ch));
            else
                s.append(ch);
        }

        token = s.toString();
        return token;
    }

    public static final String specialChars = "\\/:*?\"<>!.@%";

    public static String getSolrId(String targetId,String targetType){

        UserInfo ui = Session.getUserInfoNoExc();
        String loc = encode(Session.getSede(),specialChars);
        String codEnte = encode(ui.getCodEnte(),specialChars);
        String codAoo = encode(ui.getCodAoo(),specialChars);

        if ("fascicolo".equals(targetType) && !targetId.contains("|")){
            targetId = targetId.replaceFirst("/","|").replaceFirst("/","|");
        }
        targetId = encode(targetId,specialChars);

        switch(targetType){
            case "ente":
                targetId = String.format("%s.%s!@ente",loc,targetId);
                break;
            case "aoo":
                targetId = String.format("%s.%s!%s!@aoo",loc,codEnte,targetId);
                break;
            case "group":
            case "user":
                targetId = String.format("%s@%s",targetId,targetType);
                break;
            case "fascicolo":
                targetId = String.format("%s.%s!%s!%s@%s",loc,codEnte,codAoo,targetId,targetType);
                break;
            default:
                targetId = String.format("%s.%s!%s!%s@%s",loc,codEnte,codAoo,targetId,targetType);
                break;
        }
        return targetId;
    }

    /**
     * Given a url-encoded query string (UTF-8), map it into solr params
     */
    public static MultiMapSolrParams parseQueryString(String queryString) {
        Map<String,String[]> map = new HashMap<>();
        parseQueryString(queryString, map);
        return new MultiMapSolrParams(map);
    }

    /**
     * Given a url-encoded query string (UTF-8), map it into the given map
     * @param queryString as given from URL
     * @param map place all parameters in this map
     */
    static void parseQueryString(final String queryString, final Map<String,String[]> map) {
        if (queryString != null && queryString.length() > 0) {
            try {
                final int len = queryString.length();
                // this input stream emulates to get the raw bytes from the URL as passed to servlet container, it disallows any byte > 127 and enforces to %-escape them:
                final InputStream in = new InputStream() {
                    int pos = 0;
                    @Override
                    public int read() {
                        if (pos < len) {
                            final char ch = queryString.charAt(pos);
                            if (ch > 127) {
                                throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, "URLDecoder: The query string contains a not-%-escaped byte > 127 at position " + pos);
                            }
                            pos++;
                            return ch;
                        } else {
                            return -1;
                        }
                    }
                };
                parseFormDataContent(in, Long.MAX_VALUE, StandardCharsets.UTF_8, map, true);
            } catch (IOException ioe) {
                throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, ioe);
            }
        }
    }

    /**
     * Given a url-encoded form from POST content (as InputStream), map it into the given map.
     * The given InputStream should be buffered!
     * @param postContent to be parsed
     * @param charset to be used to decode resulting bytes after %-decoding
     * @param map place all parameters in this map
     */
    @SuppressWarnings({"fallthrough", "resource"})
    static long parseFormDataContent(final InputStream postContent, final long maxLen, Charset charset, final Map<String,String[]> map, boolean supportCharsetParam) throws IOException {
        CharsetDecoder charsetDecoder = supportCharsetParam ? null : getCharsetDecoder(charset);
        final LinkedList<Object> buffer = supportCharsetParam ? new LinkedList<>() : null;
        long len = 0L, keyPos = 0L, valuePos = 0L;
        final ByteArrayOutputStream keyStream = new ByteArrayOutputStream(),
                valueStream = new ByteArrayOutputStream();
        ByteArrayOutputStream currentStream = keyStream;
        for(;;) {
            int b = postContent.read();
            switch (b) {
                case -1: // end of stream
                case '&': // separator
                    if (keyStream.size() > 0) {
                        final byte[] keyBytes = keyStream.toByteArray(), valueBytes = valueStream.toByteArray();
                        if (Arrays.equals(keyBytes, INPUT_ENCODING_BYTES)) {
                            // we found a charset declaration in the raw bytes
                            if (charsetDecoder != null) {
                                throw new SolrException(SolrException.ErrorCode.BAD_REQUEST,
                                        supportCharsetParam ? (
                                                "Query string invalid: duplicate '"+
                                                        INPUT_ENCODING_KEY + "' (input encoding) key."
                                        ) : (
                                                "Key '" + INPUT_ENCODING_KEY + "' (input encoding) cannot "+
                                                        "be used in POSTed application/x-www-form-urlencoded form data. "+
                                                        "To set the input encoding of POSTed form data, use the "+
                                                        "'Content-Type' header and provide a charset!"
                                        )
                                );
                            }
                            // decode the charset from raw bytes
                            charset = Charset.forName(decodeChars(valueBytes, keyPos, getCharsetDecoder(CHARSET_US_ASCII)));
                            charsetDecoder = getCharsetDecoder(charset);
                            // finally decode all buffered tokens
                            decodeBuffer(buffer, map, charsetDecoder);
                        } else if (charsetDecoder == null) {
                            // we have no charset decoder until now, buffer the keys / values for later processing:
                            buffer.add(keyBytes);
                            buffer.add(Long.valueOf(keyPos));
                            buffer.add(valueBytes);
                            buffer.add(Long.valueOf(valuePos));
                        } else {
                            // we already have a charsetDecoder, so we can directly decode without buffering:
                            final String key = decodeChars(keyBytes, keyPos, charsetDecoder),
                                    value = decodeChars(valueBytes, valuePos, charsetDecoder);
                            MultiMapSolrParams.addParam(key.trim(), value, map);
                        }
                    } else if (valueStream.size() > 0) {
                        throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, "application/x-www-form-urlencoded invalid: missing key");
                    }
                    keyStream.reset();
                    valueStream.reset();
                    keyPos = valuePos = len + 1;
                    currentStream = keyStream;
                    break;
                case '+': // space replacement
                    currentStream.write(' ');
                    break;
                case '%': // escape
                    final int upper = digit16(b = postContent.read());
                    len++;
                    final int lower = digit16(b = postContent.read());
                    len++;
                    currentStream.write(((upper << 4) + lower));
                    break;
                case '=': // kv separator
                    if (currentStream == keyStream) {
                        valuePos = len + 1;
                        currentStream = valueStream;
                        break;
                    }
                    // fall-through
                default:
                    currentStream.write(b);
            }
            if (b == -1) {
                break;
            }
            len++;
            if (len > maxLen) {
                throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, "application/x-www-form-urlencoded content exceeds upload limit of " + (maxLen/1024L) + " KB");
            }
        }
        // if we have not seen a charset declaration, decode the buffer now using the default one (UTF-8 or given via Content-Type):
        if (buffer != null && !buffer.isEmpty()) {
            assert charsetDecoder == null;
            decodeBuffer(buffer, map, getCharsetDecoder(charset));
        }
        return len;
    }

    private static CharsetDecoder getCharsetDecoder(Charset charset) {
        return charset.newDecoder()
                .onMalformedInput(CodingErrorAction.REPORT)
                .onUnmappableCharacter(CodingErrorAction.REPORT);
    }

    private static String decodeChars(byte[] bytes, long position, CharsetDecoder charsetDecoder) {
        try {
            return charsetDecoder.decode(ByteBuffer.wrap(bytes)).toString();
        } catch (CharacterCodingException cce) {
            throw new SolrException(SolrException.ErrorCode.BAD_REQUEST,
                    "URLDecoder: Invalid character encoding detected after position " + position +
                            " of query string / form data (while parsing as " + charsetDecoder.charset().name() + ")"
            );
        }
    }

    private static void decodeBuffer(final LinkedList<Object> input, final Map<String,String[]> map, CharsetDecoder charsetDecoder) {
        for (final Iterator<Object> it = input.iterator(); it.hasNext(); ) {
            final byte[] keyBytes = (byte[]) it.next();
            it.remove();
            final Long keyPos = (Long) it.next();
            it.remove();
            final byte[] valueBytes = (byte[]) it.next();
            it.remove();
            final Long valuePos = (Long) it.next();
            it.remove();
            MultiMapSolrParams.addParam(decodeChars(keyBytes, keyPos.longValue(), charsetDecoder).trim(),
                    decodeChars(valueBytes, valuePos.longValue(), charsetDecoder), map);
        }
    }

    private static int digit16(int b) {
        if (b == -1) {
            throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, "URLDecoder: Incomplete trailing escape (%) pattern");
        }
        if (b >= '0' && b <= '9') {
            return b - '0';
        }
        if (b >= 'A' && b <= 'F') {
            return b - ('A' - 10);
        }
        if (b >= 'a' && b <= 'f') {
            return b - ('a' - 10);
        }
        throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, "URLDecoder: Invalid digit (" + ((char) b) + ") in escape (%) pattern");
    }
}
