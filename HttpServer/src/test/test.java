import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicException;
import net.sf.jmimemagic.MagicMatchNotFoundException;
import net.sf.jmimemagic.MagicParseException;

import java.io.File;

public class test {
    public static void main(String[] args) {
        File file = new File("D:\\WebServer\\HttpServer\\src\\test\\qsn.jpg");
        try {
            System.out.println(Magic.getMagicMatch(file, false).getMimeType());
        } catch (MagicParseException | MagicMatchNotFoundException | MagicException e) {
            e.printStackTrace();
        }
    }
}
