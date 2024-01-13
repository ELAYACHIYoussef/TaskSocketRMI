import java.awt.image.BufferedImage;
import java.awt.image.Kernel;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Slaver extends Thread{

    Socket socket;
    ObjectInputStream in;
    ObjectOutputStream out;

    int port;
    String host;

    IneUtil util =new Util();

    public Slaver(String[] args) throws IOException {
        SetSocketFromFile(args[0]);
    }

    public void SetSocketFromFile(String filepath) throws IOException {
        File file=new File(filepath);
        BufferedReader br=new BufferedReader(new FileReader(file));

        String line="";
        while(line!=null){
            line =br.readLine();
            if(line==null)break;
            String[] StringSlaver=line.split(";");
            this.host=StringSlaver[0];
            this.port=Integer.parseInt(StringSlaver[1]);

        }
    }

    @Override
    public void run() {
        try{
            socket=new Socket(host,port);
            in=new ObjectInputStream(socket.getInputStream());
            out=new ObjectOutputStream(socket.getOutputStream());
            while(true){
                Data data=(Data) in.readObject();
                System.out.println(data.f);
                Kernel kernel= new Kernel(data.getHi(), data.getWi(),data.getKirnel());
                BufferedImage res=util.filter(data.f,kernel);
                byte[] b = util.bufferdToByte(res);
                data.setF(b);
                out.writeObject(data);
                out.flush();
                System.out.println(b);
            }
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        new Slaver(args).start();
    }
}
