import java.awt.image.BufferedImage;
import java.awt.image.Kernel;
import java.io.IOException;

public interface IneUtil {
    public BufferedImage filter(byte[] file, Kernel kernel)throws IOException;
    public BufferedImage byteToBufferd(byte[] b) throws IOException;
    public  byte[] bufferdToByte(BufferedImage bi) throws IOException;
}
