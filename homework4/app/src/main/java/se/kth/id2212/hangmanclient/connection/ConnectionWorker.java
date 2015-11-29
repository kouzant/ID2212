package se.kth.id2212.hangmanclient.connection;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Arrays;

import se.kth.id2212.hangmanclient.helpers.InfoMessage;
import se.kth.id2212.hangmanclient.helpers.OnServerReply;

/**
 * Created by Anestos on 29/11/2015.
 */

    class ConnectionWorker implements Runnable {

    private final BufferedInputStream in;
    private final BufferedOutputStream out;
    private String text;

    private OnServerReply listener;

    public ConnectionWorker(String text, BufferedInputStream in, BufferedOutputStream out, OnServerReply listener) {
        this.text = text;
        this.in=in;
        this.out=out;
        this.listener = listener;
    }


    @Override
    public void run() {
        String result;
        try {
            byte[] toServer = text.getBytes();
            out.write(toServer, 0, toServer.length);
            out.flush();


            byte[] msg = new byte[4096];
            int bytesRead = 0;
            int n;
            while ((n = in.read(msg, bytesRead, 256)) != -1) {
                bytesRead += n;
                if (bytesRead == 4096) {
                    break;
                }
                if (in.available() == 0) {
                    break;
                }
            }


            String input = new String(Arrays.copyOfRange(msg, 0, bytesRead));
            result = input;


        } catch (IOException e) {
            listener.onError(InfoMessage.FAILED_TO_SEND_MSG + e.getMessage());
            result = InfoMessage.FAILED_TO_SEND_MSG + e.getMessage();
        }

        listener.onServerReply(result);

    }
}

