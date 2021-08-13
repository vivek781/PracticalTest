package com.example.practicaltest;

import java.util.ArrayList;

public class Records {
    public String total_records;
    public String message;

    public ArrayList<AllRecords> array_allrecord;

    public Records()
    {
        total_records = "";
        message = "";
    }

    public ArrayList<AllRecords> getAllData()
    {
        return array_allrecord;
    }

    public void setAllData(ArrayList<AllRecords> arrayData)
    {
        this.array_allrecord = arrayData;
    }
}
