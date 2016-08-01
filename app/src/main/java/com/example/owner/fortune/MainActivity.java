/**
 * @author: Dung Trinh
 * @version: 1.0.0
 * None support for search keyword (Will support in the future version
 * @version: 1.0.0 Support quote
 * @version: 1.0.5 Support menu selection
 * @version: 1.1.0 Refine Layout
 * @version:  1.1.5 Increase number of quote (goal 20 for each category)
 * @version: 2.0.0 Make the share on facebook button
 * @version:  3.0.0 Make the database and migration
 */
package com.example.owner.fortune;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.*;

public class MainActivity extends AppCompatActivity {
    /**Contain all the Quote*/
    private ArrayList<Quote>  quoteList;

    /**The selected quote base on the topic*/
    private ArrayList<Quote> selectedList;

    /**Topic to search from**/
    protected String topic;

    /**Keyword to search for*/
    protected String searchKeyword;

    /**Generate button to generate fortune*/
    protected Button fortuneButton;

    /**The quote to display on the screen*/
    protected TextView quoteView;

    /**Menu with the filter*/
    protected Menu mainMenu;

    /**Hashmap for quick indexing*/
    private static Map<String, ArrayList<Quote>> myMap;

    /**
     * Key used for storing and retrieving the values
     */
    protected static final String SAVED_FILTER_VALUE = "filter";
    protected static final String SAVED_HASHMAP = "quote_map";
    protected static final String SAVED_ALLQUOTE = "all_quote";
    protected static final String SAVED_SELECTED = "select_quote";
    //protected static final String SAVE_KEYWORD_VALUE = "keyword";

    /**This is used for debugging*/
    private static final String TAG = Activity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**Initialize layout and listener*/
        initLayout();
        initGenerateListener();

        this.quoteList = new ArrayList<>();
        this.selectedList = new ArrayList<>();
        this.topic = "general";
        //this.searchKeyword = "";
        myMap = new HashMap<String, ArrayList<Quote>>();

        /**import value into the hashmap*/
        importValue();

