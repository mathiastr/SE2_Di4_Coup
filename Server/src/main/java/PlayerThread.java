import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class PlayerThread extends Thread {


    private final List<Socket> players;
    private PrintWriter writer1;
    private PrintWriter writer2;
    private BufferedReader reader1;
    private BufferedReader reader2;
    private int avaiable = 0;

    public PlayerThread(List<Socket> players){


        this.players=players;


    }


    @Override
    public void run(){

        //get Sockets

        Socket player1 = players.get(0);
        Socket player2 = players.get(1);

        try {

            //set up readers and writers

            this.writer1 = new PrintWriter(new OutputStreamWriter(player1.getOutputStream()), true);
            this.writer2 = new PrintWriter(new OutputStreamWriter(player2.getOutputStream()), true);
            this.reader1 = new BufferedReader(new InputStreamReader(player1.getInputStream()));
            this.reader2 = new BufferedReader(new InputStreamReader(player2.getInputStream()));

            //decide turn
            writer1.println("turn");
            writer2.println("wait");

            boolean check = true;

            //game loop
            while(check&&(isRunning(player1,player2))){
                while(isRunning(player1,player2)){
                    String msg = reader1.readLine();
                    if(msg==null)
                        break;
                    else if(msg.equals("next")){
                        writer2.println("turn");
                        break;
                    }else
                        System.out.println(msg);
                    if(msg.equals("exit")){
                        writer2.println("win");
                        writer1.println("lose");
                        check=false;
                        break;
                    }
                }

                while (isRunning(player1,player2)){
                    String msg = reader2.readLine();
                    if(msg==null)
                        break;
                    else if(msg.equals("next")){
                        writer1.println("turn");
                        break;
                    }else
                        System.out.println(msg);
                    if(msg.equals("exit")){
                        writer1.println("win");
                        writer2.println("lose");
                        check=false;
                        break;
                    }

                }
            }


            System.out.println("Connection to "+player1.getInetAddress().getHostAddress()+" closed");
            System.out.println("Connection to "+player2.getInetAddress().getHostAddress()+" closed");
        } catch (IOException e) {
            e.printStackTrace();
            //work in progress / the remaining player wins
            if(player1.isConnected()){
                try {
                    this.writer1 =new PrintWriter(new OutputStreamWriter(player1.getOutputStream()), true);
                    writer1.println("win");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }else if(player2.isConnected()){
                try {
                    this.writer2 =new PrintWriter(new OutputStreamWriter(player2.getOutputStream()), true);
                    writer2.println("win");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

    }

    private boolean isRunning(Socket socket1, Socket socket2){return socket1.isConnected()&&socket2.isConnected();}
}


