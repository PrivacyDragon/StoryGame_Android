package eu.webdragon.storygame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;


public class MainActivity extends AppCompatActivity {

    TextView text_Random;
    int wordNumber;

    String[] Numbers = {"one", "two", "three", "four", "five", "six",
                        "seven", "eight", "nine", "ten", "eleven", "twelve",
                        "thirteen", "fourteen", "fifteen", "sixteen"};
    String[] PlaceNumbers = {"[1]", "[2]", "[3]", "[4]", "[5]", "[6]", "[7]",
                            "[8]", "[9]", "[10]", "[11]", "[12]", "[13]",
                            "[14]", "[15]", "[16]"};
    Dictionary<String, Integer> Buttons = new Hashtable<String, Integer>();

    String[] ButtonsNr = {"button1", "button2", "button3", "button4","button5",
                        "button6", "button7", "button8", "button9", "button10",
                        "button11", "button12", "button13", "button14",
                        "button15", "button16"};
    int[] Used = {999, 999, 999, 999, 999, 999, 999, 999, 999, 999, 999, 999, 999, 999, 999, 999};
    CharSequence[] filledWith = {""};
    int filled = 0;
    //Button lastClicked;
    int[] lastClicked = {0};
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //setSupportActionBar(myToolbar);
        Buttons.put("button1", 0);
        Buttons.put("button2", 1);
        Buttons.put("button3", 2);
        Buttons.put("button4", 3);
        Buttons.put("button5", 4);
        Buttons.put("button6", 5);
        Buttons.put("button7", 6);
        Buttons.put("button8", 7);
        Buttons.put("button9", 8);
        Buttons.put("button10", 9);
        Buttons.put("button11", 10);
        Buttons.put("button12", 11);
        Buttons.put("button13", 12);
        Buttons.put("button14", 13);
        Buttons.put("button15", 14);
        Buttons.put("button16", 15);
        if (savedInstanceState == null) {
            View grrr = null;
            setUp(grrr);
        }
    }
    public void setUp(View shutUp) {
        boolean duplicate;
        CharSequence[] Words = getWords();
         for (int i = 0; i < 16; i++) {
             text_Random = (TextView) findViewById(getResources().getIdentifier(ButtonsNr[i], "id", this.getPackageName()));
             TextView placeHolder = (TextView) findViewById(getResources().getIdentifier(Numbers[i], "id", this.getPackageName()));
             placeHolder.setText(PlaceNumbers[i]);
             do {
                 wordNumber = (int) (Math.random() * Words.length);
                 duplicate = false;
                 for (int j = 0; j < 16; j++) {
                     if (Used[j] == wordNumber) {
                         duplicate = true;
                         break;
                     }
                 }
             } while (duplicate);
             Used[i] = wordNumber;
             text_Random.setText(Words[wordNumber]);
         }
    }
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putIntArray("Used",Used);
        savedInstanceState.putCharSequenceArray("filledWith", filledWith);
        savedInstanceState.putInt("filled", filled);
        savedInstanceState.putIntArray("lastClickedId", lastClicked);
        //lastClicked.getId();
    }
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        Used = savedInstanceState.getIntArray("Used");
        filledWith = savedInstanceState.getCharSequenceArray("filledWith");
        filled = savedInstanceState.getInt("filled");
        lastClicked = savedInstanceState.getIntArray("lastClickedId");
        //lastClicked = (Button) findViewById(lastClickedId);
        CharSequence[] Words = getWords();
        for (int i=0; i<16; i++) {
            text_Random = (TextView) findViewById(getResources().getIdentifier(ButtonsNr[i], "id", this.getPackageName()));
            text_Random.setText(Words[Used[i]]);
            if(filled>0) {
                for (int j=0; j<=filled; j++) {
                    if (filledWith[j] == Words[Used[i]]) {
                        text_Random.setText(getString(R.string.Used_Button));
                        TextView placeHolder = (TextView) findViewById(getResources().getIdentifier(Numbers[j-1], "id", this.getPackageName()));
                        placeHolder.setText(Words[Used[i]]);
                    }

                }
            }
        }
    }
    public void buttonPress(View view) {
        Button button = (Button) findViewById(view.getId());
        lastClicked = Arrays.copyOf(lastClicked, lastClicked.length +1);
        lastClicked[lastClicked.length -1] = button.getId();
        CharSequence word = button.getText();
        if(word != getString(R.string.Used_Button)) {
            TextView placeHolder = (TextView) findViewById(getResources().getIdentifier(Numbers[filled], "id", this.getPackageName()));
            button.setText(getString(R.string.Used_Button));
            placeHolder.setText(word);
            filled++;
            filledWith = Arrays.copyOf(filledWith, filledWith.length + 1);
            filledWith[filledWith.length - 1] = word;

        }
    }
    public void infoPress(View view) {
        AlertDialog.Builder dialogBuild = new AlertDialog.Builder(MainActivity.this);
        ViewGroup viewStuff = findViewById(android.R.id.content);
        View dialog = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog, viewStuff, false);
        dialogBuild.setView(dialog);
        AlertDialog infoDialog = dialogBuild.create();
        infoDialog.show();
        //TextView infoText = (TextView) findViewById(getResources().getIdentifier("infoDialog", "id", this.getPackageName()));
        //infoText.setText(String.format(getString(R.string.About_Text), BuildConfig.VERSION_NAME));
    }
    public void redoButton(View view) {
        Button lastButton = (Button) findViewById(lastClicked[lastClicked.length -1]);
        if (filled>0 && lastButton.getText() == getString(R.string.Used_Button)) {
            CharSequence word = filledWith[filledWith.length - 1];
            TextView placeHolder = (TextView) findViewById(getResources().getIdentifier(Numbers[filled - 1], "id", this.getPackageName()));
            lastButton.setText(word);
            placeHolder.setText(PlaceNumbers[filled - 1]);
            filled--;
            filledWith = Arrays.copyOf(filledWith, filledWith.length - 1);
            lastClicked = Arrays.copyOf(lastClicked, lastClicked.length -1);
        }
     }
     public CharSequence[] getWords() {
         CharSequence[] Words = {getString(R.string.word1) , getString(R.string.word2), getString(R.string.word3), getString(R.string.word4),
                 getString(R.string.word5), getString(R.string.word6), getString(R.string.word7), getString(R.string.word8), getString(R.string.word9),
                 getString(R.string.word10), getString(R.string.word11), getString(R.string.word12), getString(R.string.word13),
                 getString(R.string.word14), getString(R.string.word15), getString(R.string.word16), getString(R.string.word17),
                 getString(R.string.word18), getString(R.string.word19), getString(R.string.word20), getString(R.string.word21),
                 getString(R.string.word22), getString(R.string.word23), getString(R.string.word24), getString(R.string.word25),
                 getString(R.string.word26), getString(R.string.word27), getString(R.string.word28), getString(R.string.word29),
                 getString(R.string.word30), getString(R.string.word31), getString(R.string.word32), getString(R.string.word33),
                 getString(R.string.word34), getString(R.string.word35), getString(R.string.word36), getString(R.string.word37),
                 getString(R.string.word38), getString(R.string.word39), getString(R.string.word40), getString(R.string.word41),
                 getString(R.string.word42), getString(R.string.word43), getString(R.string.word44), getString(R.string.word45),
                 getString(R.string.word46), getString(R.string.word47), getString(R.string.word48), getString(R.string.word49),
                 getString(R.string.word50), getString(R.string.word51), getString(R.string.word52), getString(R.string.word53),
                 getString(R.string.word54), getString(R.string.word55), getString(R.string.word56), getString(R.string.word57),
                 getString(R.string.word58), getString(R.string.word59), getString(R.string.word60), getString(R.string.word61),
                 getString(R.string.word62), getString(R.string.word63), getString(R.string.word64), getString(R.string.word65),
                 getString(R.string.word66), getString(R.string.word67), getString(R.string.word68), getString(R.string.word69),
                 getString(R.string.word70), getString(R.string.word71), getString(R.string.word72), getString(R.string.word73),
                 getString(R.string.word74), getString(R.string.word75), getString(R.string.word76), getString(R.string.word77),
                 getString(R.string.word78), getString(R.string.word79), getString(R.string.word80), getString(R.string.word81),
                 getString(R.string.word82), getString(R.string.word83), getString(R.string.word84), getString(R.string.word85),
                 getString(R.string.word86), getString(R.string.word87), getString(R.string.word88), getString(R.string.word89),
                 getString(R.string.word90), getString(R.string.word91), getString(R.string.word92), getString(R.string.word93),
                 getString(R.string.word94), getString(R.string.word95), getString(R.string.word96), getString(R.string.word97),
                 getString(R.string.word98), getString(R.string.word99), getString(R.string.word100), getString(R.string.word101),
                 getString(R.string.word102), getString(R.string.word103), getString(R.string.word104), getString(R.string.word105),
                 getString(R.string.word106), getString(R.string.word107)};
         return Words;
     }
}