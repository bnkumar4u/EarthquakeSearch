package com.example.android.earthquakesearch;



public class EarthQuakeData
{
    private double mMag;
    private String mPlace;
    private long millisec;
    private String url;

    public EarthQuakeData(double mMag, String mPlace, long mTime,String url) {
        this.mMag = mMag;
        this.mPlace = mPlace;
        this.millisec = mTime;
        this.url=url;
    }

    public double getmMag() {
        return mMag;
    }

    public String getmPlace() {
        return mPlace;
    }

    public long getmTime() {
        return millisec;
    }

    public String getUrl() {
        return url;
    }
}
