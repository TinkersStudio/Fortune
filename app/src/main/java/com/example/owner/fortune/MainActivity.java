/**
 * @author: Dung Trinh
 * @version: 1.0.0
 * None support for search keyword (Will support in the future version
 */
package com.example.owner.fortune;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.*;

public class MainActivity extends AppCompatActivity {
    /**all of the quote is in here*/
    private ArrayList<Quote>  quoteList;
    /**The selected quote base on the topic*/
    private ArrayList<Quote> selectedList;

    /**Topic to search from**/
    protected String topic;

    protected String searchKeyword;

    /**Layout and menu*/
    protected Button fortuneButton;
    protected TextView quoteView;
    protected Menu mainMenu;
    private static Map<String, ArrayList<Quote>> myMap;

    private static final String TAG = Activity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLayout();
        initGenerateListener();

        this.quoteList = new ArrayList<>();
        this.selectedList = new ArrayList<>();
        this.topic = "general";
        this.searchKeyword = "";
        myMap = new HashMap<String, ArrayList<Quote>>();

        /**import value into the hashmap*/
        importValue();
        for (String key:myMap.keySet())
        {
            quoteList.addAll(myMap.get(key));
            selectedList.addAll(myMap.get(key));
        }
    }

    /**Import the value in string resource into the map
     * Tedious work just to import the value in here
     *
     * */
    private void importValue()
    {


    }
    /**Init the first component*/
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
                if (v.getId() == R.id.generate_button) {
                    Log.d(TAG, "onClick() called by generateButton");
                    quoteView.setText((getQuote().toString()));
                    if(topic.equals("general"))
                    {
                        newQuote = getQuote();
                    }
                    else
                    {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /**
        switch(item.getItemId()){
            case R.id.submenu_like:
                applyFilter(Joke.LIKE);
                break;
            case R.id.submenu_dislike:
                applyFilter(Joke.DISLIKE);
                break;
            case R.id.submenu_show_all:
                applyFilter(FILTER_SHOW_ALL);
                break;
            case R.id.submenu_unrated:
                applyFilter(Joke.UNRATED);
                break;
            default:
                super.onOptionsItemSelected(item);
                break;
        }

        */
        return true;
    }

}
