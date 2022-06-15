package com.wz.tony.chick_vendor.Ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.wz.tony.chick_vendor.Public.Manager;
import com.wz.tony.chick_vendor.R;




public class TitleLayout extends LinearLayout {

    ImageButton logout;
    ImageButton refresh;

    public TitleLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.title, this);


        logout = (ImageButton) findViewById(R.id.tLogout);
        refresh = (ImageButton) findViewById(R.id.tRefresh);

        logout.setOnClickListener(clicked);
        refresh.setOnClickListener(clicked);
    }

    OnClickListener clicked = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tLogout:
                    //((Activity) getContext()).finish();

                    alertBox();


                    break;

                case R.id.tRefresh:
                    //Manager.getInst().mainActivity.updateFoodInfoList();
                    Manager.getInst().mainActivity.refrashAction();
                    break;
            }
        }
    };



    private void alertBox(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(((Activity) getContext()));
        //dialog.setTitle("基本訊息對話按鈕");
        dialog.setMessage("確定要登出嗎?");
/*
        dialog.setNegativeButton("NO",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
               // Toast.makeText(MainActivity.this, "我還尚未了解",Toast.LENGTH_SHORT).show();
            }

        });*/
        dialog.setPositiveButton("登出",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                //Toast.makeText(MainActivity.this, "我了解了",Toast.LENGTH_SHORT).show();
                ((Activity) getContext()).finish();
            }

        });


        dialog.setNeutralButton("取消",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                //  Toast.makeText(MainActivity.this, "取消",Toast.LENGTH_SHORT).show();
                arg0.dismiss();
            }

        });
        dialog.show();
    }


}
