import java.util.Collections;
import java.util.Stack;

public class Test {


    public static void main(String[] args) {

        Stack<String> cards = new Stack<String>();


        cards.add("one");
        cards.add("two");
        cards.add("three");
        cards.add("four");


        Collections.shuffle(cards);


        for(String s:cards)
            System.out.println(s);



    }
}
