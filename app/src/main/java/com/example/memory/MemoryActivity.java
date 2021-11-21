package com.example.memory;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MemoryActivity extends AppCompatActivity {



    int [] pics;
    Playground field;
    LinearLayout gameField;
    Position previousCard;
    ImageButton[][] buttons;
    private int foundPairs = 0;
    Position size = new Position(8,4);
    boolean denieSelection = false;
    private TextView playerOneScore;
    private TextView playerTwoScore;
    private boolean curPlayer = true;
    final private int cardFound = 10;
    final private int cardPunish = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pics = getPicsArray();
        gameField = (LinearLayout) findViewById(R.id.playingfield);

        createGame();

        generateGrid(size.y, size.x);

        playerOneScore = (TextView) findViewById(R.id.player1);
        playerTwoScore = (TextView) findViewById(R.id.player2);


    }

    public static int[] getPicsArray() {
        int[] c = new int[32];

        c[0] = R.drawable.i000;
        c[1] = R.drawable.i001;
        c[2] = R.drawable.i002;
        c[3] = R.drawable.i003;
        c[4] = R.drawable.i004;
        c[5] = R.drawable.i005;
        c[6] = R.drawable.i006;
        c[7] = R.drawable.i007;
        c[8] = R.drawable.i008;
        c[9] = R.drawable.i009;
        c[10] = R.drawable.i010;
        c[11] = R.drawable.i011;
        c[12] = R.drawable.i012;
        c[13] = R.drawable.i013;
        c[14] = R.drawable.i014;
        c[15] = R.drawable.i015;
        c[16] = R.drawable.i016;
        c[17] = R.drawable.i017;
        c[18] = R.drawable.i018;
        c[19] = R.drawable.i019;
        c[20] = R.drawable.i020;
        c[21] = R.drawable.i021;
        c[22] = R.drawable.i022;
        c[23] = R.drawable.i023;
        c[24] = R.drawable.i024;
        c[25] = R.drawable.i025;
        c[26] = R.drawable.i026;
        c[27] = R.drawable.i027;
        c[28] = R.drawable.i028;
        c[29] = R.drawable.i029;
        c[30] = R.drawable.i030;
        c[31] = R.drawable.i031;
        return c;
    }


    public void createGame() {
        field = new Playground(size.x,size.y);
        buttons = new ImageButton[size.x][size.y];
        for(int i = 0; i < buttons.length; i++){
            for(int j = 0; j < buttons[0].length; j++){
                buttons[i][j] = generateButton(new Position(i, j));
            }
        }
    }

    private void generateGrid(int nrCols, int nrRows){
        for(int i = 0; i < nrRows; i++){
            gameField.addView(generateAndAddRows(i, nrCols));
        }
    }

    //Bauen der Matrix (Spalten)

    private LinearLayout generateAndAddRows(int row, int nrCols){
        LinearLayout layout = new LinearLayout(this);


//
        layout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        layout.setGravity(Gravity.CENTER);

        for(int i = 0; i < nrCols; i++){

            try{
                layout.addView(buttons[row][i]);
            }catch(ArrayIndexOutOfBoundsException e){

            }
        }

        return layout;
    }

    private ImageButton generateButton(Position pos){

            ImageButton b = new ImageButton(this);
            b.setImageResource(R.drawable.back);
            b.setTag(pos);
            b.setForegroundGravity(Gravity.CENTER);

            b.setLayoutParams(new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onButtonClick(view);
                }
            });
            ViewGroup.LayoutParams params = b.getLayoutParams();

            params.height = 250;
            params.width = 200;

            return b;
    }

    private void closeCards(Position pos1, Position pos2)
    {
        class CloseTask extends TimerTask
        {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    buttons[pos1.x][pos1.y].setImageResource(R.drawable.back);
                    buttons[pos2.x][pos2.y].setImageResource(R.drawable.back);
                });
            }
        }

        Timer timer = new Timer();
        timer.schedule(new CloseTask(),1000);
    }

    public void onButtonClick(View view) { //--> aus view du können holen imagebutton

        if(denieSelection)
            return;

        ImageButton button =  (ImageButton) view;
        Position pos = null;

        for(int i = 0; i < buttons.length; i++){
            for(int j = 0; j < buttons[i].length; j++){
                if(buttons[i][j] == button){
                    pos = new Position(i, j);
                    break;
                }
            }
        }

        if(pos==null)
            return;

        if(field.getCard(pos).isVisible())
            return;

        button.setImageResource(field.getCard(pos).getValue());

        if(previousCard != null){
            denieSelection = true;
            if(field.isPair(pos, previousCard)) {
                foundPairs++;
                setCurScore(true, curPlayer);
                field.getCard(pos).setVisible(true);
                field.getCard(previousCard).setVisible(true);
                if(foundPairs >= ((buttons.length*buttons[0].length)/2)){
                    if(field.finished()){
                        winningScreen(gameField);
                    }
                }
                denieSelection = false;
                previousCard = null;
            } else {
                curPlayer = !curPlayer;
                setCurScore(false, curPlayer);
                closeCards(previousCard, pos);
                previousCard = null;
                denieSelection = false;
            }
        } else {
            previousCard = pos;
        }
    }

    public void scoreRefresh(){

    }

    public void winningScreen(View view ){
         int p1 = Integer.parseInt(playerOneScore.getText().toString());
         int p2 = Integer.parseInt(playerTwoScore.getText().toString());

        if(p1 > p2) {
            Snackbar.make(view, "Spieler eins hat gewonnen ", Snackbar.LENGTH_LONG).show();
        } else if(p1 == p2){
            Snackbar.make(view, "Es ist ein Unentschieden", Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(view, "Spieler zwei hat gewonnen", Snackbar.LENGTH_LONG).show();
        }
    }

    public void setCurScore(boolean pairIsFound, boolean player){
        TextView curPlayer;
        if(player == true){
            curPlayer = playerOneScore;
        } else {
            curPlayer = playerTwoScore;
        }

        int curScore = Integer.parseInt(curPlayer.getText().toString());


        if(pairIsFound == true){
            curScore += cardFound;
        } else {
            curScore -= cardPunish;
        }
        curPlayer.setText(String.valueOf(curScore));
    }
}