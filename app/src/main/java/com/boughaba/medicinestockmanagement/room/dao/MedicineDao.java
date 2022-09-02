package com.boughaba.medicinestockmanagement.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.boughaba.medicinestockmanagement.room.model.Medicine;

import java.util.List;

@Dao
public interface MedicineDao {

    @Query("SELECT * FROM medicine")
    LiveData<List<Medicine>> getAll();

    @Query("DELETE FROM medicine")
    void deleteAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Medicine medicine);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Medicine... medicines);

    @Query("DELETE FROM medicine WHERE barCode NOT IN (:barCodes)")
    void deleteWhereIn(List<String> barCodes);

    @Delete
    void delete(Medicine medicine);

}
