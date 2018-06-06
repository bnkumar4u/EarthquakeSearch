package com.example.android.earthquakesearch;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<ArrayList<EarthQuakeData>> {


    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;


    boolean isChanged=false;

    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */

    private static int EARTHQUAKE_lOADER_ID = 1;

    /** Adapter for the list of earthquakes */
    private QuakeAdapter adapter;
    private static double mag=6;
    private static int limit=10;
    private static String orderBy="time";
    private static String newOrderBy=orderBy;

    /** URL for earthquake data from the USGS dataset */
    private static String USGS_REQUEST_URl="https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby="+orderBy+"&minmag="+mag+"&limit="+limit;


    public static final String LOG_TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create a new {@link ArrayAdapter} of earthquakes
        adapter = new QuakeAdapter(this, new ArrayList<EarthQuakeData>());



        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        earthquakeListView.setEmptyView(mEmptyStateTextView);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);


        //get reference to the Connectivity manager to check state of network
        ConnectivityManager connMgr=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        //get details on currently active default data network
        NetworkInfo networkInfo=connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if(networkInfo!=null&&networkInfo.isConnected())
        {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(EARTHQUAKE_lOADER_ID, null, this);
        }
        else
        {
            //otherwise display error
            //first ,hide loading indicator so error message will be visible
            View loadingIndicator=findViewById(R.id.progress_bar);
            loadingIndicator.setVisibility(View.GONE);
            //update empty stte with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }




        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                EarthQuakeData currentEarthquake = (EarthQuakeData) adapter.getItem(position);

                Uri earthquakeUri=Uri.parse(currentEarthquake.getUrl());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW,earthquakeUri);

                startActivity(websiteIntent);
            }
        });


        RadioGroup radioGroup=(RadioGroup)findViewById(R.id.radio_sort);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {

            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                switch (checkedId)
                {
                    case R.id.radio_time:
                        newOrderBy="time";
                        break;
                    case R.id.radio_timeasc:
                        newOrderBy="time-asc";
                        break;
                    case R.id.radio_mag:
                        newOrderBy="magnitude";
                        break;
                    case R.id.radio_magasc:
                        newOrderBy="magnitude-asc";
                }
            }
        });
    }

    @Override
    public Loader<ArrayList<EarthQuakeData>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        return new EarthquakeLoader(this, USGS_REQUEST_URl);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<EarthQuakeData>> loader, ArrayList<EarthQuakeData> eathquakes)
    {
        ProgressBar progressBar=(ProgressBar)findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        // Set empty state text to display "No earthquakes found."
        mEmptyStateTextView.setText(R.string.no_earthquakes);

        // Clear the adapter of previous earthquake data
        adapter.clear();
        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if(eathquakes!=null && !eathquakes.isEmpty())
        {
            adapter.addAll(eathquakes);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<EarthQuakeData>> loader)
    {
        //Loader reset, so we can clear out our exiting data.
        adapter.clear();
    }

    public void searchButtonClick(View v)
    {
        EditText editMag=(EditText)findViewById(R.id.edit_mag);
        EditText editLimit=(EditText)findViewById(R.id.edit_limit);

        String temp;
        temp=editMag.getText().toString().trim();
        if(temp!=null&&temp.length()>0)
        {
            double newMag=Double.parseDouble(temp);
            mag=newMag;
            isChanged=true;
        }
        temp=editLimit.getText().toString().trim();
        if (temp!=null&&temp.length()>0)
        {
            int newLimit=Integer.parseInt(temp);
            limit=newLimit;
            isChanged=true;
        }
        if(!newOrderBy.equals(orderBy))
        {
            orderBy=newOrderBy;
            isChanged=true;
        }
        if(isChanged)
        {
            EARTHQUAKE_lOADER_ID++;
            USGS_REQUEST_URl="https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby="+orderBy+"&minmag="+mag+"&limit="+limit;
        }

        //get reference to the Connectivity manager to check state of network
        ConnectivityManager connMgr=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        //get details on currently active default data network
        NetworkInfo networkInfo=connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if(networkInfo!=null&&networkInfo.isConnected())
        {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(EARTHQUAKE_lOADER_ID, null, this);
        }
        else
        {
            //otherwise display error
            //first ,hide loading indicator so error message will be visible
            View loadingIndicator=findViewById(R.id.progress_bar);
            loadingIndicator.setVisibility(View.GONE);
            //update empty stte with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }
}
