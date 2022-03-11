package keysuite.docer.client;

import java.io.InputStream;

public interface NamedInputStream{
    InputStream getStream();
    String getName();
    public static NamedInputStream getNamedInputStream(InputStream stream, String name){
        return new NamedInputStream() {
            @Override public InputStream getStream() { return stream; }
            @Override public String getName() { return name; }
        };
    }
}
