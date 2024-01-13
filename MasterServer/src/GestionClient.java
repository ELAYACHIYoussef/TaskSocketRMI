import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GestionClient extends Thread{

    Socket socket;
    ObjectOutputStream out;
    ObjectInputStream in;

    public int KernelHi;
    public int KernelWi;
    public float[]Kernel;

    IntUtil util= new Util();
   List<Data> filterParty =new ArrayList<>();
   List<GestionSlaver> slavers;

   public GestionClient(Socket socket,List<GestionSlaver> slavers){
       this.socket=socket;
       this.slavers=slavers;
       System.out.println("client coneccted");
   }
    @Override
    public void run() {
        try{
            System.out.println("client youssef");
            in = new ObjectInputStream(socket.getInputStream());
            out= new ObjectOutputStream(socket.getOutputStream()) ;
            System.out.println("client youssef1");
            Data data=(Data) in.readObject();

            KernelHi= data.hi;
            KernelWi=data.wi;
            Kernel= data.Kirnel;
            System.out.println("client youssef");
            File image=new File("./assets/ImageUnit.jpg");
            FileOutputStream outf=new FileOutputStream(image);
            outf.write(data.f);
            outf.close();
            while(true){
                if(slavers.size()<1){
                    System.out.println("Waitng for Slaver Connected  ");
                    continue;
                }
                Stack<BufferedImage> st = util.Decouper(image,slavers.size());
                util.DistrToSlavers(filterParty,st,slavers,Kernel,KernelWi,KernelWi);
                break;
            }

             new Thread(new Runnable() {
                 @Override
                 public void run() {
                     try{
                         while(true){
                             if(filterParty.size()<slavers.size()){
                                 System.out.println(" waiting for slavers Response... ");
                                 continue;
                             }
                             data.setF(util.Merge(filterParty));
                             out.writeObject(data);
                             out.flush();
                             filterParty.clear();
                             System.out.println("size of filtred  part images afted merging is"+filterParty.size());
                             break;
                         }
                     } catch (IOException e) {
                         throw new RuntimeException(e);
                     }
                 }
             }).start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


}
