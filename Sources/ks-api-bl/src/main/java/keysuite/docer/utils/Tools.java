package keysuite.docer.utils;

import com.google.common.base.Strings;
import it.kdm.doctoolkit.services.ToolkitConnector;
import it.kdm.doctoolkit.utils.Utils;
import it.kdm.doctoolkit.zookeeper.ApplicationProperties;
import it.kdm.orchestratore.session.Session;
import java.io.*;
import java.net.*;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import keysuite.docer.client.ClientUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

public class Tools {
    static Logger log = LoggerFactory.getLogger(Tools.class);
    private static final SecureRandom random = new SecureRandom();
    protected final static Logger logger = LoggerFactory.getLogger(Tools.class);
    static final int memorySize = 16 * 1024 * 1024;

    public static String getMimeType(File file) {
        String mimetype = "";
        if (file.exists()) {
            if (getSuffix(file.getName()).equalsIgnoreCase("png")) {
                mimetype = "image/png";
            } else if (getSuffix(file.getName()).equalsIgnoreCase("jpg")) {
                mimetype = "image/jpg";
            } else if (getSuffix(file.getName()).equalsIgnoreCase("jpeg")) {
                mimetype = "image/jpeg";
            } else if (getSuffix(file.getName()).equalsIgnoreCase("gif")) {
                mimetype = "image/gif";
            } else {
                javax.activation.MimetypesFileTypeMap mtMap = new javax.activation.MimetypesFileTypeMap();
                mimetype = mtMap.getContentType(file);
            }
        }
        return mimetype;
    }

    public static String getSuffix(String filename) {
        String suffix = "";
        int pos = filename.lastIndexOf('.');
        if (pos > 0 && pos < filename.length() - 1) {
            suffix = filename.substring(pos + 1);
        }
        return suffix;
    }

    public static boolean isRemovible(String path) throws IOException {

        boolean isRemovible = true;
        String defaultDir = new File(it.kdm.doctoolkit.utils.Utils.getConfigHome(), "upload").getAbsolutePath();
        String excludePathsToRemove = ToolkitConnector.getGlobalProperty("excludePathsToRemove", defaultDir);
        String[] exclusions = excludePathsToRemove.split(",");
        File ff = new File(path);
        if (ff.exists() && !FileUtils.isSymlink(ff)) {
            for (String exclusion : exclusions) {
                if (ff.getAbsolutePath().equalsIgnoreCase(new File(exclusion).getAbsolutePath())) {
                    isRemovible = false;
                    break;
                }
            }
        } else {
            isRemovible = false;
        }

        return isRemovible;
    }

