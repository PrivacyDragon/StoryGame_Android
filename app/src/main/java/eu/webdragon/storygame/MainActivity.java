package eu.webdragon.storygame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class MainActivity extends AppCompatActivity {
    //First of all, a bunch of global variables are defined.
    //I know, some people say that this is bad practice, but I do not really care.
    //It works.

    //This was first also a global: TextView text_Random;
    //This too: int wordNumber;

    boolean CustomWordsUsed = false; //This really has to be global, because it is used in several functions.
    //The same goes for the next three.
    String[] Numbers = {"one", "two", "three", "four", "five", "six",
                        "seven", "eight", "nine", "ten", "eleven", "twelve",
                        "thirteen", "fourteen", "fifteen", "sixteen"};
    String[] PlaceNumbers = {"[1]", "[2]", "[3]", "[4]", "[5]", "[6]", "[7]",
                            "[8]", "[9]", "[10]", "[11]", "[12]", "[13]",
                            "[14]", "[15]", "[16]"};
    String[] ButtonsNr = {"button1", "button2", "button3", "button4","button5",
            "button6", "button7", "button8", "button9", "button10",
            "button11", "button12", "button13", "button14",
            "button15", "button16"};

    List<String> OED = new ArrayList<>(); //This is used if the user wants to use custom words.
    //Here are some global variables for storing what numbers are used for generating words.
    int[] Used = {999, 999, 999, 999, 999, 999, 999, 999, 999, 999, 999, 999, 999, 999, 999, 999};
    //Then, the already used words.
    CharSequence[] filledWith = {""};
    //How much words are selected,
    int filled = 0;
    //And which was last clicked.
    int[] lastClicked = {0};

    //onCreate is just stuff that happen when the app starts or something.
    //Basically, it does setting up.
    protected void onCreate(Bundle savedInstanceState) {
        //Standard bullshit.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            //My own stuff: Setting up the game.
            setUp(getWords()); //setUp requires the list of words to choose from. The standard is used.
        }
    }

    //This function is called when someone activates the redo button.
    //It simply calls the setUp function again, but because it is not known by the button whether custom words are used, this function has to help...
    public void reSet(View NoMatter) { //The View that is passed on to the function has to be accepted, even when it is useless...
        if (CustomWordsUsed) { //If custom words are used, this variable has been set to 'true'.
            //Thus, the custom words are retrieved from the OED via this function.
            setUp(writeOxfordDictionary());
        } else { //No custom words means that normal words are used instead.
            //Getting the normal words has been placed in a function of it's own for slightly better code, but maybe more for to make the code better readable.
            setUp(getWords());
        }
    }

    //And here we have the great setUp function!
    //This function gets the random words, puts them on the buttons and initiates the placeholders.
    //As said earlier in the comments, the function needs to know what words to use, because it might be the case that custom words have to be used.
    public void setUp(String[] Words) {
        //This boolean is used for checking whether a word has already been added to the words for this round or not.
        //If it is, it should not appear again, of course!
        boolean duplicate;
        //Now, for all buttons, a word will be randomly selected.
         for (int i = 0; i < 16; i++) {
             //Before being able to do something with the button, it has to be selected.
             //Hence, the first thing to happen here is that button number i will be grabbed to a variable. The same happens with the placeholder.
             //The placeholder could have been used before, if the user played a round already.
             TextView text_Random = (TextView) findViewById(getResources().getIdentifier(ButtonsNr[i], "id", this.getPackageName()));
             TextView placeHolder = (TextView) findViewById(getResources().getIdentifier(Numbers[i], "id", this.getPackageName()));
             //To make sure that the placeholders are good again, the correct number is put in them. So it will become '[i+1]' (See PlaceNumbers[]).
             placeHolder.setText(PlaceNumbers[i]);
             //Now it is time for selecting a word randomly. The words are in an array, so you can think of them all as numbered.
             //The number of the Chosen Word will be stored in a variable conveniently named 'wordNumber'.
             int wordNumber;
             //This do-while loop makes sure that the code will keep generating a new random number, until it is not a duplicate.
             //So, as long as the number has already been used, the do-while loop will continue.
             do {
                 //These lines are quite simple.
                 //Generating a random number.
                 wordNumber = (int) (Math.random() * Words.length);
                 //No, it is not a duplicate, it can be used! YaY!
                 duplicate = false;
                 //Or is it? Loop trough the array of used numbers to check.
                 for (int j = 0; j < 16; j++) {
                     if (Used[j] == wordNumber) {
                         //Oh no! It actually is a duplicate.
                         //'duplicate' is set to 'true' again and the do-while loop will, as a result, once again be entered...
                         duplicate = true;
                         //It is already stated as duplicate, so it is of no use whatsoever to keep on looping trough the list. Hence, break free from it!
                         break;
                     }
                 }
             } while (duplicate);
             //Then, once the random number has successfully been selected, it has to be stored in the list with used numbers.
             Used[i] = wordNumber;
             //And finally the word can be put on the button.
             text_Random.setText(Words[wordNumber]);
         }
         //Set the 'filled' variable back to 0, so regenerating words actually works...
         //The filled variable is not used in this function. It is used when a user presses a word-button.
         //Then the variable increases by one, so the code can 'know' how much words have been used by the user.
         //It should be unnecessary to say that the user has used absolutely no, zero, words when the new words are just generated...
         filled = 0;
    }

    //Here we have once again some kind of standard function.
    //This has something to do with when the user leaves the app or something.
    //The not-constant variables should then be put in 'savedInstanceState' or something.
    //I do not know how it works exactly, but it does work.
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putIntArray("Used",Used);
        savedInstanceState.putCharSequenceArray("filledWith", filledWith);
        savedInstanceState.putInt("filled", filled);
        savedInstanceState.putIntArray("lastClickedId", lastClicked);
    }

    //And then, when the app is opened again, this standard function thingy will be run.
    //So here, the previously stored variables will be set to their values again.
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        //The restoring of the variables happens here.
        Used = savedInstanceState.getIntArray("Used");
        filledWith = savedInstanceState.getCharSequenceArray("filledWith");
        filled = savedInstanceState.getInt("filled");
        lastClicked = savedInstanceState.getIntArray("lastClickedId");
        //And then, the words have to be selected again.
        //Because it has to be checked whether custom words are used, the Words variable has to be created beforehand.
        String[] Words;
        if (CustomWordsUsed) {
            //When custom words are used, grab them from the OED!
            Words = writeOxfordDictionary();
        } else {
            //Else, retrieve the standard words! (This will also work when a user changed their language.)
            //If the user changed their language, they will still see the same words, but then of the language they are now using.
            Words = getWords();
        }
        //Once the words have been found, they have to be put on their spots again.
        //So, once again, loop trough some code for all buttons. (Of which there are 16).
        for (int i=0; i<16; i++) {
            //This code might look somewhat recognizable.
            //Again, the button is selected in a variable.
            TextView text_Random = (TextView) findViewById(getResources().getIdentifier(ButtonsNr[i], "id", this.getPackageName()));
            //Then, the Used[] variable is used to make sure that the correct word gets on the button again.
            text_Random.setText(Words[Used[i]]);
            //And, if there have been already used words by the user, check whether this particular word has been used already.
            //If so, the button should show that it is used, instead of the word. And the word should show instead of a placeholder.
            if(filled>0) {
                //So, for all used words...
                for (int j=0; j<=filled; j++) {
                    //See if it is the same as the word that is currently being processed..
                    if (filledWith[j] == Words[Used[i]]) {
                        //If so, the button will now show that it is used.
                        text_Random.setText(getString(R.string.Used_Button));
                        //And the correct placeholder is selected.
                        TextView placeHolder = (TextView) findViewById(getResources().getIdentifier(Numbers[j-1], "id", this.getPackageName()));
                        //After that, it is filled with the word.
                        placeHolder.setText(Words[Used[i]]);
                    }

                }
            }
        }
    }

    //This function handles the presses on the buttons.
    //If a user activates a word button, it calls this function, with a View as argument.
    //Here, the view is actually used, because it is needed for getting the id of the button.
    public void buttonPress(View view) {
        //Somehow, the activated button is the view that is passed on to this function.
        //Or maybe not really, but something like that. The button can be grabbed via the ID of the view.
        //I do not really know what happens, but it works.
        Button button = (Button) findViewById(view.getId());
        //The first step is to place the selected word in the first available placeholder.
        //So, let's see what the word was and store it to the variable 'word'.
        CharSequence word = button.getText();
        //But what if the user selected a used button?
        //Of course, nothing should happen!
        //Because of this, the following bunch of code will only be run if the selected button does not have the text of a used button.
        if(word != getString(R.string.Used_Button)) {
            //If, however, the button has not previously been used already, the word of it should be put in the correct placeholder.
            //The first available placeholder is selected by the line below.
            TextView placeHolder = (TextView) findViewById(getResources().getIdentifier(Numbers[filled], "id", this.getPackageName()));
            //After having the placeholder, the button should be turned into a used button.
            //So, set the text on it that it is used.
            button.setText(getString(R.string.Used_Button));
            //And then put the word that the button contained earlier, in the placeholder.
            placeHolder.setText(word);
            //Next, the variable counting how much there is filled has to be updated.
            //This comes down to increasing it by one.
            filled++;
            //Because the user has the option to redo actions, it has to be stored somewhere which word buttons were pressed and when.
            //For this, there is an array. The first item in the array is the first selected word button. The last one is the most recent one.
            //So, once a word button is activated, the id of the button has to be put in the array.
            //This will be done by first making sure that the array is one element longer.
            //To achieve that, overwrite the array with itself and a new length of itself +1.
            lastClicked = Arrays.copyOf(lastClicked, lastClicked.length +1);
            //And then the button can be put in at the end of it!
            lastClicked[lastClicked.length -1] = button.getId();
            //After that, the variable filledWith is overwritten by itself plus one additional element.
            //That new element is for the new word.
            filledWith = Arrays.copyOf(filledWith, filledWith.length + 1);
            //Which is put in there by the line below.
            filledWith[filledWith.length - 1] = word;
        }
    }

    //Now we have reached the function that handles activation of the 'info' button.
    //It will create a pop-up window with information about the application.
    public void infoPress(View view) {
        //First, create a new dialog builder or something like that.
        //This will be able to then create the dialog.
        AlertDialog.Builder dialogBuild = new AlertDialog.Builder(MainActivity.this);
        //Next, get a viewgroup or something. I honestly do not remember what this does.
        //I can be of little help in making this part of the code understandable...
        //Internet guides can be helpful, but then not remembering how things worked is not...
        ViewGroup viewStuff = findViewById(android.R.id.content);
        //I think that the following line tells, I think, that the layout of the dialog is in the dialog.xml.
        View dialog = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog, viewStuff, false);
        //Then it is set viewable, I guess...
        dialogBuild.setView(dialog);
        //And this one creates it.
        AlertDialog infoDialog = dialogBuild.create();
        //So that the next line can show it.
        infoDialog.show();
        //I am sorry for the not really helpful comments in this function...
    }

    //Again, a wild function appears!
    //What will it do?
    //This function is called when the user activates the 'redo' button.
    //It reverts the previous action.
    public void redoButton(View view) {
        //The last activated button can be retrieved from the array where they are stored, which you should have seen somewhere up there^.
        //So, let's grab that button!
        Button lastButton = (Button) findViewById(lastClicked[lastClicked.length -1]);
        //Then, check if the button was actually used, for we do not want errors or bugs to occur.
        if (filled>0 && lastButton.getText() == getString(R.string.Used_Button)) {
            //It turns out that it was used after all!
            //The last used word is retrieved from the array where they are stored.
            CharSequence word = filledWith[filledWith.length - 1];
            //Next, the placeholder where this word was put is grabbed, to be able to put the placeholder thing back.
            TextView placeHolder = (TextView) findViewById(getResources().getIdentifier(Numbers[filled - 1], "id", this.getPackageName()));
            //The last used button gets back the word that it had,
            lastButton.setText(word);
            //And the placeholder is restored to its original state again.
            placeHolder.setText(PlaceNumbers[filled - 1]);
            //Lastly, there is now one less filled placeholder, so that should be updated accordingly.
            filled--;
            //And that what it was filled with should of course also be removed from the array.
            filledWith = Arrays.copyOf(filledWith, filledWith.length - 1);
            //The same goes for the button. That is no longer a clicked button now.
            lastClicked = Arrays.copyOf(lastClicked, lastClicked.length -1);
        }
     }

     //Please, do not ask me why I have made a function of this.
     //All it really does is returning the value of getResources().getStringArray(R.array.wordList).
     public String[] getWords() {
         //Get the words.
         String[] Words = getResources().getStringArray(R.array.wordlist);
         //And return them.
         return Words;
     }

    //Now this is a nice function.
    //This function writes the Oxford English Dictionary!
    //Or well... Actually not.
    //But it returns the 'dictionary' as in that it gives back the array of custom words.
    public String[] writeOxfordDictionary() {
        //The custom words are stored in the arraylist OED. So, the first step is easy:
        //Create a new string list with the length of the size of the OED.
        String[] CustomWords = new String[OED.size()];
        //And then put the words in it, for as long as it is.
        for (int i=0; i<OED.size(); i++){
            //This puts the word in it.
            CustomWords[i] = OED.get(i);
        }
        //In the end, it can be returned as an array full of custom words.
        return CustomWords;
    }

    //Almost the last function!
    //This function runs when the user activates the button for custom words.
    public void StartCustomWords(View view) {
        //It creates a file picker thingy, which is provided by standard API's or something.
        Intent FileGrabber = new Intent(Intent.ACTION_GET_CONTENT);
        //The files shown should be openable.
        FileGrabber.addCategory(Intent.CATEGORY_OPENABLE);
        //And the user should select a text file, so only text files should be shown.
        FileGrabber.setType("text/plain");
        //Start the file picker!
        startActivityForResult(FileGrabber, 42);
    }
    //Now, because usually nothing special happens, I guess, stuff has to be done.
    //We are overriding the usual behaviour!
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        //First of all, check whether the request code is the same as the one it was sent with.
        //And, of course, if the result is good.
        if (requestCode == 42 && resultCode == Activity.RESULT_OK) {
            //If it is, stuff will be done!
            //Here, an empty Uri variable is created, to be able to do things with it.
            Uri uri = null;
            //If there is no result data, there will not happen all that much.
            if (resultData != null) {
                //However, if it is NOT null, stuff will happen!
                //The uri variable is now stuffed with the uri that was returned by the file thing.
                uri = resultData.getData();
                //Having that, try to open the file and grab the words.
                try {
                    //Get the file into an InputStream, so it can be read.
                    InputStream wordFile = getContentResolver().openInputStream(uri);
                    //Then, some people seem to be fond of Scanner, so I chose to go with that.
                    //Create a scanner thing on the file.
                    Scanner wordScan = new Scanner(wordFile);
                    //With this scanner, add the word on the current line every time.
                    //Or something like that. If there is a next line, stuff happens.
                    while (wordScan.hasNextLine()) {
                        //Then, the current line will be returned as the scanner goes to the next line.
                        //This current line is only one word, so the line is the word.
                        String word = wordScan.nextLine();
                        //So, add the word to the OED!
                        OED.add(word);
                    }
                    //After all that has been finished, it is time to write the Oxford English Dictionary!
                    String[] CustomWords = writeOxfordDictionary();
                    //And then set the game up again with that custom words.
                    setUp(CustomWords);
                    //But do not forget to indicate that there are now custom words!
                    CustomWordsUsed = true;
                } catch (FileNotFoundException e) {
                    //Because opening a file can give errors, it is good to catch those errors.
                    //And once they are caught, print them or something.
                    e.printStackTrace();
                }


            }
        }
    }
}
//This is the end of my code. It was my honour to guide you trough it. I do hope that you do now know and understand what the program does!
//Thank you for reading all of this.