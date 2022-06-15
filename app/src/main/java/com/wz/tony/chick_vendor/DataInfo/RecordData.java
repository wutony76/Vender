package com.wz.tony.chick_vendor.DataInfo;

public class RecordData {

    public String convertId;
    public String date;
    public String detail_date;
    public String sourceId;
    public String sourceName;
    public int projectCount;
    public String projectName;

    public RecordData(){}
    public void loadData(String convertId, String date, String detail_date, String sourceId, String sourceName, String projectName, int projectCount) {
        this.convertId = convertId;
        this.date = date;
        this.detail_date = detail_date;
        this.sourceId = sourceId;
        this.sourceName = sourceName;
        this.projectName = projectName;
        this.projectCount = projectCount;
    }
}
