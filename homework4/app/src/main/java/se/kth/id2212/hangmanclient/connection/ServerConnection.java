package se.kth.id2212.hangmanclient.connection;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import se.kth.id2212.hangmanclient.helpers.InfoMessage;
import se.kth.id2212.hangmanclient.helpers.OnServerReply;
import se.kth.id2212.hangmanclient.helpers.Request;

/**
 * Created by Anestos on 29/11/2015.
 */
public class ServerConnection implements Runnable  {
    private static int SERVER_PORT;
    private static String SERVER_IP;
    private static Socket clientSocket;

    private OnServerReply listener;

    private static BufferedInputStream in;
    private static BufferedOutputStream out;
    private static ServerConnection instance;

    private ServerConnection(){

    }

    public static void closeConnection(){
        try {
            out.flush();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        instance = null;
    }


    public static ServerConnection getInstance(){
        if (instance == null) {
            // Create the instance
            instance = new ServerConnection();
        }
        return instance;
    }



    public void setServerPort(int serverPort){
        this.SERVER_PORT = serverPort;
    }

    public void setServerIp(String serverIp){
        this.SERVER_IP = serverIp;
    }

    public void setListener(OnServerReply listener){
        this.listener = listener;
    }






    /**
     * Connects to the server using the host name and port number specified in
     * the constructor.
     */
    void connect() {
        try {
            clientSocket = new Socket(SERVER_IP, SERVER_PORT);
            in = new BufferedInputStream(clientSocket.getInputStream());
            out = new BufferedOutputStream(clientSocket.getOutputStream());
            Request req = new Request();
            sendToServer(req.getJson().toString());

        } catch (UnknownHostException e) {
            listener.onError(InfoMessage.UNKNOWN_HOST + SERVER_IP);
        } catch (IOException e) {
            listener.onError(InfoMessage.NO_IO_CONNECTION + SERVER_IP);
        }
    }

    public void sendToServer(String text) {
        new Thread(new ConnectionWorker(text, in, out, listener)).start();
    }

    @Override
    public void run() {
        connect();
    }
}
