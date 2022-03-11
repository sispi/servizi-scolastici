package keysuite.docer.utils.qrCodeUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.decoder.Version;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import javax.imageio.ImageIO;
import org.apache.commons.codec.binary.Base64;

public class QrCodeGenerator {
    public String createQrCodeImageBase64(String message) throws WriterException, IOException {
        int size = 53 * 3 + 5;
        size *= 5;

        Integer messageSize = message.getBytes().length * 8;
//        Version version = chooseVersion(messageSize, ErrorCorrectionLevel.L);

        Hashtable<EncodeHintType, Object> hintMap = new Hashtable<>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        hintMap.put(EncodeHintType.MARGIN, 0);
//        hintMap.put(EncodeHintType.QR_VERSION, version); //9
        BitMatrix byteMatrix = new QRCodeWriter().encode(message, BarcodeFormat.QR_CODE, size, size, hintMap);

        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);

        Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, size, size);
        graphics.setColor(Color.BLACK);

        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++) {
                if (byteMatrix.get(i, j)) graphics.fillRect(i, j,1,1);
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Base64 encoder = new Base64();

        ImageIO.write(image, "jpg", baos);

        String result = new String(encoder.encode(baos.toByteArray()));
        baos.close();
        return result;
    }
    private static Version chooseVersion(int numInputBits, ErrorCorrectionLevel ecLevel) throws WriterException {
        // In the following comments, we use numbers of Version 7-H.
        for (int versionNum = 1; versionNum <= 40; versionNum++) {
            Version version = Version.getVersionForNumber(versionNum);
            // numBytes = 196
            int numBytes = version.getTotalCodewords();
            // getNumECBytes = 130
            Version.ECBlocks ecBlocks = version.getECBlocksForLevel(ecLevel);
            int numEcBytes = ecBlocks.getTotalECCodewords();
            // getNumDataBytes = 196 - 130 = 66
            int numDataBytes = numBytes - numEcBytes;
            int totalInputBytes = (numInputBits + 7) / 8;
            if (numDataBytes >= totalInputBytes) {
                return version;
            }
        }
        throw new WriterException("Data too big");
    }
}
