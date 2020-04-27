import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class Player extends Thread {


    private Socket client;
    private PrintWriter writer;
    private String clientAddress;


    public Player(Socket client){

        this.client=client;


    }


    @Override
    public void run(){

        try {


            this.writer = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
            writer.write("Works!");
            writer.flush();



            client.close();
            System.out.println("Connection to "+this.client.getInetAddress().getHostAddress()+" closed");
        } catch (IOException e) {
            e.printStackTrace();
        }




    }
}
