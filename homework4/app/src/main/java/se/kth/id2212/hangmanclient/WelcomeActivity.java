package se.kth.id2212.hangmanclient;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import se.kth.id2212.hangmanclient.connection.ServerConnection;
import se.kth.id2212.hangmanclient.helpers.OnServerReply;

public class WelcomeActivity extends AppCompatActivity implements OnServerReply {
    private ServerConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_welcome, menu);
        return true;
    }

    @OnClick(R.id.startButton) void onClick() {

        connection = ServerConnection.getInstance().getInstance();
        connection.setListener(this);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        String server_url = settings.getString("server_url", "localhost");
        int server_port = Integer.parseInt(settings.getString("server_port", "4444"));

        connection.setServerIp(server_url);
        connection.setServerPort(server_port);
        System.out.println("!!!CONNECTING:" + server_url + ":" + server_port);
        new Thread(connection).start();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onServerReply(String result) {

        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("result", result);
        startActivity(intent);

    }

    @Override
    public void onError(final String error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(
                        getBaseContext(),
                        error,
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
    }
}
