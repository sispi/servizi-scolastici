package keysuite.desktop.zuul;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

public abstract class SlidingInputStream extends InputStream {

    List<Integer> list = new LinkedList<>();
    byte[] markBytes = null;
    byte[] markToInsert = null;
    boolean eof = false;
    boolean found = false;
    int inserted = 0;
    InputStream source;

    public SlidingInputStream(InputStream source) {
        this.source = source;
        this.markBytes = getBytesToFind();
    }

    public abstract byte[] getBytesToInsert();

    public abstract byte[] getBytesToFind();

    @Override
    public void close() throws IOException {
        source.close();
    }

    @Override
    public int available() throws IOException {
        int a = super.available();
        if (a > 0)
            return a + inserted;
        else
            return a;
    }

    @Override
    public int read() throws IOException {

        while (!eof && list.size() < markBytes.length) {
            int b = source.read();
            if (b == -1) {
                eof = true;
                break;
            }
            list.add(b);
        }
        if (!found && !eof && inserted == 0) {
            found = true;
            for (int i = 0; i < markBytes.length; i++) {
                if (markBytes[i] != list.get(i)) {
                    found = false;
                    break;
                }
            }
        }

        if (found) {
            if (markToInsert == null) {
                markToInsert = getBytesToInsert();
                inserted = 0;
            }

            if (inserted >= markToInsert.length) {
                found = false;
            } else {
                byte b = markToInsert[inserted];
                inserted++;
                return b;
            }
        }

        if (list.size() == 0)
            return -1;
        else
            return list.remove(0);
    }
}
