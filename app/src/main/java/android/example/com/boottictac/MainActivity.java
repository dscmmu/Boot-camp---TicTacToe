package android.example.com.boottictac;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    //declare two-dimensional array for button views;
    private Button[][] b;
    //declare two-dimensional array for board mock-up
    private int [][] c;
    //this allows the player and computer to interchange
    private boolean player = false;
    //this is an instance of the Ai class created below
    Ai ai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chooseToken();
    }

    public void chooseToken(){
        player = true;
        setBoard();
    }

    private void setBoard(){
        //This creates an instance of the computer class
        ai = new Ai();
        //Arrays in java are classes, hence have to instantiate them
        //This is for the button views created in activity_main.xml
        b = new Button[4][4];
        //This is for the board mock up we declared above
        c = new int [4][4];

        //findViewById for all the button views created
        //begin from [1][1]
        //First row
        b[1][3] = findViewById(R.id.r0c0);
        b[1][2] = findViewById(R.id.r0c1);
        b[1][1] = findViewById(R.id.r0c2);

        //Second row
        b[2][3] = findViewById(R.id.r1c0);
        b[2][2] = findViewById(R.id.r1c1);
        b[2][1] = findViewById(R.id.r1c2);

        //Third row
        b[3][3] = findViewById(R.id.r2c0);
        b[3][2] = findViewById(R.id.r2c1);
        b[3][1] = findViewById(R.id.r2c2);


        // add the click listeners for each button
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                //on clicking any button, you do something. Defined in private class, MyClickListener
                b[i][j].setOnClickListener(new MyClickListener(i, j));
                //If he buttons is not enabled i.e. cannot be clicked
                if(!b[i][j].isEnabled()) {
                    //Put a blank text i.e. no text will appear
                    b[i][j].setText(" ");
                    //Enable the button to be clickable
                    b[i][j].setEnabled(true);
                }
            }
        }

        //for loop to mark every square on the board with a number 2
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++)
                c[i][j] = 2;
        }

        //This displays a toast to instruct the player to begin playing
        Toast.makeText(getApplicationContext(), "Click a button to start.", Toast.LENGTH_LONG).show();
    }

    class MyClickListener implements View.OnClickListener {
        int x, y;

        private MyClickListener(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void onClick(View view) {
            //On clicking the button....
            //If the button is clickable
            if (b[x][y].isEnabled()) {
                //Turn the button clicked to become false so that no one can click it again till game ends
                b[x][y].setEnabled(false);
                //If it is the player's turn i.e. player value is true

                //Put letter X in the button
                b[x][y].setText(R.string.letter_x);
                //Let the color of the text be #673ab8
                b[x][y].setTextColor(Color.parseColor("#673ab8"));


                //computer's turn

                //Set the mock-up board to equal 0, if the player has clicked
                c[x][y] = 0;

                //If the player has not made the winning move, then it's the computer's turn
                if (!checkBoard()) {
                    ai.takeTurn();
                }
            }
        }

    }

    //this class handles the algorithm that helps the computer play and win if possible.
    //this algorithm is known as the MinMax Algorithm
    private class Ai {
        public void takeTurn() {
            //If the first button has not been played by the player
            if(c[1][1]==2 &&
                    //and the next buttons in the horizontal row have been filled by the player
                    ((c[1][2]==0 && c[1][3]==0) ||
                            //or the next buttons in the diagonal row have been filled by the player
                            (c[2][2]==0 && c[3][3]==0) ||
                            //or the next buttons in the vertical row have been filled by the player
                            (c[2][1]==0 && c[3][1]==0))) {
                //then computer plays in the first button, to prevent the player from winning
                markSquare(1,1);
            }//Or, if the second button has bot been played by the player
            else if (c[1][2]==2 &&
                    //and the next buttons in the horizontal row have been filled by the player
                    ((c[2][2]==0 && c[3][2]==0) ||
                            //or the next buttons in the diagonal row have been filled by the player
                            (c[1][1]==0 && c[1][3]==0))) {
                //then computer plays in the second button, to prevent the player from winning
                markSquare(1,2);
            } else if(c[1][3]==2 &&
                    ((c[1][1]==0 && c[1][2]==0) ||
                            (c[3][1]==0 && c[2][2]==0) ||
                            (c[2][3]==0 && c[3][3]==0))) {
                markSquare(1,3);
            } else if(c[2][1]==2 &&
                    ((c[2][2]==0 && c[2][3]==0) ||
                            (c[1][1]==0 && c[3][1]==0))){
                markSquare(2,1);
            } else if(c[2][2]==2 &&
                    ((c[1][1]==0 && c[3][3]==0) ||
                            (c[1][2]==0 && c[3][2]==0) ||
                            (c[3][1]==0 && c[1][3]==0) ||
                            (c[2][1]==0 && c[2][3]==0))) {
                markSquare(2,2);
            } else if(c[2][3]==2 &&
                    ((c[2][1]==0 && c[2][2]==0) ||
                            (c[1][3]==0 && c[3][3]==0))) {
                markSquare(2,3);
            } else if(c[3][1]==2 &&
                    ((c[1][1]==0 && c[2][1]==0) ||
                            (c[3][2]==0 && c[3][3]==0) ||
                            (c[2][2]==0 && c[1][3]==0))){
                markSquare(3,1);
            } else if(c[3][2]==2 &&
                    ((c[1][2]==0 && c[2][2]==0) ||
                            (c[3][1]==0 && c[3][3]==0))) {
                markSquare(3,2);
            }else if( c[3][3]==2 &&
                    ((c[1][1]==0 && c[2][2]==0) ||
                            (c[1][3]==0 && c[2][3]==0) ||
                            (c[3][1]==0 && c[3][2]==0))) {
                markSquare(3,3);
            } else {
                //Otherwise, the computer looks for a random button and play
                Random rand = new Random();

                int a = rand.nextInt(4);
                int b = rand.nextInt(4);
                //As long as the player has not selected a button or the computer
                while(a==0 || b==0 || c[a][b]!=2) {
                    a = rand.nextInt(4);
                    b = rand.nextInt(4);
                }
                markSquare(a,b);
            }

        }

        private void markSquare(int x, int y) {
            b[x][y].setEnabled(false);
            b[x][y].setTextColor(Color.parseColor("#ffffff"));
            b[x][y].setText(R.string.letter_o);

            c[x][y] = 1;
            checkBoard();
        }
    }

    // check the board to see if someone has won
    private boolean checkBoard() {
        boolean gameOver = false;
        if ((c[1][1] == 0 && c[2][2] == 0 && c[3][3] == 0)
                || (c[1][3] == 0 && c[2][2] == 0 && c[3][1] == 0)
                || (c[1][2] == 0 && c[2][2] == 0 && c[3][2] == 0)
                || (c[1][3] == 0 && c[2][3] == 0 && c[3][3] == 0)
                || (c[1][1] == 0 && c[1][2] == 0 && c[1][3] == 0)
                || (c[2][1] == 0 && c[2][2] == 0 && c[2][3] == 0)
                || (c[3][1] == 0 && c[3][2] == 0 && c[3][3] == 0)
                || (c[1][1] == 0 && c[2][1] == 0 && c[3][1] == 0)) {
            Toast.makeText(getApplicationContext(), "Game over. You win!", Toast.LENGTH_LONG).show();
            gameOver = true;
            disableButtons();
        } else if ((c[1][1] == 1 && c[2][2] == 1 && c[3][3] == 1)
                || (c[1][3] == 1 && c[2][2] == 1 && c[3][1] == 1)
                || (c[1][2] == 1 && c[2][2] == 1 && c[3][2] == 1)
                || (c[1][3] == 1 && c[2][3] == 1 && c[3][3] == 1)
                || (c[1][1] == 1 && c[1][2] == 1 && c[1][3] == 1)
                || (c[2][1] == 1 && c[2][2] == 1 && c[2][3] == 1)
                || (c[3][1] == 1 && c[3][2] == 1 && c[3][3] == 1)
                || (c[1][1] == 1 && c[2][1] == 1 && c[3][1] == 1)) {
            Toast.makeText(getApplicationContext(), "Game over. You lost!", Toast.LENGTH_LONG).show();
            gameOver = true;
            disableButtons();
        } else {
            boolean empty = false;
            for(int i=1; i<=3; i++) {
                for(int j = 1; j <= 3; j++) {
                    if(c[i][j]==2) {
                        empty = true;
                        break;
                    }
                }
            }
            if(!empty) {
                gameOver = true;
                Toast.makeText(getApplicationContext(), "Game over. It's a draw!", Toast.LENGTH_LONG).show();
                disableButtons();
            }
        }

        return gameOver;
    }

    public void disableButtons(){
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                b[i][j].setEnabled(false);
            }
        }
    }

    //Reset the board from the reset button created in the activity_main.xml
    //Reset the board
    public void resetGame(View view){
        for (int i = 0; i < 3; i++){
            for(int j=0; j<3; j++){
                String buttonID = "r" + i + "c" + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                b[i][j] = findViewById(resID);
                b[i][j].setText("");
            }
        }
        setBoard();
    }
}

