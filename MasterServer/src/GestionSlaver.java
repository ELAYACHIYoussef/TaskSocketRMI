import java.net.Socket;

public class GestionSlaver {
    Socket socket;

    int id;
    public GestionSlaver(Socket socket,int id){
        this.socket=socket;
        this.id=id;
        System.out.println("Slaver Connected");
    }
}
