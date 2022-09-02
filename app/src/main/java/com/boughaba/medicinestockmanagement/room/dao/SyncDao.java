package com.boughaba.medicinestockmanagement.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.boughaba.medicinestockmanagement.room.model.SyncMedicine;
import com.boughaba.medicinestockmanagement.room.model.SyncMedicineAndMedicine;

import java.util.List;

@Dao
public interface SyncDao {
    @Query("SELECT * FROM SyncMedicine")
    List<SyncMedicine> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SyncMedicine syncMedicine);

    @Delete
    void delete(SyncMedicine syncMedicine);

    @Query("SELECT * FROM SyncMedicine ORDER BY id ASC")
    List<SyncMedicineAndMedicine> getAllWithMedicine();
}