    public static void removeQuietly(String fPath) {
        final String fullPath = fPath;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (!Strings.isNullOrEmpty(fullPath)) {

                    try {
                        URL url = new URL(fullPath);
                        String token = null;
                        try {
                            token = Session.getUserInfo().getJwtToken();
                            if (StringUtils.isEmpty(token)) {
                                token = ClientUtils.simpleJWTToken("system", "admin", "SECRET");
                            }
                        } catch (Exception x2) {
                            token = ClientUtils.simpleJWTToken("system", "admin", "SECRET");
                        }
                        WebClient client = WebClient
                                .builder()
                                .defaultHeader("Authorization", "Bearer " + token)
                                .codecs(configurer -> configurer
                                        .defaultCodecs()
                                        .maxInMemorySize(memorySize))
                                .build();
                        ClientResponse clientResponseDelete = client.delete().uri(fullPath).exchange().block();
                        //Map resp = clientResponseDelete.bodyToMono(Map.class).block();
                        int status = clientResponseDelete.rawStatusCode();
                        if (status > 299) {
                            logger.error("Il file: " + fullPath + " non è stato eliminato status: " + status);
                        }

                    } catch (MalformedURLException ex) {


                        File filein = new File(fullPath);

                        if (!filein.exists()) {
                            try {
                                filein = new File(URLDecoder.decode(fullPath, "UTF-8"));
                            } catch (IOException e) {
                                log.error("removeFileOrFolder errore generico nel recupero del file da eliminare: " + filein.getAbsolutePath(), e);
                            }
                        }

                        if (!filein.exists()) {
                            File path = new File(it.kdm.doctoolkit.utils.Utils.getConfigHome(), "upload");
                            File defFile = new File(path, fullPath);
                            if (defFile.exists()) {
                                try {
                                    filein = defFile;
                                } catch (Exception e) {
                                    log.error("removeFileOrFolder errore generico nel recupero del file da eliminare: " + defFile.getAbsolutePath(), e);
                                }

                                if (!filein.exists()) {
                                    try {
                                        filein = new File(path, URLDecoder.decode(fullPath, "UTF-8"));
                                    } catch (IOException e) {
                                        log.error("removeFileOrFolder errore nella conversione del filename: " + fullPath, e);
                                    }
                                }
                            }
                        }

                        try {
                            if (isRemovible(filein.getAbsolutePath())) {
                                if (filein.isDirectory()) {
                                    try {
                                        FileUtils.deleteDirectory(filein);
                                    } catch (IOException ex1) {
                                        log.error("removeFileOrFolder impossibile eliminare la cartella: (" + filein.getAbsolutePath() + ")", ex1);
                                    }
                                } else {
                                    try {
                                        FileUtils.forceDelete(filein);
                                    } catch (IOException ex2) {
                                        log.error("removeFileOrFolder impossibile eliminare il file: (" + filein.getAbsolutePath() + ")", ex2);
                                    }

                                }
                            } else {
                                log.error("removeFileOrFolder il file o la cartella: (" + filein.getAbsolutePath() + ") non esistono, non sono stati trovati sul server, o sono bloccati dal system.properties alla proprieta': excludePathsToRemove");
                            }
                        } catch (IOException e) {
                            log.error("Errore generico nella rimozione del file: " + filein.getAbsolutePath(), e);
                        }
                    }
                }
            }

        }, 0);
    }

    public static HashMap<String, String> getApplicationDBConnection() throws Exception {
        HashMap<String, String> connectionProperty = new HashMap<String, String>();

        String dbJndi = ApplicationProperties.get(DBManager.PROP_DB_JNDI_DATASOURCE);

        connectionProperty.put(DBManager.PROP_DB_JNDI_DATASOURCE, dbJndi);
        return connectionProperty;
    }

    public static String getSimpleDate(Date date) {
        if (date == null) return "";
        try {
            SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return dateFormatGmt.format(date);
        } catch (Exception e) {
            SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormatGmt.format(date);
        }
    }

    public static URI toURI(File file) throws URISyntaxException {

        String path = file.toString();

        if (path.contains(":"))
            return new URI(path);
        else
            return file.toPath().toUri();
    }

    public static URI toURL(HttpServletRequest request, File file) throws UnsupportedEncodingException, URISyntaxException {
        File uploadPath = new File(Utils.getConfigHome(), "upload");

        String path = file.getPath();

        if (path.startsWith(uploadPath.getAbsolutePath()))
            path = path.substring(uploadPath.getAbsolutePath().length());
        else
            return null;

        String curr = request.getRequestURL().toString();

        int idx = curr.indexOf("/zip/");

        curr = curr.substring(0, idx);

        String part = URLEncoder.encode(path, "UTF-8")
                .replaceAll("\\+", "%20")
                .replaceAll("\\%21", "!")
                .replaceAll("\\%27", "'")
                .replaceAll("\\%28", "(")
                .replaceAll("\\%29", ")")
                .replaceAll("\\%7E", "~");

        String url = curr + "/rest-utils/getFile?fileName=" + part;

        return new URI(url);
    }

    public static File toFile(URI uri) throws IOException {

        String scheme = uri.getScheme();
        if (scheme == null)
            return null;
        scheme = scheme.toLowerCase();

        if (scheme.startsWith("http")) {

            String uriString = uri.toString();

            File dest = null;

            String regex = ToolkitConnector.getGlobalProperty("", "^.*(?:\\/FileServer\\/getFile|\\/rest-utils\\/getFile)([^\\\\?]*)\\??[^&=]*(?:=([^&]+))?$");

            Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(uriString);
            if (m.find()) {

                dest = new File(Utils.getConfigHome(), "upload");

                if (!Strings.isNullOrEmpty(m.group(1)))
                    dest = new File(dest, URLDecoder.decode(m.group(1), "UTF-8"));

                if (!Strings.isNullOrEmpty(m.group(2)))
                    dest = new File(dest, URLDecoder.decode(m.group(2), "UTF-8"));

                if (dest.exists())
                    return dest;
            }

            String ext = FilenameUtils.getExtension(uri.toString());
            dest = tempFile(ext);
            FileUtils.copyURLToFile(uri.toURL(), dest, 10000, 100000);
            return dest;
        }

        return new File(uri.getPath());
    }

    public static File tempFile(String ext) {
        File uploadPath = new File(Utils.getConfigHome(), "upload");

        long n = random.nextLong();
        if (n == Long.MIN_VALUE) {
            n = 0;      // corner case
        } else {
            n = Math.abs(n);
        }

        String path = uploadPath + "/tmp" + n;
        if (!Strings.isNullOrEmpty(ext)) {
            if (ext.startsWith("."))
                ext = ext.substring(1);
            path += "." + ext;
        }


        return new File(path);
    }

    public static String formatDifference(long startInMillis, long endInMillis) {
        String res = "";
        if (startInMillis > 0 && endInMillis > 0 && endInMillis > startInMillis) {
            String sMillis = "" + (endInMillis - startInMillis);
            double dMillis = 0;

            int days = 0;
            int hours = 0;
            int minutes = 0;
            int seconds = 0;
            int millis = 0;

            String sTime;

            try {
                dMillis = Double.parseDouble(sMillis);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }


            seconds = (int) (dMillis / 1000) % 60;
            millis = (int) (dMillis % 1000);

            if (seconds > 0) {
                minutes = (int) (dMillis / 1000 / 60) % 60;
                if (minutes > 0) {
                    hours = (int) (dMillis / 1000 / 60 / 60) % 24;
                    if (hours > 0) {
                        days = (int) (dMillis / 1000 / 60 / 60 / 24);
                        if (days > 0) {
                            sTime = days + " days " + hours + " hours " + minutes + " min " + seconds + " sec " + millis + " millisec";
                        } else {
                            sTime = hours + " hours " + minutes + " min " + seconds + " sec " + millis + " millisec";
                        }
                    } else {
                        sTime = minutes + " min " + seconds + " sec " + millis + " millisec";
                    }
                } else {
                    sTime = seconds + " sec " + millis + " millisec";
                }
            } else {
                sTime = dMillis + " millisec";
            }

            System.out.println("time: " + sTime);
            res = sTime;
        }
        return res;
    }

    public static String readableFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public static List<File> existDocumentsLikeName(String likeName, String path) {
        List<File> exist = new ArrayList<>();
        File folder = new File(Paths.get(path).toString());
        if (folder.exists()) {
            String[] files = folder.list();
            for (String fileName : files) {
                if (fileName.startsWith(likeName)) {
                    exist.add(new File(Paths.get(path, fileName).toString()));
                }
            }
        }
        return exist;
    }
}