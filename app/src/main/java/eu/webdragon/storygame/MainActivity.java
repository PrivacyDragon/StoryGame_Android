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
    CharSequence[] Words = {"DRAGON","COMPUTER","NOTE","CALCULATE","CASTLE","HARP","WORLD",
            "SEA","BOOK","WIZARD","TREE","PLANE","JUNGLE","TEMPLE","CACKE",
            "CHICKEN","DEVICE","BOTTLE","DOG","BEER","MOON","SPACESHIP","FIREWORK",
            "VOLCANO","STORM","POETRY","FLOWER","PYTHON","TIME","ICE","THUNDER",
            "FIRE","WITCH","HEL","BUILDING","BRIDGE","CHAIR","BIRD","ROCK","SUN",
            "STARS","ALIEN","UFO","WAND","KEY","LOCK","HOOK","HORSE","GOAT","RAIN",
            "ARROW","EYE","WATCH","JUMP","FISH","MASK","PENTAGRAM","SWORD","TOWN",
            "BABY","TURTLE","HUT","SKELETON","SHADOWS","MONSTER","SEARCH","CHEST",
            "SLEEP","GOLD","WRITER","LIGHT","DARKNESS","ANGLE","THOR","DEVIL","FOOD",
            "POISON","LABYRINTH","THIEF","CROWN","DWARF","WOLF","MARKET","PRINCESS",
            "CHURCH","DEAD","FISHER","SERPENT","KING","RAINBOW","SNOW","ARMOR",
            "FAIRY","BIKE","LOKI","BANDAGE","BLOOD","QUEEN","ISLAND","GAME",
            "OXYGEN","PLANT","SOAP","HERO","OIL","PAINTING","MUSIC"};
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
    Button lastClicked;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //setSupportActionBar(myToolbar);
        boolean duplicate;
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
            for (int i = 0; i < 16; i++) {
                text_Random = (TextView) findViewById(getResources().getIdentifier(ButtonsNr[i], "id", this.getPackageName()));
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
    }
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putIntArray("Used",Used);
        savedInstanceState.putCharSequenceArray("filledWith", filledWith);
        savedInstanceState.putInt("filled", filled);
        savedInstanceState.putInt("lastClickedId", lastClicked.getId());
        //lastClicked.getId();
    }
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        Used = savedInstanceState.getIntArray("Used");
        filledWith = savedInstanceState.getCharSequenceArray("filledWith");
        filled = savedInstanceState.getInt("filled");
        int lastClickedId = savedInstanceState.getInt("lastClickedId");
        lastClicked = (Button) findViewById(lastClickedId);
        for (int i=0; i<16; i++) {
            text_Random = (TextView) findViewById(getResources().getIdentifier(ButtonsNr[i], "id", this.getPackageName()));
            text_Random.setText(Words[Used[i]]);
            if(filled>0) {
                for (int j=0; j<=filled; j++) {
                    if (filledWith[j] == Words[Used[i]]) {
                        text_Random.setText("[Used Button]");
                        TextView placeHolder = (TextView) findViewById(getResources().getIdentifier(Numbers[j-1], "id", this.getPackageName()));
                        placeHolder.setText(Words[Used[i]]);
                    }

                }
            }
        }
    }
    public void buttonPress(View view) {
        Button button = (Button) findViewById(view.getId());
        lastClicked = button;
        CharSequence word = button.getText();
        if(word != "[Used Button]") {
            TextView placeHolder = (TextView) findViewById(getResources().getIdentifier(Numbers[filled], "id", this.getPackageName()));
            button.setText("[Used Button]");
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
    }
    public void redoButton(View view) {
        if (filled>0 && lastClicked.getText() == "[Used Button]") {
            CharSequence word = filledWith[filledWith.length - 1];
            TextView placeHolder = (TextView) findViewById(getResources().getIdentifier(Numbers[filled - 1], "id", this.getPackageName()));
            lastClicked.setText(word);
            placeHolder.setText(PlaceNumbers[filled - 1]);
            filled--;
            filledWith = Arrays.copyOf(filledWith, filledWith.length - 1);
        }
     }
}