import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread{
    
    ServerSocket serverSocketClient;
    ServerSocket serverSocketSlaver;
     public int port;
     public int portSlaver;

    public static int He;
    public static int Wi;
     List<GestionSlaver> slavers =new ArrayList<>();
    int idSlaver = 1 ;
    public void SetSocketFronFile(String filepath) throws IOException {
        File file=new File(filepath);
        BufferedReader br=new BufferedReader(new FileReader(file));
        String line="";
        while(line!=null){
            line=br.readLine();
            if(line==null)break;
            String[]lineServer=line.split(";");
            this.port=Integer.parseInt(lineServer[0]);
            this.portSlaver=Integer.parseInt(lineServer[1]);
        }
      
    }
    public Server (String[]args) throws IOException {
        SetSocketFronFile(args[0]);
    }

    @Override
    public void run(){
        try {
            serverSocketClient =new ServerSocket(port);
            System.out.println("Server waiting Client at port "+port);

      //
       new Thread(new Runnable() {
           @Override
           public void run() {
               try{
                   serverSocketSlaver=new ServerSocket(portSlaver);
                   while(true){
                       System.out.println("Waiting for Slaver");
                       Socket socket=serverSocketSlaver.accept();
                       GestionSlaver s=new GestionSlaver(socket,idSlaver++);
                       slavers.add(s);
                       System.out.println("there are "+slavers.size()+" slavers");
                   }
               } catch (IOException e) {
                   throw new RuntimeException(e);
               }
           }
       },"socket Slaver").start();
          new Thread(new Runnable() {
              @Override
              public void run() {
                   try{
                       while(true){
                         new GestionClient(serverSocketClient.accept(),slavers).start();
                       }
                   } catch (IOException e) {
                       throw new RuntimeException(e);
                   }
              }
          },"socket client") .start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) throws IOException {
        new Server(args).start();
    }
    
}
