
import java.io.*;
import java.net.Socket;
import java.util.*;

public class PlayerThread extends Thread {


    private List<Socket> players;
    private Map<String, PrintWriter> toPlayer;
    private Map<String, BufferedReader> fromPlayer;
    private List<PrintWriter> writers;
    private List<BufferedReader> readers;


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
                    if (avaiable == 1)
                        throw new IOException();

                    if (turn >= writers.size())
                        turn = 0;

                }

                if (input.equals("next")) {
                    System.out.println("next player");
                    turn++;
                    if (turn >= writers.size())
                        turn = 0;
                    writers.get(turn).println("turn");


                }
                if (input.equals("exit")) {
                    writers.get(turn).println("lose");

                    writers.remove(turn);
                    readers.remove(turn);

                    avaiable--;
                    System.out.println("players avaiable: " + avaiable);
                    if (avaiable == 1) {
                        writers.get(0).println("win");
                        break;
                    }

                }

                if (input.startsWith("income") || input.startsWith("foreignaid" )|| input.startsWith("steal")|| input.startsWith("tax")) {

                    //pass message to other players
                    for (int i = 0; i < avaiable; i++) {
                        if (i == turn)
                            continue;
                        writers.get(i).println(input);
                    }
                }

                if (input.startsWith("exchange")) {
                    /**
                     TODO:
                     get two cards from stack

                     send two card to player (use writers.get(turn).println(cardname))

                     receive exchanged cards (use readers.get(0).readline())

                     push received cards back to stack
                     **/
                    for (int i = 0; i < avaiable; i++) {
                        if (i == turn)
                            continue;
                        writers.get(i).println(input);
                    }

                    ArrayList<String> cardsToSend = new ArrayList<String>();

                    cardsToSend.add(cards.pop());
                    cardsToSend.add(cards.pop());

                    for (String cardname : cardsToSend) {

                        writers.get(turn).println("card" + " " + cardname);
                    }
                    List<String> cardsToReturn = new ArrayList<String>();

                    cardsToReturn.add(readers.get(turn).readLine());
                    cardsToReturn.add(readers.get(turn).readLine());

                    for (String cardname : cardsToReturn) {
                        cards.push(cardname);
                    }
                    Collections.shuffle(cards);


                }
                if(input.startsWith("assassinate")){

                    String[] split = input.split(" ");

                    for (int i = 0; i < avaiable; i++) {
                        if (i == turn)
                            continue;
                        writers.get(i).println(input);
                    }

                    //retrieve card from attacked player

                    String cardToReturn = fromPlayer.get(split[2]).readLine();

                    if(cardToReturn.startsWith("lastcard")){
                        String[] last = cardToReturn.split(" ");
                        cards.push(last[1]);

                        //force player to loose game
                        toPlayer.get(split[2]).println("lose");
                    }
                    else
                        cards.push(cardToReturn);

                    System.out.println("card: "+cardToReturn+" returned to stack");



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

                    for (int i = 0; i < avaiable; i++) {
                        if (i == turn)
                            continue;
                        writers.get(i).println(input);
                    }

                    //retrieve card from attacked player

                    String cardToReturn = fromPlayer.get(split[2]).readLine();

                    if(cardToReturn.startsWith("lastcard")){
                        String[] last = cardToReturn.split(" ");
                        cards.push(last[1]);

                        //force player to loose game
                        toPlayer.get(split[2]).println("lose");
                    }
                    else
                        cards.push(cardToReturn);

                    System.out.println("card: "+cardToReturn+" returned to stack");

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


