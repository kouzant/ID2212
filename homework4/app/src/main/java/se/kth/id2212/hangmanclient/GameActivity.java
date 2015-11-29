package se.kth.id2212.hangmanclient;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import se.kth.id2212.hangmanclient.connection.ServerConnection;
import se.kth.id2212.hangmanclient.helpers.CommunicationStatus;
import se.kth.id2212.hangmanclient.helpers.InfoMessage;
import se.kth.id2212.hangmanclient.helpers.OnServerReply;
import se.kth.id2212.hangmanclient.helpers.Request;

public class GameActivity extends AppCompatActivity implements OnServerReply {

    @Bind(R.id.guessButton)
    Button guessButton;

    @Bind(R.id.newGameButton)
    Button newGaButton;

    @Bind(R.id.backButton)
    Button backButton;

    @Bind(R.id.wordLabel)
    TextView wordLabel;

    @Bind(R.id.usedLettersLabel)
    TextView usedLettersLabel;

    @Bind(R.id.informationLabel)
    TextView informationLabel;

    @Bind(R.id.scoresLabel)
    TextView scoresLabel;

    @Bind(R.id.userInput)
    EditText userInput;

    private ServerConnection connection;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        Intent intent = this.getIntent();
        result = intent.getStringExtra("result");
        parseGameResult(result);

        connection = ServerConnection.getInstance();
        connection.setListener(this);
    }

    @OnClick(R.id.backButton)
    void backButtonClicked() {
        //To-Do close connection
        Request req = new Request(0);
        connection.sendToServer(req.getJson().toString());
        ServerConnection.closeConnection();
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.newGameButton)
    void newGameButtonClicked() {
        Request req = new Request();
        connection.sendToServer(req.getJson().toString());
    }

    @OnClick(R.id.guessButton)
    void guessButtonClicked() {
        String toSend = userInput.getText().toString();
        userInput.setText("");
        if (!usedLettersLabel.getText().toString().contains(toSend)) {
            Request req = new Request(toSend);
            connection.sendToServer(req.getJson().toString());
        }
    }

    @Override
    public void onServerReply(String result) {
        parseGameResult(result);
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


    void parseGameResult(String txt) {
        result = txt;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                String spaced;
                JSONArray scores;
                JSONTokener tokener = new JSONTokener(result);
                JSONObject json = null;
                SpannableString text;

                try {
                    json = new JSONObject(tokener);

                    switch ((String) json.get("status")) {

                        case CommunicationStatus.GAME_WON:
                            informationLabel.setText(InfoMessage.GAME_WON);
                            guessButton.setEnabled(false);
                            //game won
                            spaced = ((String) json.get("word")).replace("", " ").trim();
                            wordLabel.setText("Word: " + spaced);

                            text = new SpannableString((json.get("letter")) + " ");
                            text.setSpan(new ForegroundColorSpan(Color.GREEN), 0, 1, 0);
                            usedLettersLabel.append(text);

                            scores = (JSONArray) json.get("score");
                            scoresLabel.setText("Total Score: " + scores.get(0) + "  Won: " + scores.get(1) + "  Lost: " + scores.get(2));
                            break;
                        case CommunicationStatus.GAME_LOST:
                            informationLabel.setText(InfoMessage.GAME_LOST);
                            //game lost
                            spaced = ((String) json.get("word")).replace("", " ").trim();
                            wordLabel.setText("Word: " + spaced);
                            guessButton.setEnabled(false);

                            text = new SpannableString((json.get("letter")) + " ");
                            text.setSpan(new ForegroundColorSpan(Color.RED), 0, 1, 0);
                            usedLettersLabel.append(text);

                            scores = (JSONArray) json.get("score");
                            scoresLabel.setText("Total Score: " + scores.get(0) + "  Won: " + scores.get(1) + "  Lost: " + scores.get(2));
                            break;
                        case CommunicationStatus.WRONG_LETTER:
                            informationLabel.setText(InfoMessage.GUESS_WRONG + InfoMessage.NUMBER_OF_LIFES + json.get("tries"));
                            // wrong letter
                            text = new SpannableString((json.get("letter")) + " ");
                            text.setSpan(new ForegroundColorSpan(Color.RED), 0, 1, 0);
                            usedLettersLabel.append(text);

                            break;
                        case CommunicationStatus.WRONG_GUESS:
                            informationLabel.setText(InfoMessage.GUESS_WRONG + InfoMessage.NUMBER_OF_LIFES + json.get("tries"));
                            // wrong guess
                            break;
                        case CommunicationStatus.CORRECT_LETTER:
                            informationLabel.setText(InfoMessage.GUESS_CORRECT + InfoMessage.NUMBER_OF_LIFES + json.get("tries"));
                            //correct letter
                            spaced = ((String) json.get("word")).replace("", " ").trim();
                            wordLabel.setText("Hint: " + spaced);

                            text = new SpannableString((json.get("letter")) + " ");
                            text.setSpan(new ForegroundColorSpan(Color.GREEN), 0, 1, 0);
                            usedLettersLabel.append(text);

                            break;
                        case CommunicationStatus.NEW_GAME:
                            guessButton.setEnabled(true);
                            usedLettersLabel.setText("");

                            informationLabel.setText(InfoMessage.GAME_STARTED + InfoMessage.NUMBER_OF_LIFES + json.get("tries"));
                            spaced = ((String) json.get("word")).replace("", " ").trim();
                            wordLabel.setText("Hint: " + spaced);
                            scores = (JSONArray) json.get("score");
                            scoresLabel.setText("Total Score: " + scores.get(0) + "  Won: " + scores.get(1) + "  Lost: " + scores.get(2));

                            break;
                        case CommunicationStatus.END_GAME:
                            Intent intent = new Intent(getApplication(), WelcomeActivity.class);
                            startActivity(intent);
                            break;
                        case CommunicationStatus.UNKNOWN:
                        default:
                            // unknown message
                            informationLabel.setText(InfoMessage.UNKNOWN_MSG);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
