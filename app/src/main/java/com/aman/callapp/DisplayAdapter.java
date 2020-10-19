package com.aman.callapp;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DisplayAdapter extends BaseAdapter {
    private Context mContext;
    //list fields to be displayed
    private ArrayList<Integer> id;
    private ArrayList<Double> lat;
    private ArrayList<Double> lng;
    private ArrayList<String> num;
    private ArrayList<String> dateTime;

    private ArrayList<String> distance;


    public DisplayAdapter(Context c, ArrayList<Integer> id,ArrayList<Double> lat, ArrayList<Double> lng, ArrayList<String> num,ArrayList<String> dateTime, ArrayList<String> distance  ) {
        this.mContext = c;
        //transfer content from database to temporary memory
        this.id = id;
        this.lat = lat;
        this.lng = lng;
        this.num = num;
        this.dateTime = dateTime;
        this.distance = distance;

    }

    public int getCount() {
        // TODO Auto-generated method stub
        return id.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    public View getView(int pos, View child, ViewGroup parent) {
        Holder mHolder;
        LayoutInflater layoutInflater;
        if (child == null) {
            layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            child = layoutInflater.inflate(R.layout.listcell, null);
            mHolder = new Holder();

            //link to TextView
            mHolder.txtLat = (TextView) child.findViewById(R.id.lat);
            mHolder.txtLng = (TextView) child.findViewById(R.id.lng);

            mHolder.txtNum = (TextView) child.findViewById(R.id.num);
            mHolder.txtDateTime = (TextView) child.findViewById(R.id.date_time);
            mHolder.txtDistance = child.findViewById(R.id.distance);
            child.setTag(mHolder);
        } else {
            mHolder = (Holder) child.getTag();
        }
        //transfer to TextView in screen
//        mHolder.txtId.setText(id.get(pos));
        mHolder.txtLat.setText(lat.get(pos).toString());
        mHolder.txtLng.setText(lng.get(pos).toString());
        mHolder.txtNum.setText(num.get(pos));
        mHolder.txtDateTime.setText(dateTime.get(pos));
        mHolder.txtDistance.setText(distance.get(pos));

        return child;
    }

    public class Holder {
        TextView txtLat;
        TextView txtLng;
        TextView txtNum;
        TextView txtDateTime;
        TextView txtDistance;
    }

}
