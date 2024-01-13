import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Thread{

    //port et adress pour client
    int port;
    String host;
    // read from command line
    File image;

    String dis="./assets/imagefiltred.jpg";

    float[] Kernel;
     Socket socket;
     ObjectInputStream in;
     ObjectOutputStream out;

     public void SetSocketFromFile(String filepath) throws IOException {
         File file=new File(filepath);
         BufferedReader br =new BufferedReader(new FileReader(file));
         String line="";
         while(line!=null){
             line=br.readLine();
             if(line==null)break;
             String[] lineSt=line.split(";");
             this.host=lineSt[0];
             this.port=Integer.parseInt(lineSt[1]);

         }
     }

     public void SetKernelFromFile(String filepath) throws IOException {
         File file =new File(filepath);
         BufferedReader br=new BufferedReader(new FileReader(file));
         String line="";
         while(line!=null){
             line= br.readLine();
             if(line==null)break;
             String[] lineKernel=line.split(";");
             Kernel =new float[lineKernel.length];
             for(int i=0;i< lineKernel.length;i++){
                 this.Kernel[i]= Float.parseFloat((lineKernel[i]));
             }

         }
     }
     public void SetImage(String filepath){
         this.image=new File(filepath);
     }

    @Override
    public void run() {
        try {
            socket = new Socket(host, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
                Data data = new Data();
                //Transfer file image to byte
                FileInputStream fileInputStream = new FileInputStream(image);
                byte[] b = new byte[fileInputStream.available()];
                fileInputStream.read(b);
                fileInputStream.close();

                //Send Data image and Kernel
                data.setF(b);
                data.setKirnel(Kernel);
                data.setHi((int) Math.sqrt(Kernel.length));
                data.setWi((int) Math.sqrt(Kernel.length));

                out.writeObject(data);
                out.flush();
                System.out.println("hihihihi");
                data = (Data) in.readObject();
                File file = new File(dis);
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(data.getF());
                fileOutputStream.close();
                socket.close();

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public Client(String[] args)throws  IOException{
        SetSocketFromFile(args[0]);
        SetImage(args[1]);
        SetKernelFromFile(args[2]);
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException {
        if(args.length<3){
            System.out.println("There is missing argement");
            System.exit(-1);
        }
        System.out.println("La Client cccc");
        Client client=new Client(args);
        //client.SendRecieveData();
   client.start();

    }



}
