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
    boolean clientOne, clientTwo;
    String dataone, datatwo;
    int playeronePt=0, playertwoPt=0;





    public NetworkConnection(Consumer<Serializable> callback) {
        this.callback = callback;
        connthread.setDaemon(true);
        array = new ArrayList<ClientThread>();
        players = new ArrayList<player>();
        clientOne = false; clientTwo = false;
    }

    public void startConn() throws Exception{
        connthread.start();
    }



    public void send(Serializable data) throws Exception{



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
        Socket socket;
        ObjectInputStream tin;
        ObjectOutputStream tout;

        ClientThread(Socket a, int n){
            this.socket = a;
            this.num = n;
        }


        public void run() {

            try( ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
                socket.setTcpNoDelay(true);
                this.tout = out;
                this.tin = in;
                tout.writeObject("Welcome! Amount of players join:" + num );

                while(true){

                    Serializable data = (Serializable) in.readObject();

                    String string =  data.toString();
                    String youString = "";
                    int spacePos = string.indexOf(" ");
                    if (spacePos > 0) {
                        youString= string.substring(0, spacePos);
                    }

                    if(youString.equals("Join")){

                        String tempWord = youString + " ";
                        string = string.replaceAll(tempWord, "");
                        tempWord = " " + youString;
                        string = string.replaceAll(tempWord, "");
                        for(int i = 0; i < players.size(); i ++){
                            if(players.get(i).returnName().equals(string)){
                                tout.writeObject("User name currently in use");
                                return;
                            }
                        }
                        player a = new player();
                        players.add(a);
                        players.get(num-1).setName(string);
                        players.get(num-1).setId(num);


                    }


                    callback.accept("Player " + num + "\n" + players.get(num-1).returnName());
                    //send(data);
                }
            }
            catch(Exception e){
                callback.accept("Player " + num + "\n" + players.get(num-1).returnName() + " has quit");
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


