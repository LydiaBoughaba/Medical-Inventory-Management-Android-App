package com.boughaba.medicinestockmanagement.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.boughaba.medicinestockmanagement.room.model.Medicine;
import com.boughaba.medicinestockmanagement.room.repository.MedicineRepository;

import java.util.List;

public class MedicineViewModel extends AndroidViewModel {

    private LiveData<List<Medicine>> medicines;
    private MedicineRepository repository;

    public MedicineViewModel(@NonNull Application application) {
        super(application);
        repository = new MedicineRepository(application);
        medicines = repository.getAllMedicine();
    }

    public void insert(Medicine medicine) {
        repository.insert(medicine);
    }

    public void delete(Medicine medicine) {
        repository.delete(medicine);
    }

    public LiveData<List<Medicine>> getMedicines() {
        return medicines;
    }

}
