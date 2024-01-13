import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Stack;

public interface IntUtil {
    public Stack<BufferedImage> Decouper(File image , int n) throws IOException;
    public void DistrToSlavers(List<Data> filterParty,Stack<BufferedImage> st,List<GestionSlaver> slavers,float[] Kernel,int he,int wi);
    public byte[] Merge(List<Data> filterParty) throws IOException;
    public BufferedImage byteToBufferd(byte[] b) throws IOException;
    public Data getItemById(int id,List<Data> list);
}
