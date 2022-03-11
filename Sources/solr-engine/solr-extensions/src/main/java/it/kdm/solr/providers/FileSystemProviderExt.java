package it.kdm.solr.providers;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.apache.solr.common.util.ContentStream;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * Created by Paolo_2 on 10/05/17.
 */
public class FileSystemProviderExt extends FileSystemProvider {

    static final String CONTENT_HASH = "content_hash";

    @Override
    public void safeSave( ContentStream stream , File file ) throws IOException
    {
        file.getParentFile().mkdirs();

        // copia lo stream http in un file temporaneo e poi lo sposta in modo atomico

        File tempFile = new File(file.getParent(), UUID.randomUUID().toString() ); // Files.createTempFile(UUID.randomUUID().toString(), (String) null);

        Path tempPath = tempFile.toPath();

        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        DigestInputStream dis = new DigestInputStream(stream.getStream(), digest);

        Files.copy(dis, tempPath, StandardCopyOption.REPLACE_EXISTING);

        String hash = Hex.encodeHexString(digest.digest());

        log.debug("sha-256 hash:{} file:{}", hash, tempFile.getPath());

        try
        {
            Files.move(tempPath, file.toPath() , StandardCopyOption.ATOMIC_MOVE , StandardCopyOption.REPLACE_EXISTING );
            log.info("stream moved from temp source:{} dest:{}",tempPath,file);
            req.getContext().put(CONTENT_HASH,hash);
            return;
        }
        catch(Exception e)
        {
            if (!FileUtils.deleteQuietly(tempFile))
                log.warn("could not delete temp file:{}", tempPath);

            log.error("stream move failed source:{} dest:{}", tempPath, file,e);
            throw e;
        }
    }

}
