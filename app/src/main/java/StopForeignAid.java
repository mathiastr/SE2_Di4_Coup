public class StopForeignAid {
    Player player;
    Action preAction;

    public StopForeignAid(Player player, Action preAction){
        this.player = player;
        this.preAction = preAction;
    }

    @Override
    public void playReaction(){
        //checken ob der Spieler diesen Zug ausführen darf
        //Konsequenzen des Zuges (zB. Anzahl coins einer Person ändern, den View ändern)
    }
}
