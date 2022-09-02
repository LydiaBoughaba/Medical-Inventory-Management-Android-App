package com.boughaba.medicinestockmanagement.room.model;

import androidx.room.Embedded;
import androidx.room.Relation;

//Relationship between the 2 entities SyncMedicine and Medicine
public class SyncMedicineAndMedicine {
    @Embedded
    private SyncMedicine syncMedicine;
    @Relation(
            parentColumn = "medicineId",
            entityColumn = "barCode"
    )
    private Medicine medicine;

    public SyncMedicine getSyncMedicine() {
        return syncMedicine;
    }

    public void setSyncMedicine(SyncMedicine syncMedicine) {
        this.syncMedicine = syncMedicine;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }
}
