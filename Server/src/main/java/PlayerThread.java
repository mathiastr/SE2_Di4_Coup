
import java.io.*;
import java.net.Socket;
import java.util.*;

public class PlayerThread extends Thread {


    private List<Socket> players;
    private Map<String, PrintWriter> toPlayer;
    private Map<String, BufferedReader> fromPlayer;
    //private Map<BufferedReader, String> fromPlayerName;
    private List<PrintWriter> writers;
    private List<BufferedReader> readers;
    private String lastAction;

    private int avaiable = 0;

    public PlayerThread(List<Socket> players){


        this.players=players;


    }


    @Override
    public void run(){



        try {
            writers = new LinkedList<PrintWriter>();
            for (int i = 0; i < this.players.size(); i++)
                writers.add(new PrintWriter(new OutputStreamWriter(this.players.get(i).getOutputStream()),true));

            readers = new LinkedList<BufferedReader>();
            for (int i=0;i<this.players.size();i++)
                readers.add(new BufferedReader(new InputStreamReader(this.players.get(i).getInputStream())));


            //get size
            avaiable=this.players.size();

            this.toPlayer = new HashMap<String, PrintWriter>(avaiable);
            this.fromPlayer = new HashMap<String, BufferedReader>(avaiable);








            //send ok to players
            for(PrintWriter writer:writers)
                writer.println("ok");



            //get and send player names
            List<String> names = new LinkedList<String>();

            for(BufferedReader reader: readers)
                names.add(reader.readLine());

            for(PrintWriter writer: writers){
                for (String name: names)
                    writer.println("playername"+" "+name);
            }



            // map player names to sockets

            for(int i=0;i<avaiable;i++)
                toPlayer.put(names.get(i), writers.get(i));

            for(int i=0;i<avaiable;i++)
                fromPlayer.put(names.get(i), readers.get(i));


            for(int i=0; i<avaiable;i++){
                System.out.println(names.get(i));
            }


            ///////////


            // send different card to every player
            Stack<String> cards = new Stack<String>();
            setUpCards(cards);

            Collections.shuffle(cards);

            for(PrintWriter writer: writers){

                writer.println("card"+" "+cards.pop());
                Collections.shuffle(cards);
                writer.println("card"+" "+cards.pop());


            }




            //pick random player to start

            int rand = (int) (Math.random() * players.size());

            System.out.println("Random start");

            for(int i=0;i<writers.size();i++) {
                if(i==rand)
                    writers.get(i).println("turn");
                else
                    writers.get(i).println("wait");
            }

            System.out.println("random finish");




            boolean running = true;

            int turn =rand;

            boolean actionperformed = false;



            //game loop
            while (running) {

                System.out.println("Avaiable players: " + avaiable);

                String input = readers.get(turn).readLine();

                System.out.println(input);


                if (input == null) {
                    System.out.println(players.get(turn).getInetAddress().getHostAddress() + " left the game");
                    //players.remove(turn);
                    writers.remove(turn);
                    readers.remove(turn);

                    avaiable--;
                    turn++;
                    if(turn>=readers.size())
                        turn=0;
                    writers.get(turn).println("turn");


                    for (int i = 0; i < avaiable; i++) {
                        if (i == turn)
                            continue;

                        writers.get(i).println("nextplayer"+" "+names.get(turn));
                    }

                    if (avaiable == 1)
                        throw new IOException();

                    if(avaiable < 1)
                        break;

                    if (turn >= writers.size())
                        turn = 0;

                }

                if (input.equals("next")) {
                    if(!actionperformed)
                        lastAction="next";

                    String nextplayer;
                    int n = 0;
                    if(turn+1<writers.size())
                        n=turn+1;

                    nextplayer=names.get(n);

                    for (int i = 0; i < avaiable; i++) {
                        if (i == n)
                            continue;
                        writers.get(i).println("nextplayer"+" "+names.get(n));
                    }


                    System.out.println("next player "+nextplayer);



                    turn++;
                    if (turn >= writers.size())
                        turn = 0;
                    writers.get(turn).println("turn");

                    actionperformed=false;

                }
                if (input.equals("exit")) {
                    //pass message to other players
                    for (int i = 0; i < avaiable; i++) {
                        if (i == turn)
                            continue;
                        writers.get(i).println("lostgame"+" "+names.get(turn));
                    }
                    writers.get(turn).println("lose");

                    writers.remove(turn);
                    readers.remove(turn);

                    avaiable--;
                    if (avaiable == 1) {
                        writers.get(0).println("win");
                        break;
                    }

                }

                if (input.startsWith("income") || input.startsWith("foreignaid" )||  input.startsWith("tax")
                        ||input.startsWith("bfa")||input.startsWith("cheat")) {

                    //pass message to other players
                    for (int i = 0; i < avaiable; i++) {
                        if (i == turn)
                            continue;
                        writers.get(i).println(input);
                    }

                    //set last action
                    if(input.startsWith("income"))
                        lastAction="income";
                    if(input.startsWith("foreignaid"))
                        lastAction="foreignaid";
                    if(input.startsWith("tax"))
                        lastAction="tax";
                    if(input.startsWith("bfa"))
                        lastAction="bfa";


                    actionperformed=true;
                }

                if(input.startsWith("steal")){

                    String[] split = input.split(" ");

                    //send message to player to attack
                    toPlayer.get(split[2]).println(input);

                    //get message from player to attack
                    String msg = fromPlayer.get(split[2]).readLine();

                    //player blocks steal
                    if(msg.startsWith("block")){
                        System.out.println();
                        for (int i = 0; i < avaiable; i++) {
                            if (writers.get(i).equals(toPlayer.get(split[2])))
                                continue;
                            writers.get(i).println("block"+" "+split[2]+" "+split[1]+" "+"steal");
                        }
                    }
                    //steal succesful
                    else{
                        //inform players
                        for (int i = 0; i < avaiable; i++) {
                            if (writers.get(i).equals(toPlayer.get(split[2])))
                                continue;
                            writers.get(i).println(input);
                        }

                    }

                    lastAction="steal";

                    actionperformed=true;

                }



                if (input.startsWith("exchange")) {

                    //inform players
                    for (int i = 0; i < avaiable; i++) {
                        if (i == turn)
                            continue;
                        writers.get(i).println(input);
                    }

                    //get and send cards from deck to player
                    ArrayList<String> cardsToSend = new ArrayList<String>();

                    cardsToSend.add(cards.pop());
                    cardsToSend.add(cards.pop());

                    for (String cardname : cardsToSend) {

                        writers.get(turn).println("card" + " " + cardname);
                    }

                    //retrieve exchanged cards from player
                    List<String> cardsToReturn = new ArrayList<String>();

                    cardsToReturn.add(readers.get(turn).readLine());
                    cardsToReturn.add(readers.get(turn).readLine());

                    //push cards back to stack
                    for (String cardname : cardsToReturn) {
                        cards.push(cardname);
                    }

                    Collections.shuffle(cards);

                    System.out.println("Cards in deck: "+cards.size());

                    lastAction = "exchange";
                    actionperformed=true;


                }
                if(input.startsWith("assassinate")){

                    String[] split = input.split(" ");

                    //send message to player to attack
                    toPlayer.get(split[2]).println(input);

                    //retrieve card from attacked player

                    String cardToReturn = fromPlayer.get(split[2]).readLine();

                    //retrieve last card from player
                    if(cardToReturn.startsWith("lastcard")){
                        String[] last = cardToReturn.split(" ");
                        cards.push(last[1]);

                        //force player to loose game
                        toPlayer.get(split[2]).println("lose");


                        //inform other players
                        for (int i = 0; i < avaiable; i++) {
                            if (writers.get(i).equals(toPlayer.get(split[2])))
                                continue;
                            writers.get(i).println("lostgame"+" "+split[2]);
                        }


                        BufferedReader tmp = readers.get(turn);

                        //remove player
                        writers.remove(toPlayer.get(split[2]));
                        readers.remove(fromPlayer.get(split[2]));
                        names.remove(split[2]);

                        turn=readers.indexOf(tmp);

                        avaiable--;

                        //last player wins
                        if(avaiable==1){
                            writers.get(0).println("win");
                            break;
                        }

                    }
                    //player blocked assassinate
                    else if(cardToReturn.startsWith("block")){
                        for (int i = 0; i < avaiable; i++) {
                            if (writers.get(i).equals(toPlayer.get(split[2])))
                                continue;
                            writers.get(i).println("block"+" "+split[2]+" "+split[1]+" "+"assassinate");
                        }
                    }
                    //retrieve card and push back to stack
                    else {
                        cards.push(cardToReturn);
                        //inform players
                        for (int i = 0; i < avaiable; i++) {
                            if (writers.get(i).equals(toPlayer.get(split[2])))
                                continue;
                            if(writers.get(i).equals(writers.get(turn)))
                                writers.get(turn).println(input);
                            else
                                writers.get(i).println("loose card"+" "+split[2]);
                        }

                    }

                    System.out.println("card: "+cardToReturn+" returned to stack");

                    lastAction = "assassinate";
                    actionperformed=true;



                }
                if(input.startsWith("losecard")){
                    for(int i=0;i<avaiable;i++){
                        if(i==turn)
                            continue;
                        writers.get(i).println(input);
                    }
                }

                if(input.startsWith("challenge")){

                    // get last player
                    int last = turn - 1;

                    if(last<0)
                        last = readers.size()-1;


                    //pass message to other players
                    for (int i = 0; i < avaiable; i++) {
                        if (i == turn)
                            continue;
                        writers.get(i).println(input+" "+fromPlayerName.get(readers.get(last))+" "+lastAction);
                    }


                    input = readers.get(last).readLine();

                    if (input.startsWith("show card")){
                        for (int i = 0; i < avaiable; i++) {
                            if (i == last)
                                continue;
                            writers.get(i).println(input);
                        }

                    }
                    else {

                        if(input.startsWith("losecard")){



                            String cardToReturn = readers.get(last).readLine();

                            if(cardToReturn.startsWith("lastcard")){
                                String[] second = cardToReturn.split(" ");
                                cards.push(second[1]);

                                //force player to loose game
                                writers.get(last).println("lose");
                            }
                            else
                                cards.push(cardToReturn);

                            for (int i = 0; i < avaiable; i++) {
                                if (i == last)
                                    continue;
                                writers.get(i).println(input);
                            }

                            System.out.println("Card: "+cardToReturn+" returned back to stack");


                        }

                    }







                }





                if(input.startsWith("coup")){


                    String[] split = input.split(" ");

                    //inform other players
                    for (int i = 0; i < avaiable; i++) {
                        if (i == turn)
                            continue;
                        writers.get(i).println(input);
                    }

                    //retrieve card from attacked player

                    String cardToReturn = fromPlayer.get(split[2]).readLine();

                    //last card
                    if(cardToReturn.startsWith("lastcard")){
                        String[] last = cardToReturn.split(" ");
                        cards.push(last[1]);

                        //force player to loose game
                        toPlayer.get(split[2]).println("lose");

                        //inform other players
                        for (PrintWriter writer:writers) {
                            if (writer.equals(toPlayer.get(split[2])))
                                continue;
                            writer.println("lostgame"+" "+split[2]);
                        }

                        BufferedReader tmp = readers.get(turn);

                        //remove player
                        writers.remove(toPlayer.get(split[2]));
                        readers.remove(fromPlayer.get(split[2]));
                        names.remove(split[2]);

                        turn=readers.indexOf(tmp);

                        avaiable--;

                        //last player wins
                        if(avaiable==1){
                            writers.get(0).println("win");
                            break;
                        }
                    }
                    //retrieve card from couped player
                    else{
                        cards.push(cardToReturn);
                        for (PrintWriter writer : writers) {
                            if (writer.equals(toPlayer.get(split[2])))
                                continue;
                            writer.println("loose card"+" "+split[2]);
                        }

                    }

                    System.out.println("card: "+cardToReturn+" returned to stack");

                    lastAction="coup";
                    actionperformed=true;

                }

                if(input.startsWith("sCheat")){

                    String[] split = input.split(" ");

                    toPlayer.get(split[2]).println(input);

                    String msg = fromPlayer.get(split[2]).readLine();

                    if(msg.startsWith("sRight")){

                        for (PrintWriter writer : writers) {
                            if (writer.equals(toPlayer.get(split[2])))
                                continue;
                            writer.println(msg);
                        }

                    }
                    else {

                        for (PrintWriter writer : writers) {
                            if (writer.equals(toPlayer.get(split[2])))
                                continue;
                            writer.println(msg);
                        }

                        lastAction="suspectCheat";
                        actionperformed=true;


                    }



                }





            }


            System.out.println("Game finished!");




        }
        catch (IOException e){
            e.printStackTrace();


            if(avaiable==1)
                writers.get(0).println("win");


        }




    }

    private void setUpCards(Stack<String> stack){
        stack.add("contessa");
        stack.add("contessa");
        stack.add("contessa");
        stack.add("duke");
        stack.add("duke");
        stack.add("duke");
        stack.add("ambassador");
        stack.add("ambassador");
        stack.add("ambassador");
        stack.add("captain");
        stack.add("captain");
        stack.add("captain");
        stack.add("assassin");
        stack.add("assassin");
        stack.add("assassin");
    }


}


