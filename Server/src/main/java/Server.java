import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Server {




    public static void main(String[] args) {



        List<Socket> players =new LinkedList<Socket>();


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


                    if(players.size()>=2){
                        System.out.println("Staritng Multiplayer Thread");
                        System.out.println(players.isEmpty());

                        final List<Socket> tee = players;


                        //starts the Player thread to handle game
                        PlayerThread thread = new PlayerThread(tee);
                        thread.start();

                        long start = System.currentTimeMillis();

                        //sleep for 3 sec
                        while (System.currentTimeMillis()-start<3000){

                        }


                        players.clear();

                    }

                }catch (SocketTimeoutException te){

                    System.out.println("Time out retrying");
                    if(players.size()==1){

                        try {
                            //force client to reconnect
                            PrintWriter writer = new PrintWriter(new OutputStreamWriter(players.get(0).getOutputStream()));
                            writer.println("noplayer");
                            writer.close();
                            players.get(0).close();
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
