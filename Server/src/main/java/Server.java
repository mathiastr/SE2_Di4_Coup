import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Server {


    private static int MAXPLAYERS = 4;





    public static void main(String[] args) {


         List<Socket> players = new LinkedList<Socket>();


        //Work in progress



        try {
            ServerSocket server = new ServerSocket(53214);

            // timeout for x seconds
            server.setSoTimeout(30000);

            Socket socket;

            System.out.println("Server started");



            while (true){

                System.out.println("Waiting for connection");

                try{
                    socket=server.accept();

                    System.out.println(socket.getInetAddress().getHostAddress()+" connected");

                    players.add(socket);


                    if(players.size()>=MAXPLAYERS){
                        System.out.println("Staritng Multiplayer Thread");
                        System.out.println(players.isEmpty());

                        final List<Socket> lobby = new LinkedList<Socket>();

                        for(Socket player:players)
                            lobby.add(player);


                        //starts the Player thread to handle game
                        PlayerThread thread = new PlayerThread(lobby);
                        thread.start();

                        long start = System.currentTimeMillis();

                        //sleep for 3 sec
                        while (System.currentTimeMillis()-start<3000){

                        }

                        players.clear();




                    }

                }catch (SocketTimeoutException te){

                    System.out.println("Time out retrying");
                    if(players.size()<MAXPLAYERS){

                        try {
                            //force client to reconnect
                            List<PrintWriter> writers = new LinkedList<PrintWriter>();
                            for(int i=0;i<players.size();i++)
                                writers.add(new PrintWriter(new OutputStreamWriter(players.get(i).getOutputStream())));

                            for(PrintWriter writer:writers)
                                writer.println("noplayer");

                            for(PrintWriter writer:writers)
                                writer.close();

                            for (Socket player:players)
                                player.close();

                            players.clear();
                        } catch (IOException ex) {
                            ex.printStackTrace();

                        }


                    }
                }




            }

        } catch (IOException e) {
            e.printStackTrace();


        }finally {

            players.clear();

        }


    }

}
