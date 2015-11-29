package se.kth.id2212.hangmanclient.helpers;

import org.json.JSONException;
import org.json.JSONObject;

import se.kth.id2212.hangmanclient.helpers.CommunicationStatus;

/**
 * Created by Anestos on 29/11/2015.
 */
public class Request {
    private final JSONObject json;

    public Request(String toSend) {
        this.json = new JSONObject();
        try {
            this.json.put("status", CommunicationStatus.GUESSING);
            if (toSend.length() > 1) {
                this.json.put("guess", toSend);
                this.json.put("letter", "");
            } else {
                this.json.put("guess", "");
                this.json.put("letter", toSend);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public Request(Integer i) {
        this.json = new JSONObject();
        try {
            this.json.put("status", CommunicationStatus.END_GAME);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Request() {
        this.json = new JSONObject();
        try {
            this.json.put("status", CommunicationStatus.NEW_GAME);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public JSONObject getJson(){
        return json;
    }
}
