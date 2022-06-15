package com.wz.tony.chick_vendor.DataInfo;

public class DailyRecordData {

    public int projectCount;
    public String projectName;

    public DailyRecordData(){}
    public void loadData(String projectName, int projectCount) {

        this.projectName = projectName;
        this.projectCount = projectCount;
    }

}