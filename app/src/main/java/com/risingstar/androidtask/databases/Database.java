package com.risingstar.androidtask.databases;

import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.risingstar.androidtask.utility.Converters;

@androidx.room.Database(entities = {DataEntity.class},version = 1,exportSchema = false)
@TypeConverters({Converters.class})
public abstract class Database extends RoomDatabase {
    public abstract DataDao dataDao();
}
