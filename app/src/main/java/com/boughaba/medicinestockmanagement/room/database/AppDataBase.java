package com.boughaba.medicinestockmanagement.room.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.boughaba.medicinestockmanagement.Converter;
import com.boughaba.medicinestockmanagement.room.dao.MedicineDao;
import com.boughaba.medicinestockmanagement.room.dao.SyncDao;
import com.boughaba.medicinestockmanagement.room.model.Medicine;
import com.boughaba.medicinestockmanagement.room.model.SyncMedicine;

@Database(entities = {Medicine.class, SyncMedicine.class}, version = 4, exportSchema = false)
@TypeConverters({Converter.class})
public abstract class AppDataBase extends RoomDatabase {
    public abstract MedicineDao medicineDao();

    public abstract SyncDao syncDao();

    private static AppDataBase INSTANCE;
    private static final String DB_NAME = "med_db";

    public static AppDataBase getDatabase(Context context){
        if (INSTANCE == null) {
            synchronized (AppDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context,
                            AppDataBase.class, DB_NAME)
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
