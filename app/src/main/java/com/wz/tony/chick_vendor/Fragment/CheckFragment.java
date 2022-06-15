package com.wz.tony.chick_vendor.Fragment;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.wz.tony.chick_vendor.DataInfo.AppData;
import com.wz.tony.chick_vendor.DataInfo.FragmentData;
import com.wz.tony.chick_vendor.R;

public class CheckFragment extends Fragment {

    Button statBarcoad;
    private IntentIntegrator scanIntegrator;
    private Context mContext;
    private Toast mToast;



    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_check, container, false);
    }
/*
    public void load( String barcodData ){
        Log.e("tony", "getData = " + barcodData);
        setBarcodData(barcodData);
    }
*/




    //TextView barcod;
    TextView food;
    TextView total;
    TextView remain;
    TextView convert;

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        food = (TextView) getActivity().findViewById(R.id.food);
        total = (TextView) getActivity().findViewById(R.id.total);
        remain = (TextView) getActivity().findViewById(R.id.remain);
        convert = (TextView) getActivity().findViewById(R.id.convert);

        //barcod = (TextView) getActivity().findViewById( R.id.barcod );
    }

    public void updataFoodData(){
        Log.e("tony", " updataFoodData");
        if(!AppData.fmList.isEmpty()) updataFoodItem( AppData.fmList.get(0) ); //false have a data
    }

    private void updataFoodItem(FragmentData fmData){
        Log.e("tony", " updataFoodItem =" + fmData);
        Log.e("tony", " updataFoodItem =" + fmData.name);

        Resources getRes = getActivity().getResources();

        food.setText(String.format( getRes.getString(R.string.food_name), fmData.name) );
        total.setText(String.format( getRes.getString(R.string.food_total), Integer.toString(fmData.total)) );
        remain.setText(String.format( getRes.getString(R.string.food_remain), Integer.toString(fmData.remainingCount)) );
        convert.setText(String.format( getRes.getString(R.string.food_convert), Integer.toString(fmData.convertedCount)) );
    }
/*
    private void setBarcodData(String barcodData){
        barcod.setText(barcodData);
    }
*/





}
