package com.risingstar.androidtask.databases;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DataDao {
    @Insert
    void insert(DataEntity dataEntity);

    @Query("DELETE FROM data_table")
    void clearAll();

    @Query("SELECT * FROM data_table")
    List<DataEntity> getAllData();
}
