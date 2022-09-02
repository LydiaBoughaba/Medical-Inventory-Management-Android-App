package com.boughaba.medicinestockmanagement.room.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

//Entity for the medicines to sync from the app to the server
@Entity
public class SyncMedicine implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int action;
    private String medicineId;

    public SyncMedicine() {
    }
    @Ignore
    public SyncMedicine(int id, int action, String medicineId) {
        this.id = id;
        this.action = action;
        this.medicineId = medicineId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(String medicineId) {
        this.medicineId = medicineId;
    }
}
