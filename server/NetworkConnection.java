package sample;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

public abstract class NetworkConnection {

    private ConnThread connthread = new ConnThread();
    private Consumer<Serializable> callback;
    //ArrayList
    ArrayList<player> players;
    ArrayList<ClientThread> array;
    int amountPlayers;
    boolean clientOne, clientTwo;
    String dataone, datatwo;
    int playeronePt=0, playertwoPt=0;





    public NetworkConnection(Consumer<Serializable> callback) {
        this.callback = callback;
        connthread.setDaemon(true);
        amountPlayers = 0;
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
                        boolean inUse = false;
                        String tempWord = youString + " ";
                        string = string.replaceAll(tempWord, "");
                        tempWord = " " + youString;
                        string = string.replaceAll(tempWord, "");
                        String b = "";
                        for(int i = 0; i < players.size(); i ++){
                            if(players.get(i).returnName().equals(string)){
                                inUse = true;
                            }
                        }
                        if(inUse == true)
                        {
                            tout.writeObject("The user name is currently in use.");
                        }
                        /*player a = new player();
                        players.add(a);
                        players.get(num-1).setName(string + b);
                        players.get(num-1).setId(num);*/
                        else {
                            a.setName(string);
                            players.add(a);
                            sendMessage("Number of players currently in: " + amountPlayers);
                            currently = " is connected";
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
                            currently = " has sent a number guess: " + string;
                        }
                        else {
                            sendMessage("Player " + a.returnName() + " you can't guess a number currently\n due to there being " + amountPlayers + " players in. You need 4 players to play.");
                            currently = " tried to send a number guess";
                        }
                    }


                    //callback.accept( players.get(num-1).returnName() + " is connected");
                    callback.accept( a.returnName() + currently);
                    //send(data);
                }
            }
            catch(Exception e){
                callback.accept( a.returnName() + " has quit");
                amountPlayers--;
            }
        }
    }


    public class player {

        private String name;
        private int id;
        private boolean clientStatus;
        private String playerData;
        private int playerPoints;

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


}


