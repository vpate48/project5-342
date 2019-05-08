package sample;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.lang.Math;

public abstract class NetworkConnection {

    private ConnThread connthread = new ConnThread();
    private Consumer<Serializable> callback;
    //ArrayList
    ArrayList<player> players;
    ArrayList<ClientThread> array;
    Gameplay game;
    boolean checking;
    int guessesPlayers;
    int amountPlayers;
    boolean clientOne, clientTwo;
    String dataone, datatwo;
    int playeronePt = 0, playertwoPt = 0;

    public NetworkConnection(Consumer<Serializable> callback) {
        this.callback = callback;
        connthread.setDaemon(true);
        guessesPlayers = 0;
        amountPlayers = 0;
        Gameplay game = new Gameplay();
        array = new ArrayList<ClientThread>();
        players = new ArrayList<player>();
        clientOne = false; clientTwo = false;
    }

    public void startConn() throws Exception{
        connthread.start();
    }


    public void sendMessage(Serializable data){
        array.forEach(t -> {
            try {
                t.tout.writeObject(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    public void setPlayersPoints(player a,int b){
        for(int i = 0; i < players.size(); i ++){
            if(a.returnName().equals(players.get(i).returnName())){
               players.get(i).setPlayerPoints(b);
            }
        }
    }

    public void getPlayersPoints(){
        for(int i = 0; i < players.size(); i ++){
               sendMessage(players.get(i).returnName() + " Points: " + players.get(i).getPlayerPoints());
            }
    }
    
    public int getIndex(player a){
        for(int i = 0; i < players.size(); i ++) {
            if (a.returnName().equals(players.get(i).returnName())) {
                return i;
            }
        }
        return 0;
    }

    public void closeConn() throws Exception{
        connthread.socket.close();
    }

    abstract protected boolean isServer();
    abstract protected String getIP();
    abstract protected int getPort();

    class ConnThread extends Thread{
        private Socket socket;
        private ObjectOutputStream out;
        public void run() {
            int num = 1;

            try{
                ServerSocket server = new ServerSocket(getPort());
                while (true) {
                    ClientThread t1 = new ClientThread(server.accept(),num);
                    num++;
                    array.add(t1);
                    t1.start();
                }
            }
            catch(Exception e) {
            }

        }
    }

    public class ClientThread extends Thread{
        int num;
        player a = new player();
        Socket socket;
        ObjectInputStream tin;
        ObjectOutputStream tout;

        ClientThread(Socket a, int n){
            this.socket = a;
            this.num = n;
        }


        public void run() {
            String currently = "";

            try( ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
                socket.setTcpNoDelay(true);
                this.tout = out;
                this.tin = in;
                amountPlayers++;
                tout.writeObject("Welcome! Amount of players join:" + num );

                while(true){

                    Serializable data = (Serializable) in.readObject();

                    String string =  data.toString();
                    String youString = "";
                    int spacePos = string.indexOf(" ");
                    if (spacePos > 0) {
                        youString= string.substring(0, spacePos);
                    }

                    System.out.println(youString);
                    if(youString.equals("Join")){
                        int inUse = 0;
                        String tempWord = youString + " ";
                        string = string.replaceAll(tempWord, "");
                        tempWord = " " + youString;
                        string = string.replaceAll(tempWord, "");
                        String b = "";
                        for(int i = 0; i < players.size(); i ++){
                            if(players.get(i).returnName().equals(string)){
                                inUse++;
                            }
                        }
                        if(inUse > 0)
                        {
                            tout.writeObject("That user name is currently in use.\n So we are going call you by " + string + " " + inUse);
                            a.setName(string + " " + inUse);
                        }
                        else
                            a.setName(string);

                        if(amountPlayers > 4 ){
                            a.updateClientStatus(false);
                            tout.writeObject("You were not able to join the game! Game is full! ");
                        }
                        else{
                            a.updateClientStatus(true);
                            tout.writeObject("You have sucessfully joined the game!");
                        }

                        /*player a = new player();
                        players.add(a);
                        players.get(num-1).setName(string + b);
                        players.get(num-1).setId(num);*/
                            players.add(a);
                            sendMessage("Number of players currently in game: " + amountPlayers+ "/4");
                            currently = " is connected";

                            if(amountPlayers == 4){
                                sendMessage("Game is starting since we have 4 players!\nAll players have 5 guesses! Guess on spot you get 10 points.\n Guess on 5 tries get 5 points, 4 tries 4 points, etc");
                                game.generateNumber();
                                sendMessage("Random Number has been generated. Good luck");
                            }


                    }
                    else if(youString.equals("Text")){
                        String tempWord = youString + " ";
                        string = string.replaceAll(tempWord, "");
                        tempWord = " " + youString;
                        string = string.replaceAll(tempWord, "");
                        //sendMessage(players.get(num-1).returnName() + ": " + string);
                        sendMessage(a.returnName() + ": "+ string);
                        currently = " has sent a text chat: " + string;
                    }
                    else if(youString.equals("Play")){

                        if(amountPlayers == 4) {
                            String tempWord = youString + " ";
                            string = string.replaceAll(tempWord, "");
                            tempWord = " " + youString;
                            string = string.replaceAll(tempWord, "");
                            int result = Integer.parseInt(string);
                            if(a.getGuesses() > 0 && a.getClientStatus() == true){
                                Boolean winorlose = game.winOrLose(result);
                                if(winorlose == true && a.getGuesses() == 5){
                                    sendMessage(a.returnName() + " has guessed SPOT on! 10 points for him");
                                    a.setGuesses(5);
                                    a.setPlayerPoints(10);
                                    setPlayersPoints(a,10);
                                }
                                if(winorlose == true){
                                    sendMessage(a.returnName() + " has guessed! "+ a.getGuesses() + " points for the player!");
                                    a.setGuesses(5);
                                    a.setPlayerPoints(a.getGuesses());
                                    setPlayersPoints(a,a.getGuesses());
                                }
                                else{
                                    String howcloseYou = game.howClose(result);
                                    tout.writeObject(howcloseYou);
                                    a.deleteGuess();
                                    if(a.getGuesses() !=0 ) {
                                        sendMessage(a.returnName() + " has guessed " + result + " and was off. The player has\n " + a.getGuesses() + " guesses left");
                                    }
                                    else{
                                        sendMessage(a.returnName() + " has run out of guesses!");
                                        a.updateClientStatus(false);
                                        guessesPlayers++;
                                        if(guessesPlayers == 4){
                                            sendMessage("Round finished! These are all the current points for all players\n To play again click the Play again button!");
                                            getPlayersPoints();
                                            guessesPlayers = 0;
                                            amountPlayers= 0;

                                        }
                                    }

                                }
                            }
                            else{
                                tout.writeObject("You can't guess anymore!");
                            }


                        }
                        else {
                            sendMessage("Player " + a.returnName() + " you can't guess a number currently\n due to there being " + amountPlayers + " players in. You need 4 players to play.");
                            currently = " tried to send a number guess";
                        }
                    }

                    else if(youString.equals("PlayAgain")){
                        if(amountPlayers <= 4){
                            a.updateClientStatus(true);
                            a.setGuesses(5);
                            amountPlayers++;
                            sendMessage("Player " + a.returnName() + " has joined the next round");
                        }
                        else
                            tout.writeObject("Game is currently full");
                    }


                    //callback.accept( players.get(num-1).returnName() + " is connected");
                    callback.accept( a.returnName() + currently);
                    //send(data);
                }
            }
            catch(Exception e){
                callback.accept( a.returnName() + " has quit");
                sendMessage(a.returnName() + " has left the game");
                amountPlayers--;
                sendMessage(amountPlayers + " players are currently waiting to play a game");
            }
        }
    }


    public class player {

        private String name;
        private int id;
        private int guesses;
        private boolean won;
        private boolean clientStatus;
        private String playerData;
        private int playerPoints = 0;

        player(){// defualt constructor
            clientStatus = false;
            playerPoints = 0;
        }

        public void setName(String i){
            name = i;
        }

        public String returnName(){
            return name;
        }


        public String getPlayerData(){
            return playerData;
        }

        public boolean getWon() {return won;}

        public void setWon(boolean w){ won = w;}

        public int getGuesses(){ return guesses;}

        public void deleteGuess(){ guesses= guesses-1;}

        public void setGuesses(int g){ guesses = g;}

        public void setPlayerData(String s){
            playerData = s;
        }

        public int getPlayerPoints(){
            return playerPoints;
        }

        public void setPlayerPoints(int p){
            playerPoints += p;
        }

        public boolean getClientStatus(){
            return clientStatus;
        }

        public void resetPoints(){
            playerPoints = 0;
        }


        public void updateClientStatus(boolean a){
            clientStatus = a;
        }

        public void setId(int i){
            id =i;
        }

        public int returnId(){
            return id;
        }
    }

    public class Gameplay{
        private int guessNumber;

        public int generateNumber(){// generation of random number
            double temp = Math.random();//generates number in double form
            temp = temp * 999;// generates number in range
            int number = (int) temp;// makes number an int
            return number;
        }

        public boolean winOrLose(int guess){
            if(guess == guessNumber)
                return true;
            else
                return false;
        }

        public String howClose(int guess){
            if(guess > guessNumber){
                return "Your guess "+ guess + " is high";
            }
            else
                return "Your guess" + guess + " is low";
        }


    }

}


