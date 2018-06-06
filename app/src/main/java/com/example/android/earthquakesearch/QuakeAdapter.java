package com.example.android.earthquakesearch;
import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class QuakeAdapter extends ArrayAdapter
{
    public QuakeAdapter(Activity context, ArrayList<EarthQuakeData> androidFlavors)
    {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for three TextViews , the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context,0,androidFlavors);
    }
    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position The position in the list of data that should be displayed in the
     *                 list item view.
     * @param convertView The recycled view to populate.
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.simple_list_item, parent, false);
        }

        EarthQuakeData currentEarthQuake=(EarthQuakeData) getItem(position);



        TextView mag=(TextView) listItemView.findViewById(R.id.mag);
        DecimalFormat d=new DecimalFormat("0.0");
        String formattedMag=d.format(currentEarthQuake.getmMag());
        mag.setText(formattedMag);
        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) mag.getBackground();
        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(currentEarthQuake.getmMag());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);


        String totalPlace=currentEarthQuake.getmPlace(),place1,place2;
        String[] pl=totalPlace.split("of");
        if(pl.length==1)
        {
            place1="Near the";
            place2=pl[0];
        }
        else
        {
            place1=pl[0]+"of";
            place2=totalPlace.replace(pl[0]+"of","");
        }
        TextView placeview1=(TextView)listItemView.findViewById(R.id.place1);
        placeview1.setText(place1);
        TextView placeview2=(TextView)listItemView.findViewById(R.id.place2);
        placeview2.setText(place2);

        Date date=new Date(currentEarthQuake.getmTime());

        //Textview for date
        TextView dateView=(TextView)listItemView.findViewById(R.id.date);
        String formattedDate=formatDate(date);
        dateView.setText(formattedDate);

        //TextView for time
        TextView timeView=(TextView)listItemView.findViewById(R.id.time);
        String formattedTime=formatTime(date);
        timeView.setText(formattedTime);


        return listItemView;
    }
    private String formatDate(Date date)
    {
        SimpleDateFormat dateFormat=new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(date);
    }
    private String formatTime(Date date)
    {
        SimpleDateFormat timeFormat=new SimpleDateFormat("h:mm a");
        return timeFormat.format(date);
    }

    private int getMagnitudeColor(double magnitude)
    {
        int magnitudecolorResourceId;
        int magnitudeFloor=(int)Math.floor(magnitude);
        switch (magnitudeFloor)
        {
            case 0:
            case 1:
                magnitudecolorResourceId=R.color.magnitude1;
                break;
            case 2:
                magnitudecolorResourceId=R.color.magnitude2;
                break;
            case 3:
                magnitudecolorResourceId=R.color.magnitude3;
                break;
            case 4:
                magnitudecolorResourceId=R.color.magnitude4;
                break;
            case 5:
                magnitudecolorResourceId=R.color.magnitude5;
                break;
            case 6:
                magnitudecolorResourceId=R.color.magnitude6;
                break;
            case 7:
                magnitudecolorResourceId=R.color.magnitude7;
                break;
            case 8:
                magnitudecolorResourceId=R.color.magnitude8;
                break;
            case 9:
                magnitudecolorResourceId=R.color.magnitude9;
                break;
            default:
                magnitudecolorResourceId=R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(),magnitudecolorResourceId);
    }
}
