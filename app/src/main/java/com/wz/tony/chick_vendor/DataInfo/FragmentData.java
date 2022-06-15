package com.wz.tony.chick_vendor.DataInfo;

public class FragmentData {

    public String name;
    public int total;
    public int convertedCount;
    public int remainingCount;
    public String imgUrl;


    float discount;  //折扣
    String offer; //優惠
    String foodPackage; //套餐
    String foodActivity; //活動


    public FragmentData(){}
    public void loadData(String name, int total, int convertedCount, int remainingCount, String url) {
        this.name = name;
        this.total = total;
        this.convertedCount = convertedCount;
        this.remainingCount = remainingCount;
        this.imgUrl = url;
    }
}
