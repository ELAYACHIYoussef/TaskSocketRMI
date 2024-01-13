import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Util implements IneUtil{
    @Override
    public BufferedImage filter(byte[] file, Kernel kernel) throws IOException {
       BufferedImage bi = byteToBufferd(file);
        ConvolveOp convolution=new ConvolveOp(kernel,ConvolveOp.EDGE_NO_OP,null);
        BufferedImage res=convolution.filter(bi,null);
        return res;
    }

    @Override
    public BufferedImage byteToBufferd(byte[] b) throws IOException {
        BufferedImage bufferedImage=null;
        InputStream is =new ByteArrayInputStream(b);
        bufferedImage= ImageIO.read(is);
        return bufferedImage;
    }

    @Override
    public byte[] bufferdToByte(BufferedImage bi) throws IOException {
       byte[] b=null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bi, "jpg", baos);
            b = baos.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return b ;
    }

}
