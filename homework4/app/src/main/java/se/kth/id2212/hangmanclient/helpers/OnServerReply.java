package se.kth.id2212.hangmanclient.helpers;

import org.json.JSONObject;

/**
 * Created by Anestos on 29/11/2015.
 */
public interface OnServerReply {
    void onServerReply(String result);
    void onError(String error);
}
