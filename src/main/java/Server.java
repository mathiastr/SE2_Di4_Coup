import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {


    public static void main(String[] args) {






        try {
            ServerSocket server = new ServerSocket(53214);

            Socket socket;

            System.out.println("Server started");

            while (true){

                System.out.println("Waiting for connection");

                socket=server.accept();

                System.out.println(socket.getInetAddress().getHostAddress()+" connected");

                Player player = new Player(socket);
                player.start();




            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
