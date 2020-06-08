package com.example.coup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class  ServerConnection {

    /**Handles connection between Server and Client*/


    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;


    public void connect(String hostname, int port) throws IOException {

        this.socket=new Socket(hostname,port);
        this.reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer=new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

    }

    public Socket getSocket(){return this.socket; }

    public void sendMessage(String msg){
        this.writer.println(msg);
    }

    public String getMessage() throws IOException {return this.reader.readLine();}

    public void disconnect() throws IOException {
        this.socket.close();
        this.socket=null;
        this.reader=null;
        this.writer=null;
    }

    public boolean connected(){return this.socket==null?false:true;}
}
