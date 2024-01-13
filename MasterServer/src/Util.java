import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class Util implements IntUtil{

    @Override
    public Stack<BufferedImage> Decouper(File image, int n) throws IOException {
        Stack imageDiv= new Stack<BufferedImage>();
        BufferedImage bufferedImage= ImageIO.read(image);
        int he= bufferedImage.getHeight();
        int wi=bufferedImage.getWidth();
        Server.He=he;
        Server.Wi=wi;
        for(int i = 0; i < n; i++){
            BufferedImage tmp_Recorte = ((BufferedImage) bufferedImage).getSubimage(0, i * (he / n) , wi , he / n) ;
            imageDiv.push(tmp_Recorte);

        }
        return imageDiv ;
    }

    @Override
    public void DistrToSlavers(List<Data> filterParty, Stack<BufferedImage> st, List<GestionSlaver> slavers, float[] Kernel, int he, int wi) {
        Iterator<BufferedImage> itr=st.iterator();
        int ind=0;

        while(itr.hasNext()){
            BufferedImage bi=itr.next();
            GestionSlaver slaver=(GestionSlaver) slavers.get(ind++);


            new Thread(new Runnable() {
                @Override
                public void run() {
                    synchronized (this){
                        try{
                            Socket socket=slaver.socket;
                            ObjectOutputStream out= new ObjectOutputStream(socket.getOutputStream());
                            ObjectInputStream in =new ObjectInputStream(socket.getInputStream());

                            File f=new File("./assets/ImagePart"+slaver.id+".jpg");
                            ImageIO.write(bi,"jpg",f);
                            FileInputStream fileInputStream=new FileInputStream(f);
                            byte[] b= new byte[fileInputStream.available()];
                            fileInputStream.read(b);
                            fileInputStream.close();

                            Data data=new Data();
                            data.setId(slaver.id);
                            data.setF(b);
                            data.setHi(he);
                            data.setWi(wi);
                            data.setKirnel(Kernel);
                            out.writeObject(data);
                            out.flush();

                            data=(Data) in.readObject();
                            filterParty.add(data);
                            System.out.println(Thread.currentThread().getName()+" id is "+data.id);
                      } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }).start();
        }
    }

    @Override
    public byte[] Merge(List<Data> Party) throws IOException {
        int x=0,y=0;
        BufferedImage result = new BufferedImage(
                Server.Wi, Server.He, //work these out
                BufferedImage.TYPE_INT_RGB);

        Data data ;

        for (int i = 1; i <= Party.size()  ;++i){

            data = getItemById(i,Party);
            File file = new File("assets/Parties.jpg");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(data.f);
            fileOutputStream.close();

            BufferedImage bi = byteToBufferd(data.getF());


            result.createGraphics().drawImage(bi,x,y,null);
            y+= bi.getHeight();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(result, "jpeg", baos);
        return baos.toByteArray();
    }
    @Override
    public BufferedImage byteToBufferd(byte[] b) throws IOException {
        BufferedImage bufferedImage=null;
        InputStream is =new ByteArrayInputStream(b);
        bufferedImage= ImageIO.read(is);
        return bufferedImage;
    }
    @Override
    public Data getItemById(int id,List<Data> list){
        Data data = null;
        for (int i = 0 ; i < list.size() ; i++){
            data = (Data)  list.get(i);
            if(data.id == id)
                return data;
        }
        return data ;
    }
}
