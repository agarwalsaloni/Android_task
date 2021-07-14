package com.risingstar.androidtask.databases;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "data_table")
public class DataEntity {
    @PrimaryKey
    @NonNull
    public String name;

    public DataEntity(String name, String capital, String region, String subRegion, String population, String flag, ArrayList<String> borders, ArrayList<String> languages) {
        this.name = name;
        this.capital = capital;
        this.region = region;
        this.subRegion = subRegion;
        this.population = population;
        this.flag = flag;
        this.borders = borders;
        this.languages = languages;
    }

    @ColumnInfo(name = "capital")
    public String capital;

    @ColumnInfo(name = "region")
    public String region;

    @ColumnInfo(name = "sub_region")
    public String subRegion;

    @ColumnInfo(name = "population")
    public String population;

    @ColumnInfo(name = "flag")
    public String flag;

    @ColumnInfo(name = "borders")
    public ArrayList<String> borders;

    @ColumnInfo(name = "languages")
    public ArrayList<String> languages;

}
