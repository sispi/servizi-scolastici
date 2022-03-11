package keysuite.docer.utils.qrCodeUtils;

import it.kdm.doctoolkit.services.ToolkitConnector;

public class ResolutionUtils {

    private static final int dpiResolution = Integer.parseInt(ToolkitConnector.getGlobalProperty("timbro.etichetta.dpiResolution","120")); //72
    public Integer changeResolution(Object value){
        return Math.round(Float.parseFloat(value.toString()) * dpiResolution / 25.4f);
    }
}
