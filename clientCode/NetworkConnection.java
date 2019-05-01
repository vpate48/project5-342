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
    private String dataof;

    public NetworkConnection(Consumer<Serializable> callback) {
        this.callback = callback;
        connthread.setDaemon(true);
    }

    public void setCallback(Consumer<Serializable> callback){
        this.callback = callback;
    }

    public void startConn() throws Exception{
        connthread.start();
    }


    public void send(Serializable data) throws Exception{
        connthread.out.writeObject(data);
    }




    public void sendWelcome(Serializable data) throws Exception{
        connthread.out.writeObject(data);
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

            try{
                Socket socket = new Socket(getIP(),getPort());
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

                this.socket = socket;
                this.out = out;
                socket.setTcpNoDelay(true);

                while(true){
                    Serializable data = (Serializable) in.readObject();
                    callback.accept(data);
                }
            }
            catch(Exception e) {
                callback.accept("Disconnected from server \n Server may not be up!");
            }
        }
    }


}


