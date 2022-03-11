package keysuite.docer.utils.qrCodeUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.jsoup.Jsoup;

public class ScapigliaturaUtils {
    private static final ResolutionUtils rs = new ResolutionUtils();

    private final DataUtils dataUtils;

    protected ScapigliaturaUtils(DataUtils dataUtils){
        this.dataUtils = dataUtils;
    }

    public void addScapigliatura(PDDocument doc) throws IOException {
        int width = rs.changeResolution(210);
        int height = rs.changeResolution(10);
        int fontSize = 65;

        int imageWidth = width * 10;
        int imageHeight = height * 10;

        int endPage = doc.getPages().getCount();

        dataUtils.put("pages", endPage);

        for (int i=0; i<endPage; i++) {
            BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = image.createGraphics();
            graphics.setBackground(new Color(0, 0, 0, 0));
            graphics.setColor(new Color(64, 64, 64));
            graphics.setFont(new Font("Verdana", Font.PLAIN, fontSize));

            dataUtils.put("page", i + 1);
            String scapigliatura = dataUtils.generate("scapigliatura");
            scapigliatura = Jsoup.parse(scapigliatura).text();


            graphics.drawString(scapigliatura,0,fontSize);
            PDPage page = doc.getPage(i);
            PDPageContentStream cs = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true);

            PDImageXObject ximage = LosslessFactory.createFromImage(doc, image);

            cs.drawImage(ximage, rs.changeResolution(5), 0, width, height);
            cs.close();
        }
    }
}