        /**Import value into the general list/select list*/
        for (String key:myMap.keySet())
        {
            quoteList.addAll(myMap.get(key));
            selectedList.addAll(myMap.get(key));
        }
    }

    /**
     * Save all of the values
     * @param outState the Bundle that will be save by the system
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        /*Save information*/
        /* Store the current value of topic in outState using SAVED_FILTER_VALUE */
        outState.putString(SAVED_FILTER_VALUE, this.topic);
        outState.putParcelableArrayList(SAVED_ALLQUOTE, this.quoteList);
        outState.putParcelableArrayList(SAVED_SELECTED, this.selectedList);
        outState.putSerializable(SAVED_HASHMAP, (Serializable) this.myMap);

        //Call the super function at the end
        super.onSaveInstanceState(outState);
    }


    /**
     * Restore the save value when the app is pause or stop
     * @param savedInstanceState the Bundle with all values
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        /* Ensure other UI state is preserved */
        super.onRestoreInstanceState(savedInstanceState);

        /* Retrieve the value of topic from savedInstanceState using SAVED_FILTER_VALUE */
        if(savedInstanceState != null &&
                savedInstanceState.containsKey(SAVED_FILTER_VALUE)) {
            this.topic = savedInstanceState.getString(SAVED_FILTER_VALUE);
            this.selectedList = savedInstanceState.getParcelableArrayList(SAVED_SELECTED);
            this.quoteList = savedInstanceState.getParcelableArrayList(SAVED_ALLQUOTE);
            //this cast may contain error
            this.myMap = (HashMap)savedInstanceState.getSerializable(SAVED_HASHMAP);
        }
    }

    /**
     * Take the list of string from resource and call importValueInMap to
     * add the quotes into the general map
     */
    public void importValue()
    {
        //TODO: add everything in here
        Resources res = this.getResources();
        importValueInMap(res.getStringArray(R.array.art), "art");
        importValueInMap(res.getStringArray(R.array.general), "general");
    }

    /**
     * Add all value in the general map. This function takes the list of strings
     * from the list and add it to the general map
     * @param list list of quote that is used
     * @param name the key for map
     */
    private void importValueInMap(String[] list, String name) {
        Quote quote;
        myMap.put(name, new ArrayList<Quote>());

        for(String s : list) {
            Log.d(TAG, "Adding new joke" + s);
            quote = new Quote(s);
            myMap.get(name).add(quote);
        }
    }

    /**Init the component of the layout*/
    protected void initLayout() {
        setContentView(R.layout.activity_main);
        fortuneButton = (Button) findViewById(R.id.generate_button);
        quoteView = (TextView) findViewById(R.id.quote_view);
    }

    /**Button listener for the generate button*/
    protected void initGenerateListener()
    {
        fortuneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Quote newQuote;
                /**button is clicked*/
                if (v.getId() == R.id.generate_button) {
                    Log.d(TAG, "onClick() called by generateButton");
                    //quoteView.setText((getQuote().toString()));
                    if(topic.equals("general"))
                    {
                        newQuote = getQuote();
                    }
                    else
                    {
                        //Toast.makeText(getApplicationContext(), "this is my Toast message!!! =)",
                        //        Toast.LENGTH_LONG).show();
                        newQuote = topicQuote(topic);
                    }
                    quoteView.setText(newQuote.toString());
                }
            }
        });
    }

    /**Return a random quote from the list*/
    protected Quote getQuote()
    {
        Random randomGenerator = new Random();
        int index = randomGenerator.nextInt(quoteList.size());
        return quoteList.get(index);
    }

    /**Return the quote based on the topic*/
    protected Quote topicQuote(String topic)
    {
        selectedList = myMap.get(topic);
        Random randomGenerator = new Random();
        int index = randomGenerator.nextInt(selectedList.size());
        return selectedList.get(index);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        this.mainMenu = menu;
        return true;
    }

    /**
     * This will change the global variable topic to set the seach
     * @param item is select
     * @return boolean of chosen value
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.share_quote:
                //call sharing on facebook
                break;
            case R.id.art:
                topic = "art";
                break;
            case R.id.general:
                topic = "general";
                break;
            case R.id.definitions:
                topic = "definitions";
                break;
            case R.id.education:
                topic = "education";
                break;
            case R.id.ethnic:
                topic = "ethnic";
                break;
            case R.id.food:
                topic = "food";
                break;
            case R.id.geeky:
                topic = "geeky";
                break;
            case R.id.humorist:
                topic = "humorist";
                break;
            case R.id.law:
                topic = "law";
                break;
            case R.id.literature:
                topic = "literature";
                break;
            case R.id.love:
                topic = "love";
                break;
            case R.id.kids:
                topic = "kids";
                break;
            case R.id.medicine:
                topic = "medicine";
                break;
            case R.id.pet:
                topic = "pet";
                break;
            case R.id.platitude:
                topic = "platitude";
                break;
            case R.id.politics:
                topic = "politics";
                break;
            case R.id.riddle:
                topic = "riddle";
                break;
            case R.id.sports:
                topic = "sports";
                break;
            case R.id.wisdom:
                topic = "wisdom";
                break;
            case R.id.work:
                topic = "work";
                break;
            default:
                super.onOptionsItemSelected(item);
                break;
        }
        onPrepareOptionsMenu(this.mainMenu);
        return true;
    }

    /**
     * Return the filter that is selected
     * @return curMenuItem the current selected filter
     */
    public String getMenuTitleChange() {
        String curMenuItem = "";
        switch (topic) {
            case "art":
                curMenuItem = "Art";
                break;
            case "general":
                curMenuItem = "General";
                break;
            case "definitions":
                curMenuItem = "Definitions";
                break;
            case "education":
                curMenuItem = "Education";
                break;
            case "ethnic":
                curMenuItem = "Ethnic";
                break;
            case "food":
                curMenuItem = "Food";
                break;
            case "geeky":
                curMenuItem = "Geeky";
                break;
            case "humorist":
                curMenuItem = "Humorist";
                break;
            case "law":
                curMenuItem = "Law";
                break;
            case "literature":
                curMenuItem = "Literature";
                break;
            case "love":
                curMenuItem = "Love";
                break;
            case "kids":
                curMenuItem = "Kids";
                break;
            case "medicine":
                curMenuItem = "Medicine";
                break;
            case "pet":
                curMenuItem = "Pet";
                break;
            case "platitude":
                curMenuItem = "Platitude";
                break;
            case "politics":
                curMenuItem = "Politics";
                break;
            case "riddle":
                curMenuItem = "Riddle";
                break;
            case "sports":
                curMenuItem = "Sports";
                break;
            case "wisdom":
                curMenuItem = "Wisdom";
                break;
            case "work":
                curMenuItem = "Work";
                break;
            default:
                curMenuItem = "General";
                break;
        }
        return curMenuItem;
    }

    /**
     * Change the menu layout
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.menu_filter);

        /* Change the title text of the filter menu item */
        item.setTitle(getMenuTitleChange());
        this.mainMenu = menu;

        return super.onPrepareOptionsMenu(menu);
    }
}
