package com.wz.tony.chick_vendor.DataInfo;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AppData {

    public static ArrayList<FragmentData> fmList;
    public static ArrayList<RecordData> recordList;
    public static ArrayList<DailyRecordData> dailyRecordList;
    public static Map<String, Object> dailyRecordData;

    //ImageCash
    public static HashMap<String, Bitmap> itemImgCash;

    //URL data
    /*public static String URl_LOGIN = "211.75.180.91:12083/partner/login";
    public static String URl_VODER = "211.75.180.91:12083/partner/data";
    public static String URl_BARCODE = "211.75.180.91:12083/partner/exchange_prepare";
    public static String URl_COVERT = "211.75.180.91:12083/partner/exchange_confirm";*/
    public static String SERVER = "13.124.140.159:12084";
    public static String URl_LOGIN = "/partner/login";
    //public static String URl_VODER = "/partner/data";
    public static String URl_INFO = "/partner/data";
    public static String URl_BARCODE = "/partner/exchange_prepare";
    public static String URl_COVERT = "/partner/exchange_confirm";

    public static String URl_RECORD = "/partner/exchange_record";
    public static String URl_DAILYRECORD = "/partner/exchange_daily_record";



    //Http State
    public static StaticData connState = null;

    public static void setConnState(String state){
        if( connState == null ){
            connState = new StaticData(state);
            return;
        }
        connState.setData(state);
    }
    public static String getConnState(){
        return connState.getData();
    }
    public static void clearConnState(){
        connState = null;
    }


    //URL Token
    public static StaticData myToken = null;

    public static boolean setToken(String token){
        if(token.isEmpty()) return false;
        if(myToken == null){
            myToken = new StaticData(token);
            return true;
        }
        myToken.setData(token);
        return true;
    }

    public static String getToken(){
        if(myToken == null) return null;
        return myToken.getData();
    }

    //Name
    public static String VENDORNAME = "";





    //public static String transKey = "";
    public static StaticData transKey = null;

    public static boolean setTransKey(String token){
        if(token.isEmpty()) return false;
        if(transKey == null){
            transKey = new StaticData(token);
            return true;
        }
        transKey.setData(token);
        return true;
    }

    public static String getTransKey(){
        if(transKey == null) return null;
        return transKey.getData();
    }

    public static void clearTransKey(){
        transKey = null;
    }






    // static data set/ get
    static class StaticData{
        private String data;
        public StaticData(String data){
            this.data = data;
        }

        private void setData( String data ){
            this.data = data;
        }

        private String getData(){
            return this.data;
        }
    }


}




